/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Chart can't be converted to a template report item
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>New a chart
 * <li>Right click the chart
 * <li>Create Template Report Item
 * </ol>
 */
public class Regression_164436 extends BaseTestCase
{

	private final static String REPORT = "regression_164436.xml";

	public void test_regression_164436( ) throws Exception
	{
		openDesign( REPORT );

		// find the chart
		ExtendedItemHandle chart = (ExtendedItemHandle) designHandle
				.findElement( "Chart1" );
		assertNotNull( chart );

		// create chart to template Report Item
		TemplateElementHandle chartTemp = chart
				.createTemplateElement( "Temp_Chart" );
		assertNotNull( chartTemp );

	}
}
