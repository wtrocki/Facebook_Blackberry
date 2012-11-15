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

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.FlowFieldManager;
import net.rim.device.api.ui.container.MainScreen;

import com.blackberry.facebook.BasicAsyncCallback;
import com.blackberry.facebook.Facebook;
import com.blackberry.facebook.inf.Album;
import com.blackberry.facebook.inf.User;

public class UploadPhotosScreen extends MainScreen {

	protected Facebook fb;
	protected User currentUser;

	protected Album[] albums;
	protected String[] albumNames;
	protected ObjectChoiceField albumsCF;

	protected EditField messageEditField;
	protected ButtonField postButtonField;
	protected ButtonField postButtonField2;

	protected FlowFieldManager ffm1;

	public UploadPhotosScreen(Facebook pfb, User pUser) {
		super();
		fb = pfb;
		currentUser = pUser;

		setTitle(new LabelField("Retrieving...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		albums = null;
		albumNames = null;
		albumsCF = new ObjectChoiceField();
		albumsCF.setLabel("Upload to album: ");
		add(albumsCF);

		messageEditField = new EditField("Message: ", "Uploaded by StrawBerry.");
		add(messageEditField);

		postButtonField = new ButtonField("Snap & Upload") {
			protected boolean invokeAction(int action) {
				ffm1.deleteAll();
				UiApplication.getUiApplication().pushScreen(new SnapshotAPIScreen());
				return true;
			}
		};
		add(postButtonField);
		postButtonField2 = new ButtonField("Upload Sample Image") {
			protected boolean invokeAction(int action) {
				ffm1.deleteAll();
				uploadPhoto(EncodedImage.getEncodedImageResource("sample.jpg").getData(), "image/jpeg");
				return true;
			}
		};
		add(postButtonField2);
		add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));

		ffm1 = new FlowFieldManager(FlowFieldManager.VERTICAL_SCROLL);
		add(ffm1);
		loadAlbums();
	}

	protected boolean onSavePrompt() {
		return true;
	}

	protected void uploadPhoto(byte[] imageBytes, String mime) {
		if ((imageBytes != null) && (imageBytes.length > 0)) {
			synchronized (Application.getEventLock()) {
				ffm1.add(new BitmapField(Bitmap.createBitmapFromBytes(imageBytes, 0, imageBytes.length, 4), Field.FOCUSABLE));
			}
			String result = null;
			if (albumsCF.getSelectedIndex() == 0) {
				result = currentUser.publishPhoto(EncodedImage.createEncodedImage(imageBytes, 0, imageBytes.length, mime), messageEditField.getText());
			} else {
				result = albums[albumsCF.getSelectedIndex()].publishPhoto(EncodedImage.createEncodedImage(imageBytes, 0, imageBytes.length), messageEditField.getText());
			}
			if ((result != null) && !result.trim().equals("")) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.inform("Upload Success.");
					}
				});
			} else {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.inform("Upload Failed.");
					}
				});
			}
		}
	}

	void loadAlbums() {
		try {
			currentUser.getAlbums(new BasicAsyncCallback() {

				public void onComplete(com.blackberry.facebook.inf.Object[] objects, final java.lang.Object state) {
					Album[] myAlbums = (Album[]) objects;
					if ((myAlbums == null) || (myAlbums.length <= 0)) {
						albums = new Album[1];
						albumNames = new String[1];
					} else {
						albums = new Album[myAlbums.length + 1];
						albumNames = new String[myAlbums.length + 1];
					}

					albums[0] = null;
					albumNames[0] = "Default";

					for (int i = 1; i < (myAlbums.length + 1); i++) {
						albums[i] = myAlbums[i - 1];
						albumNames[i] = myAlbums[i - 1].getName();
					}
					UiApplication.getApplication().invokeLater(new Runnable() {
						public void run() {
							setTitle(new LabelField("Upload Photos to " + currentUser.getName(), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
							albumsCF.setChoices(albumNames);
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

		} catch (Exception e) {
		}
	}

	class SnapshotAPIScreen extends MainScreen {

		private VideoControl vc;
		private Player p;
		private Field viewFinder;

		public SnapshotAPIScreen() {
			super();
			setTitle("Taking Snapshot...");
			initViewFinder();
			new SnapThread().start();
		}

		protected boolean onSavePrompt() {
			return true;
		}

		private void initViewFinder() {
			try {
				p = Manager.createPlayer("capture://video");
				p.realize();
				p.prefetch();
				p.start();
				vc = (VideoControl) p.getControl("VideoControl");

				if (vc != null) {
					viewFinder = (Field) vc.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
					vc.setDisplayFullScreen(true);
					vc.setVisible(true);

					add(viewFinder);
					viewFinder.setFocus();
				}

			} catch (Exception me) {
			}
		}

		private byte[] snap() {
			byte[] result = null;
			try {
				if ((vc != null)) {
					byte[] imageBytes = null;
					while ((imageBytes == null) || (imageBytes.length == 0)) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						imageBytes = vc.getSnapshot(getEncoding());
						vc.setDisplayFullScreen(true);
						vc.setVisible(true);
					}
					result = imageBytes;
				}

			} catch (Throwable t) {
				t.printStackTrace();
			}
			return result;
		}

		private class SnapThread extends Thread {
			public void run() {
				byte[] imageBytes = snap();
				synchronized (Application.getEventLock()) {
					close();
				}
				if ((imageBytes != null) && (imageBytes.length > 0)) {
					setTitle(new LabelField("Uploading...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
					uploadPhoto(imageBytes, "image/jpeg");
					setTitle(new LabelField("Upload Photos to " + currentUser.getName(), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));
				}
			}
		}

		private String getEncoding() {
			if (Display.getHeight() >= 480) {
				return "encoding=jpeg&width=640&height=480&quality=normal";
			} else {
				return "encoding=jpeg&width=480&height=360&quality=normal";
			}
		}
	}

}
