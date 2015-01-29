package org.foi.rg.object.kocka;

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
import android.view.MotionEvent;

public class DisplayKocka extends GLSurfaceView implements Renderer, SensorEventListener{

	private Kocka kocka;
	private Pyramid piramida;
	
	//svjetlost
	private float[] svijetloAmbient = {1.0f, 0.5f, 0.5f, 1.0f};
	private float[] svijetloBijelo = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] crno = {0.0f, 0.0f, 0.0f, 1.0f};
	private float[] goldenrod = {218.0f / 255.0f, 165.0f / 255.0f, 32.0f / 255.0f};
	private FloatBuffer svijetloAmbientBuff;
	private FloatBuffer svijetloBijeloBuff;
	private FloatBuffer goldenrodBuff;

	//rotacija
	private float xkut;
	private float ykut;
	private float xspremiste;
	private float yspremiste;
	
	//dubina;
	private float zos = -6.3f;
	
	//inicijalizacija senzora
	private final SensorManager SensorM;
	private final Sensor Orientation;
	
	//položaj kamere
	Float bazaAzimut;
	Float bazaPitch;
	Float bazaRoll;
	
	
	private Context context;
	
	public DisplayKocka(Context context) {
		super(context);
		SensorM = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Orientation = SensorM.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(svijetloAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		svijetloAmbientBuff = byteBuf.asFloatBuffer().put(svijetloAmbient);
		svijetloAmbientBuff.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(svijetloBijelo.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		svijetloBijeloBuff = byteBuf.asFloatBuffer().put(svijetloBijelo);
		svijetloBijeloBuff.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(goldenrod.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		goldenrodBuff = byteBuf.asFloatBuffer().put(goldenrod);
		goldenrodBuff.position(0);
		
		kocka = new Kocka();
	}


	//Rotacija objekta s obzirom na promjene senzora
	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimut = event.values[0];
		float pitch = event.values[1];
		float roll = event.values[2];
		
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
		
		ykut += rollDifference;
		xkut += pitchDifference;
				
		bazaRoll = roll;
		bazaPitch = pitch;		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	//radi poput init()
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//postavljanje svjetla
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, svijetloAmbientBuff);
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, svijetloBijeloBuff);
		gl.glLightfv(GL10.GL_LIGHT2, GL10.GL_DIFFUSE, goldenrodBuff);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glEnable(GL10.GL_LIGHT2);

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
		
		gl.glEnable(GL10.GL_LIGHTING);

		
		gl.glTranslatef(0.0f, 0.0f, zos);	
		gl.glScalef(0.8f, 0.8f, 0.8f);
		
		gl.glRotatef(xkut, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ykut, 0.0f, 1.0f, 0.0f);
				
		kocka.init(gl);		
	}
	
	@Override
	public void onPause() {
		SensorM.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		
		SensorM.registerListener(this, Orientation, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}
	
}
