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
	//�Nfilename�]�������ܼ�
	String filename = "temp.png";
	//String filename = String.format("%1$d.png",System.currentTimeMillis());
	
	//�{���Ĥ@��created�ɩI�s
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setFullScreen();
		setContentView(R.layout.main);
		//�s�W�۾��w���e��
		m_cameraPreviewSurface = new CameraPreviewSurface(this);
		SurfaceView surfaceView = (SurfaceView)findViewById(R.id.preview);
		SurfaceHolder surfaceHolder =  surfaceView.getHolder();
		surfaceHolder.addCallback(m_cameraPreviewSurface);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//�B�zUI���^���{��
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
	 * ���ù�����
	 */
	private void setFullScreen(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	/**
	 * �갵�����s���^�����
	 */
	public void onClick(View arg0) {
		Button clickedButton = (Button) arg0;
		if (clickedButton.getId() == R.id.shutterButton){
			//���U�������,�����^���e��
			m_cameraPreviewSurface.getCamera().takePicture(myShutterCallBack, null, myPngCallback);
			Log.e("filename", filename);
			// �إߤ@��Bundle�H�ǰe�Ѽƨ�U�@��Activity
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
			//��ܥX�o�۾����ѼƳ]�w
			Camera.Parameters parameters = m_cameraPreviewSurface.getCamera().getParameters();
			Toast.makeText(this, parameters.flatten(), Toast.LENGTH_LONG).show();
			
			}
		if(clickedButton.getId()== R.id.autoFocusButton){
			//���U��J�A�����榨�\�᪺�^���禡
			m_cameraPreviewSurface.getCamera().autoFocus(myAutoFocusCallback);
			}
		}
		
	/**
	 * ��ϥΪ̩԰�seekbar�|�ܧ�J�Z
	 */
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2){
		//�]�w�̰��ܲv��Bar���W��
		SeekBar seekBar = (SeekBar) findViewById (R.id.zoomRate);
		seekBar.setMax(m_cameraPreviewSurface.getCamera().getParameters().getMaxZoom());
		//�]�w�C���ܰʨ�׬��@
		seekBar.setKeyProgressIncrement(1);
		//�p�G���v�O���`���v
		if (arg1 >= 1){
			//��s���v����r����
			TextView zoomLabel=(TextView)findViewById(R.id.zoomLabel);
			String zoomString = String.format("%1$dX", arg1);
			zoomLabel.setText(zoomString);
			if (m_cameraPreviewSurface.getCamera().getParameters().isSmoothZoomSupported()){
				//�䴩�����ܵJ�A�N�Υ����ܵJ
				m_cameraPreviewSurface.getCamera().startSmoothZoom(arg1);
			}else if (m_cameraPreviewSurface.getCamera().getParameters().isZoomSupported()){
				//�p�G�u�䴩���q�ܵJ�A�N�δ��q�ܵJ
				
				Camera.Parameters parameters = m_cameraPreviewSurface.getCamera().getParameters();//�o����D�����D�X�b����
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
	 * ��J���|�����I�s
	 */
	Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
		
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if(success){
				//�p�G��J���\�N�N�Ӥ��x�s
				camera.takePicture(myShutterCallBack, null, myPngCallback);
        		 
			}else {
				//�p�G���ѴN��ܿ��~�T��
				Toast.makeText(Init.this, "Can not Focus!!", Toast.LENGTH_LONG).show();
			}
			
		}//onAutoFocus
	};
	
	
	/**
	 * ��v���^���ɩI�s
	 */
	
	Camera.ShutterCallback myShutterCallBack = new Camera.ShutterCallback(){
		public void onShutter(){
		//��ܤ@��toast�i�D�ϥΪ̥��A�B�z�v��
		Toast.makeText(Init.this, "Shutter started", Toast.LENGTH_LONG).show();
		}//onShutter
	
	};
	
	/**
	 * ��JPEG�Q���Y�n�ɩI�s
	 */
	
	Camera.PictureCallback myPngCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null ;
			try{
				//��J�o�ӵ{��SD�d�~���Ŷ�
				//File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),filename);
				File file = new File(Environment.getExternalStorageDirectory()
                        + "/cloze/"+filename);
				Log.e("path", file.getPath());
				outStream = new FileOutputStream(file);
				outStream.write(data);
				outStream.close();
				Toast.makeText(Init.this, "�ۤ��j�p:"+data.length,Toast.LENGTH_LONG ).show();
			}catch(FileNotFoundException e){
				Toast.makeText(Init.this, "File NOT Found,�ɮ׵L�k�g�J",Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}catch (IOException e){
				Toast.makeText(Init.this, "IO error,�ɮ׵L�k�g�J", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}finally{
				//�~�����
				m_cameraPreviewSurface.getCamera().startPreview();
			}
			
		}//onPictureTaken
	};//mypngcallback
	
	
	
	
	
	
	}
