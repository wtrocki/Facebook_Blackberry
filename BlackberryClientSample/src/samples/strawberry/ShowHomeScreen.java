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
import com.blackberry.facebook.inf.Post;
import com.blackberry.facebook.inf.User;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

public class ShowHomeScreen extends MainScreen {

	protected Facebook fb;
	protected ListField listField;
	protected StreamListCallback streamListCallback;

	protected User user = null;
	protected Post[] posts = null;

	public ShowHomeScreen(Facebook pfb) throws FacebookException {
		this(pfb, "me");
	}

	public ShowHomeScreen(Facebook pfb, String pUserId) throws FacebookException {
		this(pfb, pfb.getUser(pUserId));
	}

	public ShowHomeScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;
		listField = new ListField();
		user = pUser;
		if (user != null) {
			user.getHome(new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					posts = (Post[]) objects;
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Home of " + user.getName(), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
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

		if (posts != null) {
			for (int i = 0; i < posts.length; i++) {
				listField.insert(listField.getSize());
				streamListCallback.add(posts[i]);
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

		protected Vector vPosts = new Vector();

		public StreamListCallback() {
		}

		public void clear() {
			vPosts.removeAllElements();
		}

		public void add(Post post) {
			vPosts.addElement(post);
		}

		public void insert(Post post, int index) {
			vPosts.insertElementAt(post, index);
		}

		public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
			if (index < vPosts.size()) {
				int height = listField.getRowHeight();
				Post post = (Post) vPosts.elementAt(index);
				String likedBy = post.getLikes() + " people like this.";

				String drawText = "";
				if (post.getType().trim().equalsIgnoreCase(Facebook.PostTypes.LINK)) {
					drawText = "Link: " + post.getName();
				} else if (post.getType().trim().equalsIgnoreCase(Facebook.PostTypes.PHOTO)) {
					drawText = "Photo: " + post.getName();
				} else if (post.getType().trim().equalsIgnoreCase(Facebook.PostTypes.STATUS)) {
					drawText = "Status: " + post.getMessage();
				} else if (post.getType().trim().equalsIgnoreCase(Facebook.PostTypes.SWF)) {
					drawText = "Swf: " + post.getMessage();
				} else if (post.getType().trim().equalsIgnoreCase(Facebook.PostTypes.VIDEO)) {
					drawText = "Video: " + post.getName();
				} else {
					drawText = "Unknown: " + post.getName();
				}

				Font font = Font.getDefault();
				font.derive(Font.BOLD);
				g.setFont(font);
				g.drawText(drawText, 2, y, 0, width - 2);
				g.drawText("(" + likedBy + ")", 2, y + (height / 2), DrawStyle.ELLIPSIS, width - 2);
				g.drawLine(0, y + height - 1, width, y + height - 1);
			}
		}

		public Object get(ListField listField, int index) {
			if (index < vPosts.size()) {
				return vPosts.elementAt(index);
			}
			return null;
		}

		public int getPreferredWidth(ListField listField) {
			return Display.getWidth();
		}

		public int indexOfList(ListField listField, String prefix, int start) {
			for (int i = start; i < vPosts.size(); i++) {
				Post post = (Post) vPosts.elementAt(i);

				if (post.getMessage().indexOf(prefix) > -1) {
					return i;
				}
			}

			return -1;
		}

	}

}