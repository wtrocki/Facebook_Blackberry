/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.ui;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class ProgressAnimationField extends Field implements Runnable {
	private Bitmap _bitmap;
	private int _numFrames;
	private int _frameWidth;
	private int _frameHeight;

	private int _currentFrame;
	private int _timerID = -1;

	private Application _application;
	private boolean _visible;

	public ProgressAnimationField(Bitmap bitmap, int numFrames, long style) {
		super(style | Field.NON_FOCUSABLE);
		_bitmap = bitmap;
		_numFrames = numFrames;
		_frameWidth = _bitmap.getWidth() / _numFrames;
		_frameHeight = _bitmap.getHeight();

		_application = Application.getApplication();
	}

	public void run() {
		if (_visible) {
			invalidate();
		}
	}

	protected void layout(int width, int height) {
		setExtent(_frameWidth, _frameHeight);
	}

	protected void paint(Graphics g) {
		g.drawBitmap(0, 0, _frameWidth, _frameHeight, _bitmap, _frameWidth * _currentFrame, 0);
		_currentFrame++;
		if (_currentFrame >= _numFrames) {
			_currentFrame = 0;
		}
	}

	protected void onDisplay() {
		super.onDisplay();
		_visible = true;
		if (_timerID == -1) {
			_timerID = _application.invokeLater(this, 200, true);
		}
	}

	protected void onUndisplay() {
		super.onUndisplay();
		_visible = false;
		if (_timerID != -1) {
			_application.cancelInvokeLater(_timerID);
			_timerID = -1;
		}
	}
}
