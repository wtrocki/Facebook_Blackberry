/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.model;

public interface IObject {

	public void fetch(boolean force);

	public String getId();

	public boolean isStub();

}
