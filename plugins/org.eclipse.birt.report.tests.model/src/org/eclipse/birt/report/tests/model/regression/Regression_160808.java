/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * getRelativePath error when base is root.
 * <p>
 * example: URIUtil.getRelativePath( "c:\\", "c:\\test.library" ) will return
 * "../test.library", it should return "test.library" or "./test.library". if
 * base is not root, the result is correct, for example,
 * URIUtil.getRelativePath( "c:\\a\\", "c:\\a\\test.library" ) return
 * "test.library".
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test the example in description
 * <p>
 */
public class Regression_160808 extends BaseTestCase
{

	public void test_regression_160808( )
	{
		assertEquals( "test.library", URIUtil.getRelativePath(
				"c:\\a\\",
				"c:\\a\\test.library" ) );
	}
}
