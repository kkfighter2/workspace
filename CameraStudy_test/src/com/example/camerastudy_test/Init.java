package com.example.camerastudy_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class Init extends Activity implements OnClickListener,SeekBar.OnSeekBarChangeListener {
	private CameraPreviewSurface m_cameraPreviewSurface;
	//將filename設為全域變數
	String filename = "temp.png";
	//String filename = String.format("%1$d.png",System.currentTimeMillis());
	
	//程式第一次created時呼叫
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.main);
		//新增相機預覽畫面
		m_cameraPreviewSurface = new CameraPreviewSurface(this);
		SurfaceView surfaceView = (SurfaceView)findViewById(R.id.preview);
		SurfaceHolder surfaceHolder =  surfaceView.getHolder();
		surfaceHolder.addCallback(m_cameraPreviewSurface);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//處理UI的回應程式
		Button shutterButton = (Button)findViewById(R.id.shutterButton);
		shutterButton.setOnClickListener(this);
		Button autoFocusButton = (Button) findViewById(R.id.autoFocusButton);
		autoFocusButton.setOnClickListener(this);
		
		Button infoButton = (Button)findViewById(R.id.infoButton);
		infoButton.setOnClickListener(this);
		SeekBar seekBar=(SeekBar)findViewById(R.id.zoomRate);
		seekBar.setOnClickListener(this);
	}
	
	
	/**
	 * 全螢幕執行
	 */
	private void setFullScreen(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	/**
	 * 實做按按鈕的回應函數
	 */
	public void onClick(View arg0) {
		Button clickedButton = (Button) arg0;
		if (clickedButton.getId() == R.id.shutterButton){
			//按下直接拍照,直接擷取畫面
			m_cameraPreviewSurface.getCamera().takePicture(myShutterCallBack, null, myPngCallback);
			Log.e("filename", filename);
			// 建立一個Bundle以傳送參數到下一個Activity
			Intent intent = new Intent();
		    Bundle bundle = new Bundle();
		    bundle.putString("filename", filename);
		    intent.setClass(Init.this, PicturePreview.class);
		    intent.putExtras(bundle);
		    // Turn to Edit info activity
			startActivity(intent);
			//Init.this.finish();
			}
		if (clickedButton.getId()==R.id.infoButton){
			//顯示出這相機的參數設定
			Camera.Parameters parameters = m_cameraPreviewSurface.getCamera().getParameters();
			Toast.makeText(this, parameters.flatten(), Toast.LENGTH_LONG).show();
			
			}
		if(clickedButton.getId()== R.id.autoFocusButton){
			//按下對焦，執行對交成功後的回應函式
			m_cameraPreviewSurface.getCamera().autoFocus(myAutoFocusCallback);
			}
		}
		
	/**
	 * 當使用者拉動seekbar會變更焦距
	 */
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2){
		//設定最高變率為Bar的上限
		SeekBar seekBar = (SeekBar) findViewById (R.id.zoomRate);
		seekBar.setMax(m_cameraPreviewSurface.getCamera().getParameters().getMaxZoom());
		//設定每次變動刻度為一
		seekBar.setKeyProgressIncrement(1);
		//如果倍率是正常倍率
		if (arg1 >= 1){
			//更新倍率的文字提示
			TextView zoomLabel=(TextView)findViewById(R.id.zoomLabel);
			String zoomString = String.format("%1$dX", arg1);
			zoomLabel.setText(zoomString);
			if (m_cameraPreviewSurface.getCamera().getParameters().isSmoothZoomSupported()){
				//支援平順變焦，就用平順變焦
				m_cameraPreviewSurface.getCamera().startSmoothZoom(arg1);
			}else if (m_cameraPreviewSurface.getCamera().getParameters().isZoomSupported()){
				//如果只支援普通變焦，就用普通變焦
				
				Camera.Parameters parameters = m_cameraPreviewSurface.getCamera().getParameters();//這邊問題不知道出在哪邊
				parameters.setZoom(arg1);
				m_cameraPreviewSurface.getCamera().setParameters(parameters);
			}
			
		}
		
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		
		
	}

	/**
	 * 對焦完會完成呼叫
	 */
	Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
		
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if(success){
				//如果對焦成功就將照片儲存
				camera.takePicture(myShutterCallBack, null, myPngCallback);
        		 
			}else {
				//如果失敗就顯示錯誤訊息
				Toast.makeText(Init.this, "Can not Focus!!", Toast.LENGTH_LONG).show();
			}
			
		}//onAutoFocus
	};
	
	
	/**
	 * 當影像擷取時呼叫
	 */
	
	Camera.ShutterCallback myShutterCallBack = new Camera.ShutterCallback(){
		public void onShutter(){
		//顯示一個toast告訴使用者正再處理影像
		Toast.makeText(Init.this, "Shutter started", Toast.LENGTH_LONG).show();
		}//onShutter
	
	};
	
	/**
	 * 當JPEG被壓縮好時呼叫
	 */
	
	Camera.PictureCallback myPngCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null ;
			try{
				//放入這個程式SD卡外部空間
				//File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),filename);
				File file = new File(Environment.getExternalStorageDirectory()
                        + "/cloze/"+filename);
				Log.e("path", file.getPath());
				outStream = new FileOutputStream(file);
				outStream.write(data);
				outStream.close();
				Toast.makeText(Init.this, "相片大小:"+data.length,Toast.LENGTH_LONG ).show();
			}catch(FileNotFoundException e){
				Toast.makeText(Init.this, "File NOT Found,檔案無法寫入",Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}catch (IOException e){
				Toast.makeText(Init.this, "IO error,檔案無法寫入", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}finally{
				//繼續拍攝
				m_cameraPreviewSurface.getCamera().startPreview();
			}
			
		}//onPictureTaken
	};//mypngcallback
	
	
	
	
	
	
	}
