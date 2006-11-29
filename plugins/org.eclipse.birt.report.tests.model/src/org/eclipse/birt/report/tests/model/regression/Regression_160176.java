/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b>
 * <p>
 * Create design by a template
 * <p>
 * we should create a report design from a template, model told me that we can
 * use ReportDesignHandle createDesign( String templateName ), but in fact it
 * doesn't work at all.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test method createDesignFromTemplate in DesignSession
 * <p>
 */
public class Regression_160176 extends BaseTestCase
{

	private final static String REPORT = "regression_160176.rpttemplate"; //$NON-NLS-1$

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( REPORT , REPORT );
	}
	
	public void tearDown( )
	{
		removeResource( );
	}
	/**
	 * @throws DesignFileException
	 */

	public void test_regression_160176( ) throws DesignFileException
	{
		DesignEngine engine = new DesignEngine( new DesignConfig( ) );
		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH );
		ReportDesignHandle designHandle = session
				.createDesignFromTemplate( this.getFullQualifiedClassName( ) + "/"
						+ INPUT_FOLDER + "/" + REPORT );
		assertNotNull( designHandle );
	}
}
