/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.objects;

import net.rim.device.api.system.Bitmap;

import com.blackberry.facebook.AsyncCallback;

public interface User extends com.blackberry.facebook.objects.Object {

	public String getName();

	public String getFirstName();

	public String getLastName();

	public String getGender();

	public String getLocale();

	public String getLink();

	public String getThirdPartyId();

	public double getTimezone();

	public String getUpdatedTimeAsString();

	public boolean getVerified();

	public String getAbout();

	public String getBio();

	public String getBirthdayAsString();

	public String getEmail();

	public String[] getInterestedIn();

	public String[] getMeetingFor();

	public String getPolitical();

	public String getQuotes();

	public String getRelationshipStatus();

	public String getReligion();

	public String getWebsite();

	public Bitmap getPicture(int pictureType);

	public Bitmap getPicture(final int pPictureType,
			final AsyncCallback listener, final java.lang.Object state);

}