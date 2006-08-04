/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * It is allowed to edit data set defined in library thru report design, model
 * should provide check method as canEdit() on designElementHandle.
 * </p>
 * Test description:
 * <p>
 * Report include a library that contains data set, retrive the data set from
 * report, ensure that:
 * <p>
 * 1. data set from library can not be edited.
 * <p>
 * 2. data set from report itself can be edited
 * </p>
 */
public class Regression_116425 extends BaseTestCase
{

	private final static String INPUT = "regression_116425.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */
	public void test_116425( ) throws DesignFileException
	{
		openDesign( INPUT );
		List datasets = designHandle.getAllDataSets( );

		assertEquals( 3, datasets.size( ) );

		// ds in the design.

		DataSetHandle ds1 = (DataSetHandle) datasets.get( 0 );
		assertEquals( "Data Set", ds1.getQualifiedName( ) ); //$NON-NLS-1$
		assertTrue( ds1.canEdit( ) );

		// ds in the library.

		DataSetHandle ds2 = (DataSetHandle) datasets.get( 1 );
		assertEquals( "regression_116425_lib.Data Set", ds2.getQualifiedName( ) ); //$NON-NLS-1$
		assertFalse( ds2.canEdit( ) );

		DataSetHandle ds3 = (DataSetHandle) datasets.get( 2 );
		assertEquals( "regression_116425_lib.Data Set1", ds3.getQualifiedName( ) ); //$NON-NLS-1$
		assertFalse( ds3.canEdit( ) );
	}
}
