/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
/*******************************************************************************
 *******************************************************************************/
package com.blackberry.facebook.inf;

import com.blackberry.facebook.AsyncCallback;

public interface Profile extends com.blackberry.facebook.inf.Object {

	// Properties

	public String getName();

	public com.blackberry.facebook.inf.Object toObject();

	public com.blackberry.facebook.inf.Object toObject(final AsyncCallback listener, final java.lang.Object state);

}