package com.example.camerastudy_test;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class CombineImages {

	public static Bitmap combineImages(Bitmap bitmap2, Bitmap bitmap1) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
		Bitmap bitmap = null;
	    try {

	        bitmap = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
	        Canvas c = new Canvas(bitmap);
	        


	       

	        
	        Drawable drawable1 = new BitmapDrawable(bitmap1);
	        Drawable drawable2 = new BitmapDrawable(bitmap2);


	        drawable1.setBounds(100, 100, 400, 400);
	        drawable2.setBounds(150, 150, 350, 350);
	        drawable1.draw(c);
	        drawable2.draw(c);


	    } catch (Exception e) {
	    }
	    return bitmap;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/**
		Bitmap cs = null; 

	    int width, height = 0; 

	    if(c.getWidth() > s.getWidth()) { 
	      width = c.getWidth() + s.getWidth(); 
	      height = c.getHeight(); 
	    } else { 
	      width = s.getWidth() + s.getWidth(); 
	      height = c.getHeight(); 
	    } 

	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 

	    Canvas comboImage = new Canvas(cs); 

	    comboImage.drawBitmap(c, 0f, 0f, null); 
	    comboImage.drawBitmap(s, c.getWidth(), 0f, null); 

	    // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location 
	    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png"; 

	    OutputStream os = null; 
	    try { 
	      os = new FileOutputStream(loc + tmpImg); 
	      cs.compress(CompressFormat.PNG, 100, os); 
	    } catch(IOException e) { 
	      Log.e("combineImages", "problem combining images", e); 
	    } 

	    return cs;
	    **/
	  } 
}
