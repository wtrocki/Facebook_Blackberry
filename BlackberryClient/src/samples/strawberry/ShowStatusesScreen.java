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

import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.inf.Profile;
import com.blackberry.facebook.inf.Status;
import com.blackberry.facebook.inf.User;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

public class ShowStatusesScreen extends MainScreen {

	protected Facebook fb;
	protected EditField msgField = null;
	protected ButtonField publishButton = null;
	protected ListField listField;
	protected StreamListCallback streamListCallback;

	protected User user = null;
	protected Status[] statuses = null;

	public ShowStatusesScreen(Facebook pfb) throws FacebookException {
		this(pfb, "me");
	}

	public ShowStatusesScreen(Facebook pfb, String pUserId) throws FacebookException {
		this(pfb, pfb.getUser(pUserId));
	}

	public ShowStatusesScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;

		msgField = new EditField("New Status:  ", "");

		publishButton = new ButtonField("Publish") {
			protected boolean invokeAction(int action) {
				if ((msgField.getText() != null) && !msgField.getText().trim().equals("")) {
					publishNewStatus(msgField.getText().trim());
				} else {
					Dialog.alert("Please enter the new status.");
				}
				return true;
			}
		};

		listField = new ListField();

		user = pUser;
		if (user != null) {
			user.getStatuses(new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					statuses = (Status[]) objects;
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Statuses of " + user.getName(), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
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
		add(msgField);
		add(publishButton);
		add(new SeparatorField());
		add(listField);
	}

	protected void loadList() {
		while (listField.getSize() > 0) {
			listField.delete(0);
		}
		streamListCallback.clear();

		if (statuses != null) {
			for (int i = 0; i < statuses.length; i++) {
				listField.insert(listField.getSize());
				streamListCallback.add(statuses[i]);
			}
		}

	}

	protected void publishNewStatus(String pMessage) {
		try {
			User user = fb.getCurrentUser();
			String result = user.publishStatus(pMessage);
			if ((result != null) && !result.trim().equals("")) {
				Dialog.inform("Publish Success.");
			} else {
				Dialog.inform("Publish Failed.");
			}

		} catch (FacebookException e) {
			e.printStackTrace();
		} finally {
		}
	}

	public boolean onClose() {
		return super.onClose();
	}

	protected boolean onSavePrompt() {
		return true;
	}

	protected class StreamListCallback implements ListFieldCallback {

		protected Vector vStatuses = new Vector();

		public StreamListCallback() {
		}

		public void clear() {
			vStatuses.removeAllElements();
		}

		public void add(Status statuses) {
			vStatuses.addElement(statuses);
		}

		public void insert(Status status, int index) {
			vStatuses.insertElementAt(status, index);
		}

		public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
			if (index < vStatuses.size()) {
				int height = listField.getRowHeight();
				Status status = (Status) vStatuses.elementAt(index);
				String likedBy = "Liked by: ";
				Profile[] likedByUsers = status.getLikedUsers();
				if ((likedByUsers != null) && (likedByUsers.length > 0)) {
					for (int i = 0; i < likedByUsers.length; i++) {
						likedBy += likedByUsers[i].getName() + ", ";
					}
				}

				Font font = Font.getDefault();
				font.derive(Font.BOLD);
				g.setFont(font);
				g.drawText(status.getMessage(), 2, y, 0, width - 2);
				if ((likedByUsers != null) && (likedByUsers.length > 0)) {
					g.drawText("(" + likedBy + ")", 2, y + (height / 2), DrawStyle.ELLIPSIS, width - 2);
				} else {
					g.drawText(" ", 2, y + (height / 2), DrawStyle.ELLIPSIS, width - 2);
				}
				g.drawLine(0, y + height - 1, width, y + height - 1);
			}
		}

		public Object get(ListField listField, int index) {
			if (index < vStatuses.size()) {
				return vStatuses.elementAt(index);
			}
			return null;
		}

		public int getPreferredWidth(ListField listField) {
			return Display.getWidth();
		}

		public int indexOfList(ListField listField, String prefix, int start) {
			for (int i = start; i < vStatuses.size(); i++) {
				Status status = (Status) vStatuses.elementAt(i);

				if (status.getMessage().indexOf(prefix) > -1) {
					return i;
				}
			}

			return -1;
		}

	}

}