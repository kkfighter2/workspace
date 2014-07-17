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

	//SurfaceView�����󪺰Ѽ�
	private SurfaceHolder m_holder;
	//�۾����󪺰ѷ�
	private Camera m_camera;
	

	
	/**
	 * �|�b��������ק��ܮɩI�s
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//���o�۾���Ҫ��Ѽ�
		Camera.Parameters parameters = m_camera.getParameters();
		//�]�w�Ѽ�
		List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
		//�]�w�^�۾�
		m_camera.setParameters(parameters);
		//���o�䴩�ѪR���Ĥ@��
		Camera.Size previewSize = previewSizes.get(0);
		
		//�]�w�Ѽ�
		parameters.setPictureSize(previewSize.width, previewSize.height);
		//�]�w�s�^�۾�
		m_camera.setParameters(parameters);
		m_camera.startPreview();
	}
	/**
	 * �|�b������Q�ͦ�����I�s
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
	 * ��o�Ӫ�����n�Q���������e�|�Q�I�s�A�Ncamera���^
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
