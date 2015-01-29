package org.foi.rg.object.pyramid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.foi.rg.object.pyramid.Pyramid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class DisplayPyramid extends GLSurfaceView implements Renderer, SensorEventListener{

	private Pyramid piramida;

	//rotacija
	private float xkut;
	private float ykut;
	
	//dubina;
	private float zos = -6.3f;
	
	//inicijalizacija senzora
	private final SensorManager SensorM;
	private final Sensor orientation;
	
	//položaj kamere
	Float bazax;
	Float bazay;
	Float bazaz;
	
	private Context context;
	
	public DisplayPyramid(Context context) {
		super(context);
		SensorM = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		orientation = SensorM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	   
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		
		//objekt
		piramida = new Pyramid();
	}

	private static final float NS2S = 1.0f / 1000000000.0f;
	private final float[] deltaRotationVector = new float[4];
	private float timestamp;


	//Rotacija objekta s obzirom na promjene senzora
	@Override
	public void onSensorChanged(SensorEvent event) {
		
			if (timestamp != 0) {
			    final float dT = (event.timestamp - timestamp) * NS2S;
			    // Axis of the rotation sample, not normalized yet.
			    float axisX = event.values[0];
			    float axisY = event.values[1];
			    float axisZ = event.values[2];

			    // Calculate the angular speed of the sample
			    float omegaMagnitude = (float)Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

			    // Normalize the rotation vector if it's big enough to get the axis
			    // (that is, EPSILON should represent your maximum allowable margin of error)
			    if (omegaMagnitude > 50) {
			      axisX /= omegaMagnitude;
			      axisY /= omegaMagnitude;
			      axisZ /= omegaMagnitude;
			    }

			
		    float thetaOverTwo = omegaMagnitude * dT / 2.0f;
		    float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
		    float cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
		    deltaRotationVector[0] = sinThetaOverTwo * axisX;
		    deltaRotationVector[1] = sinThetaOverTwo * axisY;
		    deltaRotationVector[2] = sinThetaOverTwo * axisZ;
		    deltaRotationVector[3] = cosThetaOverTwo;
		    
			}
			timestamp = event.timestamp;
			if ( null == bazax ) {
				bazax = (float)Math.toDegrees(deltaRotationVector[0]);
			}
			if ( null == bazay ) {
				bazay = (float)Math.toDegrees(deltaRotationVector[1]);
			}
			if ( null == bazaz ) {
				bazaz = (float)Math.toDegrees(deltaRotationVector[2]);
			}
			
			float diffx = (float)Math.toDegrees(deltaRotationVector[0]) - bazax;
			float diffy = (float)Math.toDegrees(deltaRotationVector[1]) - bazay;
			float diffz = (float)Math.toDegrees(deltaRotationVector[2]) - bazaz;
			
			xkut -= diffx;
			ykut -= diffy;
			
			bazax = (float)Math.toDegrees(deltaRotationVector[0]);
			bazay = (float)Math.toDegrees(deltaRotationVector[1]);
			bazaz = (float)Math.toDegrees(deltaRotationVector[2]);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	//radi poput init()
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.2f, 0.5f, 0.6f, 1f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 	
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height); 
		gl.glMatrixMode(GL10.GL_PROJECTION); 
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 
		gl.glLoadIdentity(); 
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
		
		gl.glTranslatef(0.0f, 0.0f, zos);	
		gl.glScalef(0.8f, 0.8f, 0.8f);
		
		gl.glRotatef(xkut, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ykut, 0.0f, 1.0f, 0.0f);
				
		piramida.init(gl);
		
	}
	
	@Override
	public void onPause() {
		SensorM.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		SensorM.registerListener(this, orientation, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

}
