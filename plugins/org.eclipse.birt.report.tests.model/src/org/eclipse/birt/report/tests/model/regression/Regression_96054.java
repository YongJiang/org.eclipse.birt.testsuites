/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Cannot open tutorial report customers.rptdesign
 * </p>
 * Test description:
 * <p>
 * Open the customers report, ensure that there won't be any design file
 * exception.
 * </p>
 */
public class Regression_96054 extends BaseTestCase
{

	private final static String INPUT = "regression_96054_customers.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */
	
	public void setup( ) throws Exception
	{
		super.setUp();
		copyResource_INPUT( INPUT, INPUT );
		System.out.println(INPUT);
	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	public void test_regression_96054( ) throws DesignFileException
	{
		openDesign( INPUT );
		assertNotNull( designHandle );
	}
}
