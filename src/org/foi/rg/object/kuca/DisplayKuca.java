package org.foi.rg.object.kuca;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.foi.rg.object.kocka.Kocka;
import org.foi.rg.object.pyramid.Pyramid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class DisplayKuca extends GLSurfaceView implements Renderer, SensorEventListener{

	private Pyramid piramida;
	private Kocka kocka;

	//rotacija
	private float xkut;
	private float ykut;
	private float zkut;
	
	//dubina;
	private float zos = -6.3f;
	
	//inicijalizacija senzora
	private final SensorManager SensorM;
	private final Sensor accelerometer;
	private final Sensor magnetometer;
	
	//položaj kamere
	Float bazaAzimut;
	Float bazaPitch;
	Float bazaRoll;
	
	
	private Context context;
	
	public DisplayKuca(Context context) {
		super(context);
		SensorM = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = SensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer = SensorM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	  
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		
		//objekti
		piramida = new Pyramid();
		kocka = new Kocka();
	}


	float[] mGravity;
	float[] mGeomagnetic;
	//Rotacija objekta s obzirom na promjene senzora
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		      mGravity = event.values;
		    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		      mGeomagnetic = event.values;
		    if (mGravity != null && mGeomagnetic != null) {
		      float R[] = new float[9];
		      float I[] = new float[9];
		      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
		      if (success) {
		        float orientation[] = new float[3];
		        SensorManager.getOrientation(R, orientation);
		        float azimut = (float)Math.toDegrees(orientation[0])*0.1f;
		        float pitch = (float)Math.toDegrees(orientation[1])*0.8f;
		        float roll  = (float)Math.toDegrees(orientation[2])*0.8f;
		        
				if ( null == bazaAzimut ) {
					bazaAzimut = azimut;
				}
				if ( null == bazaPitch ) {
					bazaPitch = pitch;
				}
				if ( null == bazaRoll ) {
					bazaRoll = roll;
				}
				
				float azimuthDifference = azimut - bazaAzimut;
				float pitchDifference = pitch - bazaPitch;
				float rollDifference = roll - bazaRoll;
				
				ykut -= rollDifference;
				xkut += pitchDifference;
				//za manje "trzanja" zakomentirati zos
				zos += azimuthDifference;
								
				bazaRoll = roll;
				bazaPitch = pitch;
				bazaAzimut = azimut;
		      }
		   }
		  
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
		
		gl.glTranslatef(0.0f, 0.8f, zos);	
		gl.glScalef(0.8f, 0.8f, 0.8f);
		gl.glRotatef(xkut, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ykut, 0.0f, 1.0f, 0.0f);
		piramida.init(gl);
		gl.glTranslatef(0.0f, -1.6f, 0.0f);	
		gl.glScalef(0.8f, 0.8f, 0.8f);
		kocka.init(gl);
		
	}
	
	@Override
	public void onPause() {
		SensorM.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		
		SensorM.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		SensorM.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

}
