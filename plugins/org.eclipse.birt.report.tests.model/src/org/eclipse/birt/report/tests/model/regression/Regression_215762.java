/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class Regression_215762 extends BaseTestCase
{

	private final static String REPORT = "regression_215762.rptdesign";
	private final static String LIBRARY = "regression_215762.rptlibrary";
	private final static String GOLDEN = "regression_215762_golden.rptdesign";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyInputToFile( INPUT_FOLDER + "/" + REPORT );
		copyInputToFile( INPUT_FOLDER + "/" + LIBRARY );
		copyGoldenToFile( GOLDEN_FOLDER + "/" + GOLDEN );
	}

	public void tearDown( )
	{
		// removeResource( );
	}

	public void test_regression_215762( ) throws Exception
	{
		openDesign( REPORT );
		os = new ByteArrayOutputStream( );
		DocumentUtil.serialize( designHandle, os );
		File f=new File(getTempFolder( )+"/"+OUTPUT_FOLDER,REPORT);
		
		InputStream filein  =   new  FileInputStream( f);
		OutputStream out=new FileOutputStream(f);
		out.write( os.toByteArray( ));
		this.compareFile( GOLDEN, REPORT );
	}
}
