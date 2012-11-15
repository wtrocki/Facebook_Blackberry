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

import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.util.json.JSONObject;

public class FacebookObject implements com.blackberry.facebook.inf.Object {

	protected Facebook fb;
	protected JSONObject jsonObject;

	public FacebookObject(Facebook pfb, JSONObject pJsonObject) throws FacebookException {
		if ((pfb == null) || (pJsonObject == null)) {
			throw new FacebookException("Unable to create Facebook FacebookObject.");
		}
		fb = pfb;
		jsonObject = pJsonObject;
	}

	public void fetch(boolean force) {
		try {
			if (isStub() || force) {
				String id = getId();
				if ((id != null) && !id.trim().equals("")) {
					JSONObject jo;
					jo = fb.read(id.trim());
					if ((jo != null) && (jo.length() > 0)) {
						jsonObject = jo;
					}
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return jsonObject.optString("id");
	}

	public boolean isStub() {
		return false;
	}

}
