/**
 * Copyright (c) E.Y. Baskoro, Research In Motion Limited.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * This License shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * The name(s) of the above copyright holders shall not be used 
 * in advertising or otherwise to promote the sale, use or other 
 * dealings in this Software without prior written authorization.
 * 
 */
package com.blackberry.facebook.dao;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Education;
import com.blackberry.facebook.inf.Profile;

public class FacebookEducation extends FacebookObject implements Education {

	public FacebookEducation(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		super(pfb, pJsonObject);
	}

	public String getType() {
		return jsonObject.optString("type");
	}

	public Profile getSchool() {
		Profile result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("school");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getProfile(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Profile getDegree() {
		Profile result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("degree");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getProfile(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Profile getYear() {
		Profile result = null;
		try {
			JSONObject jo = jsonObject.optJSONObject("year");
			if ((jo != null) && (jo.length() > 0)) {
				result = fb.getProfile(jo);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Profile[] getConcentration() {
		Profile[] result = null;
		try {
			JSONArray ja = jsonObject.optJSONArray("concentration");
			if ((ja != null) && (ja.length() > 0)) {
				result = new Profile[ja.length()];
				for (int i = 0; i < ja.length(); i++) {
					result[i] = fb.getProfile(ja.optJSONObject(i));
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
		return result;
	}

}
