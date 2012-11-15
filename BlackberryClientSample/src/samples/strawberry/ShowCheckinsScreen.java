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
package samples.strawberry;

import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Checkin;
import com.blackberry.facebook.inf.Page;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.User;

public class ShowCheckinsScreen extends MainScreen {

	protected Facebook fb;
	protected ListField listField;
	protected StreamListCallback streamListCallback;

	protected User user = null;
	protected Checkin[] checkins = null;

	public ShowCheckinsScreen(Facebook pfb) throws FacebookException {
		this(pfb, "me");
	}

	public ShowCheckinsScreen(Facebook pfb, String pUserId) throws FacebookException {
		this(pfb, pfb.getUser(pUserId.trim()));
	}

	public ShowCheckinsScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;

		listField = new ListField();

		user = pUser;
		if (user != null) {
			user.getCheckins(new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					checkins = (Checkin[]) objects;
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Checkins of " + user.getName(), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
							loadList();
						}
					});
				}

				public void onException(final Exception e, final java.lang.Object state) {
					e.printStackTrace();
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Error Encountered", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
						}
					});
				}

			}, null);
		}

		setTitle(new LabelField("Retrieving...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
		streamListCallback = new StreamListCallback();
		listField.setRowHeight(50);
		listField.setCallback(streamListCallback);
		add(listField);
	}

	protected void loadList() {
		while (listField.getSize() > 0) {
			listField.delete(0);
		}
		streamListCallback.clear();

		if (checkins != null) {
			for (int i = 0; i < checkins.length; i++) {
				listField.insert(listField.getSize());
				streamListCallback.add(checkins[i]);
			}
		}

	}

	public boolean onClose() {
		return super.onClose();
	}

	protected boolean onSavePrompt() {
		return true;
	}

	protected class StreamListCallback implements ListFieldCallback {

		protected Vector vCheckins = new Vector();

		public StreamListCallback() {
		}

		public void clear() {
			vCheckins.removeAllElements();
		}

		public void add(Checkin checkin) {
			vCheckins.addElement(checkin);
		}

		public void insert(Checkin checkin, int index) {
			vCheckins.insertElementAt(checkin, index);
		}

		public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
			if (index < vCheckins.size()) {
				int height = listField.getRowHeight();
				Checkin checkin = (Checkin) vCheckins.elementAt(index);

				String tagged = "Tagged: ";
				Profile[] taggedUsers = checkin.getTags();
				if ((taggedUsers != null) && (taggedUsers.length > 0)) {
					for (int i = 0; i < taggedUsers.length; i++) {
						tagged += taggedUsers[i].getName() + ", ";
					}
				}

				Page place = (Page) checkin.getPlace().toObject();
				String drawText = "Place: " + place.getName() + " (" + place.getLocation().getLatitude() + "/" + place.getLocation().getLongitude() + ")";

				Font font = Font.getDefault();
				font.derive(Font.BOLD);
				g.setFont(font);
				g.drawText(drawText, 2, y, 0, width - 2);
				if ((taggedUsers != null) && (taggedUsers.length > 0)) {
					g.drawText("(" + tagged + ")", 2, y + (height / 2), DrawStyle.ELLIPSIS, width - 2);
				} else {
					g.drawText(" ", 2, y + (height / 2), DrawStyle.ELLIPSIS, width - 2);
				}
				g.drawLine(0, y + height - 1, width, y + height - 1);
			}
		}

		public Object get(ListField listField, int index) {
			if (index < vCheckins.size()) {
				return vCheckins.elementAt(index);
			}
			return null;
		}

		public int getPreferredWidth(ListField listField) {
			return Display.getWidth();
		}

		public int indexOfList(ListField listField, String prefix, int start) {
			for (int i = start; i < vCheckins.size(); i++) {
				Checkin checkin = (Checkin) vCheckins.elementAt(i);

				if (checkin.getMessage().indexOf(prefix) > -1) {
					return i;
				}
			}

			return -1;
		}

	}

}