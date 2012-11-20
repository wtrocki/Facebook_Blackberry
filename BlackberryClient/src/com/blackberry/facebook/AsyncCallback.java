package com.blackberry.facebook;

import com.blackberry.facebook.model.IObject;

import net.rim.device.api.system.Bitmap;

public interface AsyncCallback {

	public void onComplete(IObject[] values, java.lang.Object state);

	public void onComplete(Bitmap[] values, java.lang.Object state);

	public void onException(Exception e, java.lang.Object state);
}