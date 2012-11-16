/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
/*******************************************************************************
 *******************************************************************************/
package com.blackberry.facebook.objects;

import com.blackberry.facebook.AsyncCallback;

public interface Profile extends com.blackberry.facebook.objects.Object {

	// Properties

	public String getName();

	public com.blackberry.facebook.objects.Object toObject();

	public com.blackberry.facebook.objects.Object toObject(final AsyncCallback listener, final java.lang.Object state);

}