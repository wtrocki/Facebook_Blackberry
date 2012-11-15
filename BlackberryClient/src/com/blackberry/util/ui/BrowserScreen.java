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
package com.blackberry.util.ui;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.browser.field2.BrowserFieldNavigationRequestHandler;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.BrowserFieldResourceRequestHandler;
import net.rim.device.api.browser.field2.ProtocolController;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.TransitionContext;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.w3c.dom.Document;

import com.blackberry.util.log.Logger;
import com.blackberry.util.string.StringUtils;

public class BrowserScreen extends MainScreen implements BrowserFieldNavigationRequestHandler, BrowserFieldResourceRequestHandler {

	protected Logger log = Logger.getLogger(getClass());

	protected String url;
	protected ConnectionFactory cf;
	protected String loadingText;

	protected BrowserFieldConfig bfc;
	protected BrowserField bf;
	protected MyBrowserFieldListener listener;

	protected VerticalFieldManager vfm;
	protected EvenlySpacedHorizontalFieldManager hfm1;
	protected EvenlySpacedHorizontalFieldManager hfm2;
	protected ProgressAnimationField spinner;

	public BrowserScreen(String pUrl) {
		this(pUrl, new ConnectionFactory(), "Loading");
	}

	public BrowserScreen(String pUrl, ConnectionFactory pcf, String pLoadingText) {
		super();
		url = pUrl;
		cf = pcf;
		loadingText = pLoadingText;

		bfc = new BrowserFieldConfig();
		bfc.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);
		bfc.setProperty(BrowserFieldConfig.JAVASCRIPT_ENABLED, Boolean.TRUE);
		bfc.setProperty(BrowserFieldConfig.USER_SCALABLE, Boolean.TRUE);
		bfc.setProperty(BrowserFieldConfig.MDS_TRANSCODING_ENABLED, Boolean.FALSE);
		bfc.setProperty(BrowserFieldConfig.NAVIGATION_MODE, BrowserFieldConfig.NAVIGATION_MODE_POINTER);
		bfc.setProperty(BrowserFieldConfig.VIEWPORT_WIDTH, new Integer(Display.getWidth()));
		bfc.setProperty(BrowserFieldConfig.CONNECTION_FACTORY, cf);

		bf = new BrowserField(bfc);

		listener = new MyBrowserFieldListener();
		bf.addListener(listener);

		((ProtocolController) bf.getController()).setNavigationRequestHandler("http", this);
		((ProtocolController) bf.getController()).setResourceRequestHandler("http", this);
		((ProtocolController) bf.getController()).setNavigationRequestHandler("https", this);
		((ProtocolController) bf.getController()).setResourceRequestHandler("https", this);

		// Init Screen
		attachTransition(TransitionContext.TRANSITION_FADE);
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));

		hfm1 = new EvenlySpacedHorizontalFieldManager(USE_ALL_WIDTH);
		hfm1.add(new LabelField("\n" + loadingText));

		hfm2 = new EvenlySpacedHorizontalFieldManager(USE_ALL_WIDTH);
		spinner = new ProgressAnimationField(Bitmap.getBitmapResource("spinner2.png"), 6, Field.FIELD_HCENTER);
		spinner.setMargin(15, 15, 15, 15);
		hfm2.add(spinner);

		vfm = new VerticalFieldManager(USE_ALL_WIDTH);
		vfm.add(hfm1);
		vfm.add(hfm2);

		showLoading();
		fetch();
	}

	protected void showContent() {
		try {
			if (vfm.getScreen() != null) {
				synchronized (net.rim.device.api.system.Application.getEventLock()) {
					delete(vfm);
				}
			}
			if (bf.getScreen() == null) {
				synchronized (net.rim.device.api.system.Application.getEventLock()) {
					add(bf);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			log.error("Throwable:  " + t.getMessage());
		}
	}

	protected void showLoading() {
		try {
			if (vfm.getScreen() == null) {
				synchronized (net.rim.device.api.system.Application.getEventLock()) {
					add(vfm);
				}
			}
			if (bf.getScreen() != null) {
				synchronized (net.rim.device.api.system.Application.getEventLock()) {
					delete(bf);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			log.error("Throwable:  " + t.getMessage());
		}
	}

	protected void fetch() {
		bf.requestContent(url);
	}

	protected boolean onSavePrompt() {
		return true;
	}

	protected void call() {
		if (!isDisplayed()) {
			synchronized (net.rim.device.api.system.Application.getEventLock()) {
				UiApplication.getUiApplication().pushScreen(this);
			}
		}
	}

	protected void dismiss() {
		if (isDisplayed()) {
			synchronized (net.rim.device.api.system.Application.getEventLock()) {
				UiApplication.getUiApplication().popScreen(this);
			}
		}
	}

	public void handleNavigation(BrowserFieldRequest request) throws Exception {
		log.info("BF-Navigate: " + request.getURL());
		if (shouldFetchContent(request)) {
			// showLoading();
			request.setURL(StringUtils.fixHttpsUrlPrefix(request.getURL()));
			bf.displayContent(handleResource(request), request.getURL());
		}
	}

	public InputConnection handleResource(BrowserFieldRequest request) throws Exception {
		InputConnection conn = bf.getConnectionManager().makeRequest(request);

		if ((conn != null) && (conn instanceof HttpConnection)) {
			processHttpConnection((HttpConnection) conn);
		} else if ((conn != null) && (conn instanceof HttpsConnection)) {
			processHttpsConnection((HttpsConnection) conn);
		}

		return conn;
	}

	protected boolean processHttpConnection(HttpConnection conn) {
		// waiting for subclasses to override.
		return true;
	}

	protected boolean processHttpsConnection(HttpsConnection conn) {
		// waiting for subclasses to override.
		return true;
	}

	protected boolean shouldFetchContent(BrowserFieldRequest request) {
		// waiting for subclasses to override.
		return true;
	}

	protected boolean shouldShowContent(BrowserField pbf, Document pdoc) {
		// waiting for subclasses to override.
		return true;
	}

	protected boolean postProcessing(BrowserField pbf, Document pdoc) {
		// waiting for subclasses to override.
		return true;
	}

	protected class MyBrowserFieldListener extends BrowserFieldListener {

		public void documentLoaded(BrowserField browserField, Document document) throws Exception {
			log.info("BF-DocumentLoaded(): " + document.getDocumentURI());
			if (shouldShowContent(browserField, document)) {
				showContent();
			}
			postProcessing(browserField, document);
		}

		public void documentAborted(BrowserField browserField, Document document) throws Exception {
			log.info("BF-DocumentAborted(): " + document.getDocumentURI());
		}

		public void documentCreated(BrowserField browserField, Document document) throws Exception {
			log.info("BF-DocumentCreated(): " + document.getDocumentURI());
		}

		public void documentError(BrowserField browserField, Document document) throws Exception {
			log.info("BF-DocumentError(): " + document.getDocumentURI());
		}

		public void documentUnloading(BrowserField browserField, Document document) throws Exception {
			log.info("BF-DocumentUnloading(): " + document.getDocumentURI());
		}

		public void downloadProgress(BrowserField browserField, Document document) throws Exception {
			log.info("BF-DownloadProgress(): " + document.getDocumentURI());
		}

	}

	protected void attachTransition(int transitionType) {
		UiEngineInstance engine = Ui.getUiEngineInstance();
		TransitionContext pushAction = null;
		TransitionContext popAction = null;

		switch (transitionType) {

		case TransitionContext.TRANSITION_FADE:
			pushAction = new TransitionContext(TransitionContext.TRANSITION_FADE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_FADE);
			pushAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			popAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			break;

		case TransitionContext.TRANSITION_NONE:
			pushAction = new TransitionContext(TransitionContext.TRANSITION_NONE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_NONE);
			break;

		case TransitionContext.TRANSITION_SLIDE:
			pushAction = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_SLIDE);
			pushAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			pushAction.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_LEFT);
			popAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			popAction.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_RIGHT);
			break;

		case TransitionContext.TRANSITION_WIPE:
			pushAction = new TransitionContext(TransitionContext.TRANSITION_WIPE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_WIPE);
			pushAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			pushAction.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_LEFT);
			popAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			popAction.setIntAttribute(TransitionContext.ATTR_DIRECTION, TransitionContext.DIRECTION_RIGHT);
			break;

		case TransitionContext.TRANSITION_ZOOM:
			pushAction = new TransitionContext(TransitionContext.TRANSITION_ZOOM);
			popAction = new TransitionContext(TransitionContext.TRANSITION_ZOOM);
			pushAction.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_IN);
			popAction.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_OUT);
			break;

		default:
			pushAction = new TransitionContext(TransitionContext.TRANSITION_NONE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_NONE);
		}

		engine.setTransition(null, this, UiEngineInstance.TRIGGER_PUSH, pushAction);
		engine.setTransition(this, null, UiEngineInstance.TRIGGER_POP, popAction);
	}

}