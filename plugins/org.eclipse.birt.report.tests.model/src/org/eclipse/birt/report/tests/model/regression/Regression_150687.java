/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.net.URL;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Model - Support including path to jar files in the report designs and library
 * <p>
 * Test description:
 * <p>
 * Include a "test.jar" from input folder, make sure it can be retrieved from
 * the default resource locator.
 * <p>
 */
public class Regression_150687 extends BaseTestCase
{

	/**
	 * @throws SemanticException
	 */
	public void test_regression_150687( ) throws SemanticException
	{
		DesignEngine engine = new DesignEngine( new DesignConfig( ) );
		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH );
		ReportDesignHandle designHandle = session.createDesign( );

		ScriptLib lib1 = StructureFactory.createScriptLib( );
		lib1.setName( "input/test.jar" ); //$NON-NLS-1$

		designHandle.addScriptLib( lib1 );

		session.setResourceFolder( this.getClassFolder( ) + INPUT_FOLDER );
		URL url = session.getResourceLocator( ).findResource( designHandle,
				"test.jar", IResourceLocator.JAR_FILE ); //$NON-NLS-1$
		assertNotNull( url );
	}
}
