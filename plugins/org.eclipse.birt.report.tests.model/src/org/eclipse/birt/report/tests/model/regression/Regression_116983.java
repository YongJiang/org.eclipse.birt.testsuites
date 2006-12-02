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
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Exception is thrown out when deleting a template item then save it.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a template file.
 * <li>Add a label in it and transfer it to a template item.
 * <li>Delete this template item and save the template file.
 * <li>Exception is thrown out.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Follow the steps and save the template.
 * </p>
 */
public class Regression_116983 extends BaseTestCase
{

	/**
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void test_regression_116983( ) throws SemanticException, IOException
	{
		SessionHandle sessionHandle = new DesignEngine( new DesignConfig( ) )
				.newSessionHandle( ULocale.ENGLISH );
		ReportDesignHandle template = sessionHandle.createDesign( );

		ElementFactory factory = template.getElementFactory( );
		LabelHandle label = factory.newLabel( "label" ); //$NON-NLS-1$

		template.getBody( ).add( label );

		// transfer to template item.

		template
				.findElement( "label" ).createTemplateElement( "templateLabel" );  //$NON-NLS-1$//$NON-NLS-2$
		
		// drop it
		
		template.findElement( "templateLabel" ).drop( ); //$NON-NLS-1$
		
		
		
		// save the template
		
		//cannot create BaseTestCases.makeOutputDir
		//makeOutputDir( );
		template.saveAs( getClassFolder( ) + OUTPUT_FOLDER + "regression_116983_template.out" ); //$NON-NLS-1$
	}
}
