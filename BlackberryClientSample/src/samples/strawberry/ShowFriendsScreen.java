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

import java.util.Hashtable;
import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.User;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

public class ShowFriendsScreen extends MainScreen {

	protected Facebook fb;
	protected ListField listField;
	protected StreamListCallback streamListCallback;

	protected User user = null;
	protected Profile[] friends = null;

	public ShowFriendsScreen(Facebook pfb) throws FacebookException {
		this(pfb, "me");
	}

	public ShowFriendsScreen(Facebook pfb, String pUserId) throws FacebookException {
		this(pfb, pfb.getUser(pUserId));
	}

	public ShowFriendsScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;

		listField = new ListField() {
			protected boolean navigationClick(int status, int time) {
				synchronized (Application.getEventLock()) {
					UiApplication.getUiApplication().pushScreen(new ShowUserScreen(fb, friends[getSelectedIndex()].getId()));
				}
				return true;
			}

		};

		user = pUser;
		if (user != null) {
			user.getFriends(new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					friends = (Profile[]) objects;
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Friends of " + user.getName(), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
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

		if (friends != null) {
			for (int i = 0; i < friends.length; i++) {
				listField.insert(listField.getSize());
				streamListCallback.add(friends[i]);
			}
			streamListCallback.loadBitmaps();
		}

	}

	public boolean onClose() {
		streamListCallback.stopLoading();
		return super.onClose();
	}

	protected boolean onSavePrompt() {
		return true;
	}

	protected class StreamListCallback implements ListFieldCallback {

		protected Hashtable pictureBitmaps = new Hashtable();
		protected boolean runThread = false;

		public StreamListCallback() {
		}

		public void clear() {
		}

		public void add(Profile user) {
		}

		public void insert(Profile user, int index) {
		}

		public void loadBitmaps() {
			startLoading();
			(new BitmapThread()).start();
		}

		public void startLoading() {
			runThread = true;
		}

		public void stopLoading() {
			runThread = false;
		}

		public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
			if (index < friends.length) {
				int height = listField.getRowHeight();
				Profile user = friends[index];
				Bitmap bitmap = getBitmap(user.getId());

				if (bitmap != null) {
					g.drawBitmap(0, y + ((height - Math.min(bitmap.getHeight(), height)) / 2), 50, height, bitmap, 0, 0);
				}

				Font font = Font.getDefault();
				font.derive(Font.BOLD);
				g.setFont(font);
				g.drawText(user.getName(), 52, y, 0, width - 52);
				g.drawText(" ", 52, y + (height / 2), DrawStyle.ELLIPSIS, width - 52);
				g.drawLine(0, y + height - 1, width, y + height - 1);
			}
		}

		public Object get(ListField listField, int index) {
			if (index < friends.length) {
				return friends[index];
			}

			return null;
		}

		public int getPreferredWidth(ListField listField) {
			return Display.getWidth();
		}

		public int indexOfList(ListField listField, String prefix, int start) {
			for (int i = start; i < friends.length; i++) {
				if (friends[i].getName().indexOf(prefix) > -1) {
					return i;
				}
			}
			return -1;
		}

		protected Bitmap getBitmap(String id) {
			return (Bitmap) pictureBitmaps.get(id);
		}

		protected class BitmapThread extends Thread {
			public void run() {
				for (int i = 0; i < friends.length; i++) {
					if (runThread) {
						final User user = (User) friends[i].toObject();

						user.getPicture(Facebook.PictureTypes.SQUARE, new BasicAsyncCallback() {

							public void onComplete(final Bitmap[] values, final java.lang.Object state) {
								UiApplication.getApplication().invokeLater(new Runnable() {
									public void run() {
										pictureBitmaps.put(user.getId(), values[0]);
										listField.invalidate(((Integer) state).intValue());
									}
								});
							}

							public void onException(final Exception e, final java.lang.Object state) {
								e.printStackTrace();
							}

						}, new Integer(i));
					}
				}
			}
		}

	}

}