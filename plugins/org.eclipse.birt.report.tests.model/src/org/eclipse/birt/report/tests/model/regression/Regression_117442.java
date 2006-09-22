/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * Exception is thrown out when exporting the whole report design.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li> Create a design file.
 * <li> Select in outline view and export it to a library file.
 * <li> Set its name and click on "OK".
 * <li> Exception is thrown out.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Prepare a report that contains most report elements and data connections and
 * parameter, export it to a library. Make sure there won't be exception.
 * </p>
 */
public class Regression_117442 extends BaseTestCase
{

	private final static String INPUT = "regression_117442.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws IOException
	 * @throws SemanticException
	 * 
	 */
	public void test_regression_117442( ) throws DesignFileException, SemanticException,
			IOException
	{
		openDesign( INPUT );
		String output = this.getClassFolder( ) + OUTPUT_FOLDER
				+ "regression_117442.out"; //$NON-NLS-1$
		ElementExportUtil.exportDesign( designHandle, output, true, true ); 
		assertTrue( new File( output ).exists( ) );
	}
}
