/*******************************************************************************
 * BB Facebook Simple client
 *******************************************************************************/
package com.blackberry.facebook;

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
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.w3c.dom.Document;

public class BrowserScreen extends MainScreen implements
		BrowserFieldNavigationRequestHandler,
		BrowserFieldResourceRequestHandler {

	protected String url;
	protected ConnectionFactory cf;

	protected BrowserFieldConfig bfc;
	protected BrowserField bf;
	protected MyBrowserFieldListener listener;

	public BrowserScreen(String pUrl) {
		this(pUrl, new ConnectionFactory());
	}

	public BrowserScreen(String pUrl, ConnectionFactory pcf) {
		super();
		url = pUrl;
		cf = pcf;

		bfc = new BrowserFieldConfig();
		bfc.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);
		bfc.setProperty(BrowserFieldConfig.JAVASCRIPT_ENABLED, Boolean.TRUE);
		bfc.setProperty(BrowserFieldConfig.USER_SCALABLE, Boolean.TRUE);
		bfc.setProperty(BrowserFieldConfig.NAVIGATION_MODE,
				BrowserFieldConfig.NAVIGATION_MODE_POINTER);
		bfc.setProperty(BrowserFieldConfig.VIEWPORT_WIDTH,
				new Integer(Display.getWidth()));
		bfc.setProperty(BrowserFieldConfig.CONNECTION_FACTORY, cf);
		// COOKIES DISABLED (USER MUST PROVIDE CREDENTIALS ON EVERY REQUEST)
		bfc.setProperty(BrowserFieldConfig.ENABLE_COOKIES, Boolean.FALSE);

		bf = new BrowserField(bfc);

		listener = new MyBrowserFieldListener();
		bf.addListener(listener);

		initProtocolsControllers();

		// Init Screen
		getMainManager().setBackground(
				BackgroundFactory.createSolidBackground(Color.WHITE));
		showLoading();
		fetch();
	}

	private void initProtocolsControllers() {
		((ProtocolController) bf.getController()).setNavigationRequestHandler(
				"http", this);
		((ProtocolController) bf.getController()).setResourceRequestHandler(
				"http", this);
		((ProtocolController) bf.getController()).setNavigationRequestHandler(
				"https", this);
		((ProtocolController) bf.getController()).setResourceRequestHandler(
				"https", this);
	}

	protected void showContent() {
		if (bf.getScreen() == null) {
			synchronized (net.rim.device.api.system.Application.getEventLock()) {
				add(bf);
			}
		}
	}

	protected void showLoading() {
		if (bf.getScreen() != null) {
			synchronized (net.rim.device.api.system.Application.getEventLock()) {
				delete(bf);
			}
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
			request.setURL(fixHttpsUrlPrefix(request.getURL()));
			bf.displayContent(handleResource(request), request.getURL());
		}
	}

	public static String fixHttpsUrlPrefix(String url) {
		String result = "";
		if ((url == null) || url.trim().equals("")) {
			result = url;
		} else {
			if (url.startsWith("http://") && (url.indexOf(":443") != -1)) {
				// fix it
				result = "https://" + url.substring(7);
			} else {
				// return the original url for all other cases
				result = url;
			}
		}
		return result;
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
}