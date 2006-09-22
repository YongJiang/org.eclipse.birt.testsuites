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
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * Can't delete group name in outline view
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Add a sample db and a dataset with table "CUSTOMERS"
 * <li>Add a table binding with the data set
 * <li>Add a group"ID" on the table, group on row["CUSTOMERNUMBER"]
 * <li>Switch to outline view, choose the table group, delete the group name
 * </ol>
 * 
 * <b>Expected result:</b>
 * <p>
 * The table group name can be deleted in outline view
 * <p>
 * <b>Actual result:</b>
 * <p>
 * The table group name can't be deleted in outline view
 * </p>
 * Test description:
 * <p>
 * Follow the steps, set table group name to null, ensure that group name is
 * cleared
 * </p>
 */
public class Regression_122879 extends BaseTestCase
{

	private final static String INPUT = "regression_122879.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws NameException
	 */
	public void test_regression_122879( ) throws DesignFileException, NameException
	{
		openDesign( INPUT );
		TableHandle table = (TableHandle) designHandle.findElement( "table1" ); //$NON-NLS-1$
		TableGroupHandle group1 = (TableGroupHandle) table.getGroups( ).get( 0 );

		group1.setName( null );
		assertEquals( null, group1.getQualifiedName( ) );
		assertEquals( null, group1.getName( ) );
		
	}
}
