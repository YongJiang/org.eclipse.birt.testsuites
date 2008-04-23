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
import java.io.InputStream;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.tests.model.BaseTestCase;


/**
 * Regression description:
 * </p>
 * [regression]Fail to setCurrentView to chart in table onPrepare method.[1302]
 * </p>
 * Test description:
 * <p>
 * </p>
 */
public class Regression_225252 extends BaseTestCase
{
	private final static String REPORT = "regression_225252.xml";

	protected void setUp( ) throws Exception
	{
//		super.setUp( );
		removeResource( );
		copyInputToFile(INPUT_FOLDER + "/"+ REPORT);
		copyGoldenToFile(GOLDEN_FOLDER+ "/"+ REPORT);
	}
	
	public void tearDown( )
	{
		removeResource( );
	}
	

	/**
	 * @throws Exception 
	 * 
	 */
	
	public void test_regression_225252( ) throws Exception
	{
		DesignEngine engine=new DesignEngine(new DesignConfig());
		InputStream is=getResource( INPUT_FOLDER + "/"+ REPORT ).openStream( );
		IReportDesign report=engine.openDesign(INPUT_FOLDER + "/" +REPORT, is, null );
		
		ITable table=report.getTable( "mytable" );
		try{
			table.setCurrentView( report.getReportElement( "NewChart" ) );
		}catch(Exception e){
			fail();
		}
		File f=new File(getTempFolder( )+"/"+OUTPUT_FOLDER );
		if(!f.exists( )) f.mkdirs( );
		report.saveAs( getTempFolder( )+"/"+OUTPUT_FOLDER + "/"+ REPORT);
		is.close( );
		
		assertTrue(compareFile( REPORT, REPORT ));
		
	}
}
