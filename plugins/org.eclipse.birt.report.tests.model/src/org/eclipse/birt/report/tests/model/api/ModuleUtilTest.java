package org.eclipse.birt.report.tests.model.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

public class ModuleUtilTest extends BaseTestCase {
	private final String reportName = "ModuleUtilTest_report.xml"; //$NON-NLS-1$
	private final String libraryName = "ModuleUtilTest_report.xml"; //$NON-NLS-1$
	private final String invalidreportName = "ModuleUtilTest_report_invalid.xml"; //$NON-NLS-1$
	private final String invalidlibraryName = "ModuleUtilTest_report_invalid.xml"; //$NON-NLS-1$

		public ModuleUtilTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(ModuleUtilTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
			
	}
	public void testReportValidation() throws Exception 
	{
		//test a valid report design
		
		openDesign( reportName );
		File file = new File(PLUGIN_PATH +getClassFolder( ) + INPUT_FOLDER + reportName );
		InputStream is = new FileInputStream( file );
		SessionHandle session = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = session.openDesign( file.toString() );
		assertTrue(ModuleUtil.isValidDesign(session,reportName,is));
		
		//test a invalid report design
		File file2 = new File(PLUGIN_PATH +getClassFolder( ) + INPUT_FOLDER + invalidreportName );
		InputStream is2 = new FileInputStream( file2 );
		SessionHandle session2 = DesignEngine.newSession( ULocale.ENGLISH );
		assertFalse(ModuleUtil.isValidDesign(session2,invalidreportName,is2));
		
	}	
	public void testLibraryValidation() throws Exception 
	{
		//test a valid library 
		openDesign( libraryName );
		File file = new File(PLUGIN_PATH +getClassFolder( ) + INPUT_FOLDER + libraryName );
		InputStream is = new FileInputStream( file );
		SessionHandle session = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = session.openDesign(file.toString());
		assertTrue(ModuleUtil.isValidDesign(session,libraryName,is));
		
		//test a invalid library
		File file2 = new File(PLUGIN_PATH +getClassFolder( ) + INPUT_FOLDER + invalidlibraryName );
		InputStream is2 = new FileInputStream( file2 );
		SessionHandle session2 = DesignEngine.newSession( ULocale.ENGLISH );
		assertFalse(ModuleUtil.isValidDesign(session2,invalidlibraryName,is2));
	}		
}
