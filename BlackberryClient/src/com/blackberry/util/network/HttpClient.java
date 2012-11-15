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
package com.blackberry.util.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.PostData;
import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;

public class HttpClient {

	protected ConnectionFactory cf;

	protected static final String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";
	protected static final String START_BOUNDARY = "--" + BOUNDARY + "\r\n";
	protected static final String END_BOUNDARY = "\r\n--" + BOUNDARY + "--\r\n";

	public HttpClient(ConnectionFactory pcf) {
		cf = pcf;
	}

	public StringBuffer doGet(String url, Hashtable args) throws Exception {
		StringBuffer urlBuffer = new StringBuffer(url);

		if ((args != null) && (args.size() > 0)) {
			urlBuffer.append('?');
			Enumeration keysEnum = args.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				String val = (String) args.get(key);
				urlBuffer.append(key).append('=').append(val);

				if (keysEnum.hasMoreElements()) {
					urlBuffer.append('&');
				}
			}
		}

		return doGet(urlBuffer.toString());
	}

	public StringBuffer doGet(String url) throws Exception {
		HttpConnection conn = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (cf == null)) {
				return null;
			}
			ConnectionDescriptor connd = cf.getConnection(url);
			String transportTypeName = TransportInfo.getTransportTypeName(connd
					.getTransportDescriptor().getTransportType());
			conn = (HttpConnection) connd.getConnection();
			int resCode = conn.getResponseCode();
			String resMessage = conn.getResponseMessage();
			switch (resCode) {
			case HttpConnection.HTTP_OK: {
				InputStream inputStream = conn.openInputStream();
				int c;
				while ((c = inputStream.read()) != -1) {
					buffer.append((char) c);
				}
				inputStream.close();
				break;
			}

			case HttpConnection.HTTP_BAD_REQUEST: {
				InputStream inputStream = conn.openInputStream();
				int c;
				while ((c = inputStream.read()) != -1) {
					buffer.append((char) c);
				}
				inputStream.close();
				break;
			}
			case HttpConnection.HTTP_TEMP_REDIRECT:
			case HttpConnection.HTTP_MOVED_TEMP:
			case HttpConnection.HTTP_MOVED_PERM: {
				url = conn.getHeaderField("Location");
				buffer = doGet(url);
				break;
			}

			default:
				break;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
				}
			}
		}
		return buffer;
	}

	public StringBuffer doPost(String url, Hashtable data) throws Exception {
		URLEncodedPostData encoder = new URLEncodedPostData("UTF-8", false);
		Enumeration keysEnum = data.keys();

		while (keysEnum.hasMoreElements()) {
			String key = (String) keysEnum.nextElement();
			String val = (String) data.get(key);
			encoder.append(key, val);
		}

		return doPost(url, encoder);
	}

	public StringBuffer doPost(String url, PostData postData) throws Exception {
		HttpConnection conn = null;
		OutputStream os = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (cf == null)) {
				return null;
			}

			ConnectionDescriptor connd = cf.getConnection(url);
			String transportTypeName = TransportInfo.getTransportTypeName(connd
					.getTransportDescriptor().getTransportType());
			conn = (HttpConnection) connd.getConnection();

			if (conn != null) {
				try {
					// post data
					if (postData != null) {
						conn.setRequestMethod(HttpConnection.POST);
						conn.setRequestProperty(
								HttpProtocolConstants.HEADER_CONTENT_TYPE,
								postData.getContentType());
						conn.setRequestProperty(
								HttpProtocolConstants.HEADER_CONTENT_LENGTH,
								String.valueOf(postData.size()));

						os = conn.openOutputStream();
						os.write(postData.getBytes());

					} else {
						conn.setRequestMethod(HttpConnection.GET);
					}

				} catch (Throwable t) {
					t.printStackTrace();
				}

				int resCode = conn.getResponseCode();
				String resMessage = conn.getResponseMessage();

				switch (resCode) {

				case HttpConnection.HTTP_OK: {
					InputStream inputStream = conn.openInputStream();
					int c;

					while ((c = inputStream.read()) != -1) {
						buffer.append((char) c);
					}

					inputStream.close();
					break;
				}

				case HttpConnection.HTTP_BAD_REQUEST: {
					InputStream inputStream = conn.openInputStream();
					int c;

					while ((c = inputStream.read()) != -1) {
						buffer.append((char) c);
					}

					inputStream.close();
					break;
				}

				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM: {
					url = conn.getHeaderField("Location");
					buffer = doPost(url, postData);
					break;
				}

				default:
					break;
				}
			}
			if ((conn.getType() != null)
					&& conn.getType().trim().startsWith("text/javascript")) {
			}

		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
				}
			}
		}

		return buffer;
	}

	public StringBuffer doPostMultipart(String url, Hashtable params,
			String name, String fileName, String fileType, byte[] payload)
			throws Exception {
		HttpConnection conn = null;
		OutputStream os = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (cf == null)) {
				return null;
			}

			ConnectionDescriptor connd = cf.getConnection(url);
			String transportTypeName = TransportInfo.getTransportTypeName(connd
					.getTransportDescriptor().getTransportType());
			conn = (HttpConnection) connd.getConnection();

			if (conn != null) {
				try {
					byte[] postBytes = getMultipartPostBytes(params, name,
							fileName, fileType, payload);
					conn.setRequestMethod(HttpConnection.POST);
					conn.setRequestProperty(
							HttpProtocolConstants.HEADER_CONTENT_TYPE,
							HttpProtocolConstants.CONTENT_TYPE_MULTIPART_FORM_DATA
									+ "; boundary=" + BOUNDARY);
					conn.setRequestProperty(
							HttpProtocolConstants.HEADER_CONTENT_LENGTH,
							postBytes + "");

					os = conn.openOutputStream();
					os.write(postBytes);

				} catch (Throwable t) {
					t.printStackTrace();
				}

				int resCode = conn.getResponseCode();
				String resMessage = conn.getResponseMessage();

				switch (resCode) {

				case HttpConnection.HTTP_OK: {
					InputStream inputStream = conn.openInputStream();
					int c;

					while ((c = inputStream.read()) != -1) {
						buffer.append((char) c);
					}

					inputStream.close();
					break;
				}

				case HttpConnection.HTTP_BAD_REQUEST: {
					InputStream inputStream = conn.openInputStream();
					int c;

					while ((c = inputStream.read()) != -1) {
						buffer.append((char) c);
					}

					inputStream.close();
					break;
				}

				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM: {
					url = conn.getHeaderField("Location");
					buffer = doPostMultipart(url, params, name, fileName,
							fileType, payload);
					break;
				}

				default:
					break;
				}
			}
			if ((conn.getType() != null)
					&& conn.getType().trim().startsWith("text/javascript")) {
			}

		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
				}
			}
		}

		return buffer;
	}

	protected byte[] getMultipartPostBytes(Hashtable params, String name,
			String fileName, String fileType, byte[] payload) {
		StringBuffer res = new StringBuffer(START_BOUNDARY);

		Enumeration keys = params.keys();

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) params.get(key);
			res.append("Content-Disposition: form-data; name=\"").append(key)
					.append("\"\r\n").append("\r\n").append(value)
					.append("\r\n").append("--").append(BOUNDARY)
					.append("\r\n");
		}

		res.append("Content-Disposition: form-data; name=\"").append(name)
				.append("\"; filename=\"").append(fileName).append("\"\r\n")
				.append("Content-Type: ").append(fileType).append("\r\n\r\n");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(res.toString().getBytes());
			baos.write(payload);
			baos.write(END_BOUNDARY.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return baos.toByteArray();
	}

}
