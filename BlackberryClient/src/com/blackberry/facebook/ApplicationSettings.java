package com.blackberry.facebook;

public class ApplicationSettings {

	protected String nextUrl = null;
	protected String applicationId = null;
	protected String applicationSecret = null;
	protected String[] permissions = null;

	public ApplicationSettings(String pNextUrl, String pApplicationId, String pApplicationSecret, String[] pPermissions) {
		nextUrl = pNextUrl;
		applicationId = pApplicationId;
		applicationSecret = pApplicationSecret;
		permissions = pPermissions;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String pNextUrl) {
		nextUrl = pNextUrl;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String pApplicationId) {
		applicationId = pApplicationId;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public String getPermissionsString() {
		String result = "";
		if ((permissions != null) && (permissions.length > 0)) {
			for (int i = 0; i < permissions.length; i++) {
				if (i != 0) {
					result += ",";
				}
				result += permissions[i].trim();
			}
		}

		return result;
	}

	public void setPermissions(String[] pPermissions) {
		permissions = pPermissions;
	}

}
