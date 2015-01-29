package org.foi.rg.object.kocka;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Kocka {
	/*
	 * DoubleBuffer u naprednim verzijama OpenGL-a, koristim float.
	 */
	private FloatBuffer vrhoviBuffer;
	private FloatBuffer normaleBuffer;
	private ByteBuffer indexiBuffer;
	
	private float vrhovi[]={
			-1.0f, -1.0f, 1.0f, 
			1.0f, -1.0f, 1.0f, 
			-1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 

			1.0f, -1.0f, 1.0f, 
			1.0f, -1.0f, -1.0f, 
			1.0f, 1.0f, 1.0f, 
			1.0f, 1.0f, -1.0f,

			1.0f, -1.0f, -1.0f, 
			-1.0f, -1.0f, -1.0f, 
			1.0f, 1.0f, -1.0f, 
			-1.0f, 1.0f, -1.0f,

			-1.0f, -1.0f, -1.0f, 
			-1.0f, -1.0f, 1.0f, 
			-1.0f, 1.0f, -1.0f, 
			-1.0f, 1.0f, 1.0f,

			-1.0f, -1.0f, -1.0f, 
			1.0f, -1.0f, -1.0f, 
			-1.0f, -1.0f, 1.0f, 
			1.0f, -1.0f, 1.0f,

			-1.0f, 1.0f, 1.0f, 
			1.0f, 1.0f, 1.0f, 
			-1.0f, 1.0f, -1.0f, 
			1.0f, 1.0f, -1.0f, 
								};
	
	private float normale[]={
			0.0f, 0.0f, 1.0f, 						
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f, 
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
								
	};
	
	private byte indexi[]={
			0, 1, 3, 0, 3, 2,
			4, 5, 7, 4, 7, 6, 
			8, 9, 11, 8, 11, 10, 
			12, 13, 15, 12, 15, 14, 
			16, 17, 19, 16, 19, 18, 
			20, 21, 23, 20, 23, 22, 
									
	};
	
	
	public Kocka(){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vrhovi.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		vrhoviBuffer = byteBuf.asFloatBuffer().put(vrhovi); //dodavanje podataka
		vrhoviBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(normale.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		normaleBuffer = byteBuf.asFloatBuffer().put(normale);
		normaleBuffer.position(0);
		
		indexiBuffer = ByteBuffer.allocateDirect(indexi.length);
		indexiBuffer.put(indexi);
		indexiBuffer.position(0);
		
		
	}
	
	
	public void init(GL10 gl){
		//aktivacija polja
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	
		gl.glFrontFace(GL10.GL_CCW);
		
		//pointer na polja
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vrhoviBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normaleBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indexi.length, GL10.GL_UNSIGNED_BYTE, indexiBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}
	
	
}
