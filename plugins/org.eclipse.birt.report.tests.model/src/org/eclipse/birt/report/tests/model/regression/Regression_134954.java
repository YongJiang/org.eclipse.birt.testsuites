/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Library can't be removed.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a report "a.rptdesign"
 * <li>New a library which is not in the same fold with report, saying
 * "../test/lib.rptlibrary"
 * <li>Report includes the library
 * <li>Remove the library
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * The library is removed
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Open XMLSource, the included library structure is still there.
 * </p>
 * Test description:
 * <p>
 * Report include a library from a relative folder "lib/***", remove the library
 * from report, save and reopen the report, make sure the included library
 * structure is cleared.
 * </p>
 */

public class Regression_134954 extends BaseTestCase
{

	private final static String INPUT = "regression_134954.xml"; //$NON-NLS-1$
	private final static String OUTPUT = "regression_134954.out"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	
	public void test_134954( ) throws DesignFileException, SemanticException,
			IOException
	{
		openDesign( INPUT );
		LibraryHandle lib = designHandle.getLibrary( "regression_134954" ); //$NON-NLS-1$
		designHandle.dropLibrary( lib );

		// save the design and reopen it, make sure the included structure is
		// cleared.

		saveAs( OUTPUT );
		DesignEngine engine = new DesignEngine( new DesignConfig( ) );
		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH );
		ReportDesignHandle designHandle = session.openDesign( this
				.getClassFolder( )
				+ OUTPUT_FOLDER + OUTPUT );
		assertNull( designHandle
				.getListProperty( ReportDesignHandle.LIBRARIES_PROP ) );
	}
}
