package com.example.camerastudy_test;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraPreviewSurface extends SurfaceView implements Callback {
	public CameraPreviewSurface(Context context) {
		super(context);
		m_holder = getHolder();
		m_holder.addCallback(this);
		m_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	//SurfaceView表面物件的參數
	private SurfaceHolder m_holder;
	//相機物件的參照
	private Camera m_camera;
	

	
	/**
	 * 會在表面物件維度改變時呼叫
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//取得相機實例的參數
		Camera.Parameters parameters = m_camera.getParameters();
		//設定參數
		List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
		//設定回相機
		m_camera.setParameters(parameters);
		//取得支援解析的第一組
		Camera.Size previewSize = previewSizes.get(0);
		
		//設定參數
		parameters.setPictureSize(previewSize.width, previewSize.height);
		//設定存回相機
		m_camera.setParameters(parameters);
		m_camera.startPreview();
	}
	/**
	 * 會在表面物件被生成之後呼叫
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		m_camera = Camera.open();
		try{
			m_camera.setPreviewDisplay(holder);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	/***
	 * 當這個表面物件要被消滅掉之前會被呼叫，將camera收回
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (m_camera != null){
			m_camera.stopPreview();
			m_camera.release();
			m_camera = null;
		}

	}
	
	public Camera getCamera(){
		return m_camera;
	}

}
