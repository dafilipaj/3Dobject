package org.foi.rg.object.pyramid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Pyramid {
	   private FloatBuffer vrhoviBuffer; 
	   private ByteBuffer indexiBuffer;
	   private FloatBuffer bojeBuffer;
	    
	   private float[] vrhovi = {
	      -1.0f, -1.0f, -1.0f,  
	       1.0f, -1.0f, -1.0f,  
	       1.0f, -1.0f,  1.0f, 
	      -1.0f, -1.0f,  1.0f,  
	       0.0f,  1.0f,  0.0f  
	   };
	   
	   private float[] boje = {
			      0.0f, 0.0f, 1.0f, 1.0f,
			      0.0f, 1.0f, 0.0f, 1.0f,
			      0.0f, 0.0f, 1.0f, 1.0f,
			      0.0f, 1.0f, 0.0f, 1.0f,
			      1.0f, 0.0f, 0.0f, 1.0f
			   };
	          
	   private byte[] indexi = {
	      2, 4, 3,
	      1, 4, 2, 
	      0, 4, 1, 
	      4, 0, 3 
	   };
	   
	   public Pyramid(){
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(vrhovi.length*4);
			byteBuf.order(ByteOrder.nativeOrder());
			vrhoviBuffer = byteBuf.asFloatBuffer().put(vrhovi); //dodavanje podataka
			vrhoviBuffer.position(0); //postavljanje na nulti element
			
			indexiBuffer = ByteBuffer.allocateDirect(indexi.length).put(indexi);
			indexiBuffer.position(0);
			
			ByteBuffer bojebytebuffer = ByteBuffer.allocateDirect(boje.length * 4);
			bojebytebuffer.order(ByteOrder.nativeOrder());
			bojeBuffer = bojebytebuffer.asFloatBuffer();
			bojeBuffer.put(boje);
			bojeBuffer.position(0);
	   }
	   
	   public void init(GL10 gl){
			//aktivacija polja
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);			
			gl.glFrontFace(GL10.GL_CCW);
			
			//pointer na polja
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vrhoviBuffer);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, bojeBuffer);
			
			gl.glDrawElements(GL10.GL_TRIANGLES, indexi.length, GL10.GL_UNSIGNED_BYTE, indexiBuffer);
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	   }
}
