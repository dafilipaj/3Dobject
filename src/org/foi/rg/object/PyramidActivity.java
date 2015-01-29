package org.foi.rg.object;

import org.foi.rg.object.kocka.DisplayKocka;
import org.foi.rg.object.pyramid.DisplayPyramid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class PyramidActivity extends Activity {

	private DisplayPyramid display;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		display = new DisplayPyramid(this);
		setContentView(display);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		
		switch(id){
		case R.id.gumb_kocka:
			if(getClass().equals(org.foi.rg.object.MainActivity.class)){
				return true;
			}else{		
			    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			    startActivity(intent);
				return true;
			}
		case R.id.gumb_piramida:
			if(getClass().equals(org.foi.rg.object.PyramidActivity.class)){
				return true;
			}else{		
			    Intent intent = new Intent(getApplicationContext(), PyramidActivity.class);
			    startActivity(intent);
				return true;
			}
		case R.id.gumb_kuca:
			if(getClass().equals(org.foi.rg.object.KucaActivity.class)){
				return true;
			}else{		
			    Intent intent = new Intent(getApplicationContext(), KucaActivity.class);
			    startActivity(intent);
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	/**
	 * Remember to resume our Lesson
	 */
	@Override
	protected void onResume() {
		super.onResume();
		display.onResume();
	}

	/**
	 * Also pause our Lesson
	 */
	@Override
	protected void onPause() {
		super.onPause();
		display.onPause();
	}

}
