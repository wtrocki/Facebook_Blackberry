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

/**
 * The Test is a abstract class that has the main
 * implementation to create a executing test class
 * or a utility class to execute others. The
 * MIDlet methods as startApp are localized here.
 *
 * @author Brunno Silva
 * @since JMUnit 1.0
 */
public abstract class Test extends AdvancedAssertion{   
    /**
     * The default constructor.
     * It creates a screen instance with the name passed as
     * paramenter. If the total of tests isn't lower than zero,
     * the amount is increased in the Result class.
     *
     * @throws IllegalArgumentException when the total of tests is negative.
     * @param name the name of the executing class.
     * @param totalOfTests the amount of test methods that the subclass has.
     * @since JMUnit 1.0
     */
    public Test(int totalOfTests, String name){
        super();       
        if(totalOfTests < 0){
            throw new IllegalArgumentException();
        }
        Result.addTotalOfTests(totalOfTests);
    }

    
    /**
     * This abstract method is used to execute the tests.
     * Ever sub-class must create a code to let it
     * execute it's tests.
     *
     * @since JMUnit 1.0
     */
    public abstract void test();
}