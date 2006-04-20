/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class RunAndRenderTaskTest extends EngineCase
{

	/**
	 * Test methods in RunAndRenderTask class
	 */
	public void testRunAndRenderTask( ) throws EngineException
	{

		String input = getClassFolder( )
				+ System.getProperty( "file.separator" ) + INPUT_FOLDER
				+ System.getProperty( "file.separator" )
				+ "report_engine.rptdesign";

		try
		{
			IReportRunnable runnable = engine
					.openReportDesign( new FileInputStream( new File( input ) ) );
			IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );
			// validateParameters
			assertTrue( task.validateParameters( ) );
			// set/getRenderOption
			RenderOptionBase option = new RenderOptionBase( ), optionGet;
			task.setRenderOption( option );
			optionGet = (RenderOptionBase) task.getRenderOption( );
			assertEquals( "set/getRenderOption fail", option, optionGet );

			// parameters
			HashMap hm = new HashMap( ), hmGet;
			task.setParameterValues( hm );
			hmGet = task.getParameterValues( );
			assertEquals( "set/getParameterValues(hashmap) fail", hm, hmGet );

			task.setParameterValue( "p1", "p1value" );
			assertEquals( "Set/getParameterValues fail", task
					.getParameterValues( ).get( "p1" ), "p1value" );
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace( );
		}
	}

	/*
	 * Cancel RunAndRenderTask in another thread with cancel method.
	 */
	public void testCancelRunAndRenderTask( )
	{
		String input = getClassFolder( )
				+ System.getProperty( "file.separator" ) + INPUT_FOLDER
				+ System.getProperty( "file.separator" ) + "pages9.rptdesign";
		long bTime,eTime,timeSpan1,timeSpan2;
		try
		{
			IReportRunnable runnable = engine
					.openReportDesign( new FileInputStream( new File( input ) ) );
			IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );
			HTMLRenderOption option=new HTMLRenderOption();
			option.setOutputFormat( "html" );
			task.setRenderOption( option );
			task.setAppContext( new HashMap() );
			
			CancelTask cancelThread = new CancelTask( "cancelThread", task );
			cancelThread.start( );
			bTime=System.currentTimeMillis( );
			task.run( );
			eTime=System.currentTimeMillis( );
			task.close( );
			timeSpan1=eTime-bTime;

			task=engine.createRunAndRenderTask( runnable );
			task.setRenderOption( option );
			task.setAppContext( new HashMap() );
			bTime=System.currentTimeMillis( );
			task.run( );
			eTime=System.currentTimeMillis( );
			task.close( );
			timeSpan2=eTime-bTime;

			assertTrue("RunAndRenderTask.cancel() failed!",timeSpan2>timeSpan1);
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail("RunAndRenderTask.cancel() failed!");
		}

	}

	/*
	 * A new thread to cancel existed runTask
	 */
	private class CancelTask extends Thread
	{

		private IRunAndRenderTask runTask;

		public CancelTask( String threadName, IRunAndRenderTask task )
		{
			super( threadName );
			runTask = task;
		}

		public void run( )
		{
			try
			{
				Thread.currentThread( ).sleep( 100 );
				runTask.cancel( );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				fail( "RunAndRenderTask.cancel() failed!" );
			}
		}

	}

}
