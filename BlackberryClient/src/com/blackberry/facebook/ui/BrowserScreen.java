/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook.ui;

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

import com.blackberry.facebook.util.StringUtils;

public class BrowserScreen extends MainScreen implements
		BrowserFieldNavigationRequestHandler,
		BrowserFieldResourceRequestHandler {
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
		bfc.setProperty(BrowserFieldConfig.NAVIGATION_MODE,
				BrowserFieldConfig.NAVIGATION_MODE_POINTER);
		bfc.setProperty(BrowserFieldConfig.VIEWPORT_WIDTH,
				new Integer(Display.getWidth()));
		bfc.setProperty(BrowserFieldConfig.CONNECTION_FACTORY, cf);

		bf = new BrowserField(bfc);

		listener = new MyBrowserFieldListener();
		bf.addListener(listener);

		((ProtocolController) bf.getController()).setNavigationRequestHandler(
				"http", this);
		((ProtocolController) bf.getController()).setResourceRequestHandler(
				"http", this);
		((ProtocolController) bf.getController()).setNavigationRequestHandler(
				"https", this);
		((ProtocolController) bf.getController()).setResourceRequestHandler(
				"https", this);

		// Init Screen
		attachTransition(TransitionContext.TRANSITION_FADE);
		getMainManager().setBackground(
				BackgroundFactory.createSolidBackground(Color.WHITE));

		hfm1 = new EvenlySpacedHorizontalFieldManager(USE_ALL_WIDTH);
		hfm1.add(new LabelField("\n" + loadingText));

		hfm2 = new EvenlySpacedHorizontalFieldManager(USE_ALL_WIDTH);
		spinner = new ProgressAnimationField(
				Bitmap.getBitmapResource("spinner.png"), 6, Field.FIELD_HCENTER);
		spinner.setMargin(15, 15, 15, 15);
		hfm2.add(spinner);

		vfm = new VerticalFieldManager(USE_ALL_WIDTH | FIELD_VCENTER);
		vfm.add(hfm1);
		vfm.add(hfm2);

		showLoading();
		fetch();
	}

	protected void showContent() {
		try {
			if (vfm.getScreen() != null) {
				synchronized (net.rim.device.api.system.Application
						.getEventLock()) {
					delete(vfm);
				}
			}
			if (bf.getScreen() == null) {
				synchronized (net.rim.device.api.system.Application
						.getEventLock()) {
					add(bf);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void showLoading() {
		try {
			if (vfm.getScreen() == null) {
				synchronized (net.rim.device.api.system.Application
						.getEventLock()) {
					add(vfm);
				}
			}
			if (bf.getScreen() != null) {
				synchronized (net.rim.device.api.system.Application
						.getEventLock()) {
					delete(bf);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
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
		if (shouldFetchContent(request)) {
			// showLoading();
			request.setURL(StringUtils.fixHttpsUrlPrefix(request.getURL()));
			bf.displayContent(handleResource(request), request.getURL());
		}
	}

	public InputConnection handleResource(BrowserFieldRequest request)
			throws Exception {
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

		public void documentLoaded(BrowserField browserField, Document document)
				throws Exception {
			if (shouldShowContent(browserField, document)) {
				showContent();
			}
			postProcessing(browserField, document);
		}
	}

	protected void attachTransition(int transitionType) {
		UiEngineInstance engine = Ui.getUiEngineInstance();
		TransitionContext pushAction = null;
		TransitionContext popAction = null;

		switch (transitionType) {

		case TransitionContext.TRANSITION_FADE:
			pushAction = new TransitionContext(
					TransitionContext.TRANSITION_FADE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_FADE);
			pushAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			popAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			break;

		case TransitionContext.TRANSITION_NONE:
			pushAction = new TransitionContext(
					TransitionContext.TRANSITION_NONE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_NONE);
			break;

		case TransitionContext.TRANSITION_SLIDE:
			pushAction = new TransitionContext(
					TransitionContext.TRANSITION_SLIDE);
			popAction = new TransitionContext(
					TransitionContext.TRANSITION_SLIDE);
			pushAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			pushAction.setIntAttribute(TransitionContext.ATTR_DIRECTION,
					TransitionContext.DIRECTION_LEFT);
			popAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			popAction.setIntAttribute(TransitionContext.ATTR_DIRECTION,
					TransitionContext.DIRECTION_RIGHT);
			break;

		case TransitionContext.TRANSITION_WIPE:
			pushAction = new TransitionContext(
					TransitionContext.TRANSITION_WIPE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_WIPE);
			pushAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			pushAction.setIntAttribute(TransitionContext.ATTR_DIRECTION,
					TransitionContext.DIRECTION_LEFT);
			popAction.setIntAttribute(TransitionContext.ATTR_DURATION, 300);
			popAction.setIntAttribute(TransitionContext.ATTR_DIRECTION,
					TransitionContext.DIRECTION_RIGHT);
			break;

		case TransitionContext.TRANSITION_ZOOM:
			pushAction = new TransitionContext(
					TransitionContext.TRANSITION_ZOOM);
			popAction = new TransitionContext(TransitionContext.TRANSITION_ZOOM);
			pushAction.setIntAttribute(TransitionContext.ATTR_KIND,
					TransitionContext.KIND_IN);
			popAction.setIntAttribute(TransitionContext.ATTR_KIND,
					TransitionContext.KIND_OUT);
			break;

		default:
			pushAction = new TransitionContext(
					TransitionContext.TRANSITION_NONE);
			popAction = new TransitionContext(TransitionContext.TRANSITION_NONE);
		}

		engine.setTransition(null, this, UiEngineInstance.TRIGGER_PUSH,
				pushAction);
		engine.setTransition(this, null, UiEngineInstance.TRIGGER_POP,
				popAction);
	}

}