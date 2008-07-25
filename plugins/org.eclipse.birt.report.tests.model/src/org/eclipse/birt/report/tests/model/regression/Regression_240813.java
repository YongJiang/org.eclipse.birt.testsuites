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
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * Refresh Library reset overridden values.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li> create a library with a grid (1x1) and place a label inside. (define a text
on that label).
 * <li> create a report using this library.
 * <li> drag the grid twice into this report.
 * <li> override the text of the two labels.
 * <li> hit "refresh library"
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Check two overridden values are kept.
 * </p>
 */
public class Regression_240813 extends BaseTestCase
{

	private final static String REPORT = "regression_240813.xml";
	private final static String LIBRARY = "regression_240813_lib.xml";
	private final static String GOLDEN = "regression_240813_golden.xml";
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		copyInputToFile ( INPUT_FOLDER + "/" + REPORT );
		copyInputToFile ( INPUT_FOLDER + "/" + LIBRARY );
		copyGoldenToFile(GOLDEN_FOLDER+ "/" + GOLDEN);
	}
	/**
	 * @throws Exception 
	 * 
	 */
	public void test_regression_240813( ) throws Exception
	{
		openDesign( REPORT );
		LabelHandle labelHandle=(LabelHandle)designHandle.findElement("NewLabel");
		labelHandle.setText("label");
		LabelHandle labelHandle1=(LabelHandle)designHandle.findElement("NewLabel1");
		labelHandle1.setText("label1");
		
		designHandle.reloadLibraries();
		
		String fileName=genOutputFile(REPORT);
		designHandle.saveAs(fileName);
		
		assertTrue(compareTextFile(GOLDEN, REPORT));
	}
}
