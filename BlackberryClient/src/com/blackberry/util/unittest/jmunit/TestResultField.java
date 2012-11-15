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
package com.blackberry.util.unittest.jmunit;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

/**
 *
 * @author Primer
 */
public class TestResultField extends Field implements DrawStyle {

    private Font fieldFont = Font.getDefault();
    private int fieldWidth = Display.getWidth();
    private int fieldHeight = fieldFont.getHeight() + 8;
    private Bitmap passBitmap = Bitmap.getBitmapResource("pass.png");
    private Bitmap failBitmap = Bitmap.getBitmapResource("fail.png");
    private boolean testPassed;
    private String methodName;
    private String testName;
    private boolean isActive = false;

    public TestResultField(boolean testPassed, String methodName, String testName) {
        super(Field.FOCUSABLE);
        this.testPassed = testPassed;
        this.methodName = methodName;
        this.testName = testName;
    }

    protected void layout(int width, int height) {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public int getPreferredWidth() {
        return fieldWidth;
    }

    public int getPreferredHeight() {
        return fieldHeight;
    }

    protected void paint(Graphics graphics) {
        if (isActive) {
            graphics.setColor(0xdddddd);
            graphics.fillRect(0, 0, fieldWidth, fieldHeight);
        }
        if (testPassed) {
            graphics.drawBitmap(2, (fieldHeight - passBitmap.getHeight()) / 2, passBitmap.getWidth(), passBitmap.getHeight(), passBitmap, 0, 0);
        } else {
            graphics.drawBitmap(2, (fieldHeight - failBitmap.getHeight()) / 2, failBitmap.getWidth(), failBitmap.getHeight(), failBitmap, 0, 0);
        }

        graphics.setColor(0x333333);
        graphics.drawText(methodName, 4 + failBitmap.getWidth(), (fieldHeight - fieldFont.getHeight()) / 2, DrawStyle.ELLIPSIS, fieldWidth - (fieldFont.getAdvance(testName) + 6 + failBitmap.getWidth()));
        graphics.setColor(0x888888);
        graphics.drawText(testName, fieldWidth - (fieldFont.getAdvance(testName) + 2), (fieldHeight - fieldFont.getHeight()) / 2);
        graphics.drawLine(0, fieldHeight - 1, fieldWidth, fieldHeight - 1);
    }

    protected void onFocus(int direction) {
        isActive = true;
        invalidate();
        super.onFocus(direction);
    }

    protected void onUnfocus() {
        isActive = false;
        invalidate();
        super.onUnfocus();
    }

    //Override to prevent native focus drawing.
    protected void drawFocus(Graphics graphics, boolean on) {
        //super.drawFocus(graphics, on);
    }
}
