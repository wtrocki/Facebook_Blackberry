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
package com.blackberry.util.date;

import java.util.Date;

import net.rim.device.api.io.http.HttpDateParser;

public final class DateUtils {

	public static final String FACEBOOK_LONG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String FACEBOOK_LONG_DATE_FORMAT_WITHOUT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String FACEBOOK_SHORT_DATE_FORMAT = "MM/dd/yyyy";
	public static final String FACEBOOK_MONTH_YEAR_DATE_FORMAT = "yyyy-MM";

	private DateUtils() {
	}

	//	public static Date toDateFromLongFormat(String date) {
	//		Date parsedDate = toDateWithFormatString(date, FACEBOOK_LONG_DATE_FORMAT);
	//		if (parsedDate == null) {
	//			parsedDate = toDateWithFormatString(date, FACEBOOK_LONG_DATE_FORMAT_WITHOUT_TIMEZONE);
	//		}
	//		return parsedDate;
	//	}
	//
	//	public static Date toDateFromShortFormat(String date) {
	//		return toDateWithFormatString(date, FACEBOOK_SHORT_DATE_FORMAT);
	//	}
	//
	//	public static Date toDateFromMonthYearFormat(String date) {
	//		if ("0000-00".equals(date)) {
	//			return null;
	//		}
	//		return toDateWithFormatString(date, FACEBOOK_MONTH_YEAR_DATE_FORMAT);
	//	}

	//	private static Date toDateWithFormatString(String date, String format) {
	//		if (date == null) {
	//			return null;
	//		}
	//		try {
	//			return new SimpleDateFormat(format).parse(date);
	//		} catch (Exception e) {
	//			return null;
	//		}
	//	}

	public static Date parse(String date) {
		if (date == null) {
			return null;
		}
		return new Date(HttpDateParser.parse(date));
	}
}