/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
/*******************************************************************************
 *******************************************************************************/
package com.blackberry.facebook.inf;

import net.rim.device.api.system.Bitmap;

public interface Application extends com.blackberry.facebook.inf.Object {

	// Properties

	public String getName();

	public String getDescription();

	public String getCategory();

	public String getLink();

	public Bitmap getPicture();

}