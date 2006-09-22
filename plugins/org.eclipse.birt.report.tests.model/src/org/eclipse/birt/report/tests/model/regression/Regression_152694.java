/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * NPE when converting an autotext to template report item; We do not need to
 * support making autotext as a template report item, Model will return false in
 * method canTransformToTemplate() to disable the action.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Make sure canTransformToTemplate() on AutoTextHandle will return false;
 * <p>
 */

public class Regression_152694 extends BaseTestCase
{

	private final static String REPORT = "regression_152694.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_152694( ) throws DesignFileException
	{
		openDesign( REPORT );
		SimpleMasterPageHandle page = (SimpleMasterPageHandle) designHandle
				.getMasterPages( ).get( 0 );
		AutoTextHandle pageNo = (AutoTextHandle) page.getPageHeader( ).get( 0 );
		assertFalse( pageNo.canTransformToTemplate( ) );
	}
}
