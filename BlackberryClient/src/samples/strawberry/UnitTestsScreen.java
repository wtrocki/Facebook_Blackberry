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

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import samples.strawberry.tests.CompleteUnitTests;

import com.blackberry.facebook.Facebook;
import com.blackberry.util.unittest.ResultsScreen;
import com.blackberry.util.unittest.UnitTests;

public class UnitTestsScreen extends MainScreen {

	protected EditField idEditField;
	protected EditField pwdEditField;
	protected ButtonField goButtonField;

	public UnitTestsScreen(final Facebook pfb) {
		super();
		setTitle(new LabelField("Unit Tests", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		idEditField = new EditField("Email or Phone: ", "");
		add(idEditField);

		pwdEditField = new EditField("Password: ", "");
		add(pwdEditField);

		goButtonField = new ButtonField("Perform Tests") {
			protected boolean invokeAction(int action) {
				UnitTests[] tests = new UnitTests[1];
				tests[0] = new CompleteUnitTests(pfb, idEditField.getText(), pwdEditField.getText());
				UiApplication.getUiApplication().pushScreen(new ResultsScreen(tests));
				return true;
			}
		};
		add(goButtonField);

		add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));
		add(new LabelField("Steps:\n\n1. Remove this app from your FB a/c.\n2. Enter id/pwd above.\n3. Click the button to start.\n\nNote: You will be required to login several times to complete the unit tests."));

	}

	protected boolean onSavePrompt() {
		return true;
	}

}
