package com.example.camerastudy_test;

import java.io.*;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

	public class PicturePreview extends Activity {
		private ImageView preview;
		private Button okButton;
		private Button retakeButton;
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.picture_preview);
			preview = (ImageView)findViewById(R.id.preview);
			okButton = (Button)findViewById(R.id.okButton);
			retakeButton = (Button)findViewById(R.id.retakeButton);
			setListeners();
			//框架轉bitmap
			Bitmap maskBitmap = resintobitmap();
			
			//接收bundle
			Bundle bundle = getIntent().getExtras();
			String filename =  bundle.getString("filename");
			
			//String filename = "1407123533348.png";
			//String filename = "1406011418687.png";
			
			Log.e("filename", filename);
			
			String pathName = Environment.getExternalStorageDirectory() +"/cloze/"+filename;
			Log.e("pathname", pathName);
			
			//照片轉bitmap
			Bitmap photoBitmap = BitmapFactory.decodeFile(pathName);
			Bitmap result = CombineImages.combineImages(maskBitmap,photoBitmap);
			preview.setImageBitmap(result);
			/**
			String path = Environment.getExternalStorageDirectory()+"/cloze/"+filename+".png";
			Log.e("path", path);
			Bitmap bmp = BitmapFactory.decodeFile(path);
			preview.setImageBitmap(bmp);
			**/
		}//oncreate
		
		public Bitmap resintobitmap(){
			Resources res = getResources(); 
			Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.circle); 
			return bmp;
		}
		private void setListeners(){
			okButton.setOnClickListener(new Button.OnClickListener(){
	            public void onClick(View v) {
	            	//確認儲存，跳去背程式
	            }
	        });
			retakeButton.setOnClickListener(new Button.OnClickListener(){
	            public void onClick(View v) {
	            	//取消，重新拍照
	            }
	        });
	        
		}
		//將剛拍完的照片轉成bitmap
		/**
		public Bitmap photointobitmap(File maskFile){
			Drawable drawable = Drawable.createFromPath(maskFile.getAbsolutePath());
			BitmapDrawable BD = (BitmapDrawable) drawable;
			Bitmap BM = BD.getBitmap();
			return BM;
		}
		**/
				
	}
