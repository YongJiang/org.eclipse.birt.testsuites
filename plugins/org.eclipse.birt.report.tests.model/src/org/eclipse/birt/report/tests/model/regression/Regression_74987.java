/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Drag a table into a grid cell. Set row style for this grid. Row style didn't
 * take effect on table
 * </p>
 * Test description:
 * <p>
 * Set style for grid row, check table style
 * </p>
 */

public class Regression_74987 extends BaseTestCase
{

	private String filename = "Regression_74987.xml";

	public void test( ) throws DesignFileException, StyleException,
			ContentException, NameException
	{
		openDesign( filename );
		GridHandle grid = (GridHandle) designHandle.findElement( "Grid" );
		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"Table" );

		RowHandle row = (RowHandle) grid.getRows( ).get( 0 );
		CellHandle cell = (CellHandle) row.getCells( ).get( 0 );
		cell.addElement( table, 0 );

		row.setStyleName( "Style" );
		assertEquals( "red", table.getProperty( Style.COLOR_PROP ) );
	}
}
