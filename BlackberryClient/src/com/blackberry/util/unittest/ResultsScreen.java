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
package com.blackberry.util.unittest;

import com.blackberry.util.unittest.jmunit.*;

import java.util.Vector;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * 
 * @author Primer
 */
public class ResultsScreen extends MainScreen {

	int numberTestsPass = 0;
	int numberTestsFail = 0;
	UnitTestLogic utl = new UnitTestLogic();

	public ResultsScreen(UnitTests[] tests) {
		deleteAll();
		setTitle("UnitTest Results");

		if ((tests != null) && (tests.length > 0)) {
			for (int i = 0; i < tests.length; i++) {
				try {
					tests[i].runTests();
				} catch (AssertionFailedException e) {
					e.printStackTrace();
				}
			}
		}

		add(new ColourLabelField("Failed Tests:", LabelField.USE_ALL_WIDTH));

		Vector reportsVector = utl.getInfoVector();
		int numberReports = reportsVector.size();

		if (reportsVector.size() < 1) {
			add(new LabelField("All Clear"));
		} else {
			for (int i = 0; i < numberReports; i++) {
				String[] reportArray = (String[]) reportsVector.elementAt(i);
				add(new SeparatorField());
				add(new ColourLabelField(reportArray[0], LabelField.USE_ALL_WIDTH, 0xefefef, 0x333333));
				add(new LabelField(reportArray[1]));
				add(new NullField());
			}
		}
		add(new ColourLabelField("Tests Overview:", LabelField.USE_ALL_WIDTH));
		int numberofResults = utl.getNumberofResults();
		for (int i = 0; i < numberofResults; i++) {
			String[] resultArray = utl.getResultArray(i);
			if (resultArray[2].equals("pass")) {
				add(new TestResultField(true, resultArray[0], resultArray[1]));
				numberTestsPass++;
			} else if (resultArray[2].equals("fail")) {
				add(new TestResultField(false, resultArray[0], resultArray[1]));
				numberTestsFail++;
			} else {
				add(new ColourLabelField(resultArray[0], LabelField.USE_ALL_WIDTH, 0x666666, 0xffffff));
			}

		}
		setTitle("Fail: " + numberTestsFail + " Pass: " + numberTestsPass + " Total: " + (numberTestsFail + numberTestsPass) + " tests");
	}

	class ColourLabelField extends LabelField {

		private int fontColour = 0xffffff;
		private int backgroundColour = 0xff00cc;

		public ColourLabelField(String text, long style) {
			super(text, style);
		}

		public ColourLabelField(String text, long style, int backgroundColour, int fontColour) {
			super(text, style);
			this.backgroundColour = backgroundColour;
			this.fontColour = fontColour;
		}

		protected void paint(Graphics graphics) {
			graphics.setColor(backgroundColour);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.setColor(fontColour);
			super.paint(graphics);
		}
	}
}
