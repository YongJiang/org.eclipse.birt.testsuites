/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportRunner;

/**
 * Base class for Engine test.
 * 
 */

public abstract class EngineCase extends TestCase
{

	private String caseName;

	protected static final String BUNDLE_NAME = "org.eclipse.birt.report.tests.engine.messages";//$NON-NLS-1$

	protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle( BUNDLE_NAME );

	protected static final String PLUGIN_NAME = "org.eclipse.birt.report.tests.engine"; //$NON-NLS-1$
	protected static final String PLUGINLOC = "/org.eclipse.birt.report.tests.engine/"; //$NON-NLS-1$

	protected static final String PLUGIN_PATH = System.getProperty( "user.dir" ) //$NON-NLS-1$
			+ "/plugins/" + PLUGINLOC.substring( PLUGINLOC.indexOf( "/" ) + 1 ) //$NON-NLS-1$//$NON-NLS-2$
			+ "bin/"; //$NON-NLS-1$

	protected static final String TEST_FOLDER = "src/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "output"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden"; //$NON-NLS-1$

	protected IReportEngine engine = null;

	private static final String FORMAT_HTML = "html"; //$NON-NLS-1$
	private static final String ENCODING_UTF8 = "UTF-8"; //$NON-NLS-1$
	private static final String IMAGE_DIR = "image"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		// IPlatformContext context = new PlatformFileContext( );
		// config.setEngineContext( context );
		// this.engine = new ReportEngine( config );

		EngineConfig config = new EngineConfig( );
		this.engine = createReportEngine( config );
	}

	/**
	 * Create a report engine instance.
	 */
	public IReportEngine createReportEngine( EngineConfig config )
	{
		if ( config == null )
		{
			config = new EngineConfig( );
		}

		Platform.initialize( new PlatformFileContext( ) );
		// assume we has in the platform
		Object factory = Platform
				.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		if ( factory instanceof IReportEngineFactory )
		{
			return ( (IReportEngineFactory) factory )
					.createReportEngine( config );
		}
		return null;
	}

	/**
	 * Constructor.
	 */

	public EngineCase( )
	{
		super( null );
	}

	/**
	 * Constructor for DemoCase.
	 * 
	 * @param name
	 */
	public EngineCase( String name )
	{
		super( name );
	}

	protected void setCase( String caseName )
	{
		// set the case and emitter manager accroding to caseName.
		this.caseName = caseName;
	}

	protected void runCase( String args[] )
	{
		Vector runArgs = new Vector( );
		// invoke the report runner.
		String input = PLUGIN_PATH + System.getProperty( "file.separator" ) //$NON-NLS-1$
				+ RESOURCE_BUNDLE.getString( "CASE_INPUT" ); //$NON-NLS-1$
		input += System.getProperty( "file.separator" ) + caseName //$NON-NLS-1$
				+ ".rptdesign"; //$NON-NLS-1$
		System.out.println( "input is : " + input ); //$NON-NLS-1$

		// run report runner.

		if ( args != null )
		{
			for ( int i = 0; i < args.length; i++ )
			{
				runArgs.add( args[i] );
			}
		}
		runArgs.add( "-f" ); //$NON-NLS-1$
		runArgs.add( "test" ); //$NON-NLS-1$
		runArgs.add( input );

		args = (String[]) runArgs.toArray( new String[runArgs.size( )] );
		ReportRunner.main( args );
	}

	/**
	 * Make a copy of a given file to the target file.
	 * 
	 * @param from
	 *            the file where to copy from
	 * @param to
	 *            the target file to copy to.
	 * @throws IOException
	 */

	protected final void copyFile( String from, String to ) throws IOException
	{

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try
		{
			new File( to ).createNewFile( );

			bis = new BufferedInputStream( new FileInputStream( from ) );
			bos = new BufferedOutputStream( new FileOutputStream( to ) );

			int nextByte = 0;
			while ( ( nextByte = bis.read( ) ) != -1 )
			{
				bos.write( nextByte );
			}
		}
		catch ( IOException e )
		{
			throw e;
		}
		finally
		{
			try
			{
				if ( bis != null )
					bis.close( );

				if ( bos != null )
					bos.close( );
			}
			catch ( IOException e )
			{
				// ignore
			}

		}
	}

	public void removeFile( File file )
	{
		if ( file.isDirectory( ) )
		{
			File[] children = file.listFiles( );
			for ( int i = 0; i < children.length; i++ )
			{
				removeFile( children[i] );
			}
		}
		if ( file.exists( ) )
		{
			if ( !file.delete( ) )
			{
				System.out.println( file.toString( ) + " can't be removed" );
			}
		}
	}

	public void removeFile( String file )
	{
		removeFile( new File( file ) );
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

/*	protected String getClassFolder( )
	{
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = TEST_FOLDER + className.replace( '.', '/' );

		return className;
	}*/

	protected String getClassFolder( )
	{
		String pathBase = null;

		ProtectionDomain domain = this.getClass( ).getProtectionDomain( );
		if ( domain != null )
		{
			CodeSource source = domain.getCodeSource( );
			if ( source != null )
			{
				URL url = source.getLocation( );
				pathBase = url.getPath( );

				if ( pathBase.endsWith( "bin/" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 4 );
				if ( pathBase.endsWith( "bin" ) ) //$NON-NLS-1$
					pathBase = pathBase.substring( 0, pathBase.length( ) - 3 );
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		className = pathBase + className.replace( '.', '/' );

		return className;
	}
	
	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param golden
	 *            the 1st file name to be compared.
	 * @param output
	 *            the 2nd file name to be compared.
	 * @return true if two text files are same line by line
	 * @throws Exception
	 *             if any exception.
	 */

	protected boolean compareHTML( String golden, String output )
			throws Exception
	{
		FileReader readerA = null;
		FileReader readerB = null;
		boolean same = true;
		StringBuffer errorText = new StringBuffer( );

		try
		{
			golden = getClassFolder( ) + "/" + GOLDEN_FOLDER + "/" + golden; //$NON-NLS-1$//$NON-NLS-2$
			output = getClassFolder( ) + "/" + OUTPUT_FOLDER + "/" + output; //$NON-NLS-1$//$NON-NLS-2$

			readerA = new FileReader( golden );
			readerB = new FileReader( output );

			same = compareTextFile( readerA, readerB, output );
		}
		catch ( IOException e )
		{
			errorText.append( e.toString( ) );
			errorText.append( "\n" ); //$NON-NLS-1$
			e.printStackTrace( );
		}
		finally
		{
			try
			{
				readerA.close( );
				readerB.close( );
			}
			catch ( Exception e )
			{
				readerA = null;
				readerB = null;

				errorText.append( e.toString( ) );

				throw new Exception( errorText.toString( ) );
			}
		}

		return same;
	}

	/**
	 * Run and render the given design file into html file. If the input is
	 * "a.xml", output html file will be named "a.html" under folder "output".
	 * 
	 * @param input
	 * @throws EngineException
	 */

	protected void runAndRender_HTML( String input, String output )
			throws EngineException
	{
		runAndRender_HTML( input, output, null );
	}

	/**
	 * RunAndRender a report with the given parameters.
	 */

	protected final void runAndRender_HTML( String input, String output,
			Map paramValues ) throws EngineException
	{
		String outputFile = this.getClassFolder( ) + "/" + OUTPUT_FOLDER //$NON-NLS-1$
				+ "/" + output; //$NON-NLS-1$
		String inputFile = this.getClassFolder( )
				+ "/" + INPUT_FOLDER + "/" + input; //$NON-NLS-1$ //$NON-NLS-2$

		IReportRunnable runnable = engine.openReportDesign( inputFile );
		IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );

		if ( paramValues != null )
		{
			Iterator keys = paramValues.keySet( ).iterator( );
			while ( keys.hasNext( ) )
			{
				String key = (String) keys.next( );
				task.setParameterValue( key, paramValues.get( key ) );
			}
		}

		task.setLocale( Locale.ENGLISH );

		IRenderOption options = new HTMLRenderOption( );
		options.setOutputFileName( outputFile );

		HTMLRenderContext renderContext = new HTMLRenderContext( );
		renderContext.setImageDirectory( IMAGE_DIR );
		HashMap appContext = new HashMap( );
		appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext );
		task.setAppContext( appContext );

		options.setOutputFormat( FORMAT_HTML );
		options.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
				ENCODING_UTF8 );

		task.setRenderOption( options );
		task.run( );
		task.close( );
	}

	/**
	 * Run a report, generate a self-contained report document.
	 * 
	 * @throws EngineException
	 */

	protected final void run( String input, String output )
			throws EngineException
	{
		String outputFile = this.getClassFolder( ) + "/" + OUTPUT_FOLDER //$NON-NLS-1$
				+ "/" + output; //$NON-NLS-1$
		String inputFile = this.getClassFolder( )
				+ "/" + INPUT_FOLDER + "/" + input; //$NON-NLS-1$ //$NON-NLS-2$

		IReportRunnable runnable = engine.openReportDesign( inputFile );
		IRunTask task = engine.createRunTask( runnable );
		task.setAppContext( new HashMap( ) );
		IDocArchiveWriter archive = null;
		try
		{
			archive = new FileArchiveWriter( outputFile );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}

		task.run( archive );
		task.close( );
	}

	/**
	 * 
	 * @param doc
	 *            input rpt docuement file
	 * @param output
	 *            output file of the generation.
	 * @param pageRange
	 *            The pages to render, use "All" to render all, use 1-N to
	 *            render a selected page.
	 * @throws EngineException
	 */

	protected void render_HTML( String doc, String output, String pageRange )
			throws EngineException
	{
		render( "html", doc, output, pageRange ); //$NON-NLS-1$
	}

	/**
	 * Render a report document into PDF file.
	 * 
	 * @param doc
	 * @param output
	 * @param pageRange
	 * @throws EngineException 
	 */

	protected void render_PDF( String doc, String output, String pageRange ) throws EngineException
	{
		render( "pdf", doc, output, pageRange ); //$NON-NLS-1$
	}

	private void render( String format, String doc, String output,
			String pageRange ) throws EngineException
	{
		String outputFile = this.getClassFolder( ) + "/" + OUTPUT_FOLDER //$NON-NLS-1$
				+ "/" + output; //$NON-NLS-1$
		String inputFile = this.getClassFolder( )
				+ "/" + INPUT_FOLDER + "/" + doc; //$NON-NLS-1$ //$NON-NLS-2$

		String encoding = "UTF-8"; //$NON-NLS-1$

		IReportDocument document = engine.openReportDocument( inputFile );
		IRenderTask task = engine.createRenderTask( document );
		task.setLocale( Locale.ENGLISH );

		IRenderOption options = new HTMLRenderOption( );
		options.setOutputFileName( outputFile );
		options.setOutputFormat( format );
		options.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
				encoding );

		
		HTMLRenderContext renderContext = new HTMLRenderContext( );
		renderContext.setImageDirectory( IMAGE_DIR );
		HashMap appContext = new HashMap( );
		appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext );
		task.setAppContext( appContext );
		task.setRenderOption( options );

		task.setPageRange( pageRange ); 
		task.render( );
		task.close( );
	}

	/**
	 * Run the input design, generate a report document, and then render the
	 * report document into a html file, <code>pageRange</code> specified the
	 * page(s) to render.
	 * 
	 * @throws IOException
	 * @throws EngineException
	 */

	protected final void runAndThenRender( String input, String output,
			String pageRange ) throws Exception
	{
		String tempDoc = "temp_123aaabbbccc789.rptdocument"; //$NON-NLS-1$

		run( input, tempDoc );

		String from = this.getClassFolder( ) + "/" + OUTPUT_FOLDER //$NON-NLS-1$
				+ "/" + tempDoc; //$NON-NLS-1$
		String temp = this.getClassFolder( )
				+ "/" + INPUT_FOLDER + "/" + tempDoc; //$NON-NLS-1$//$NON-NLS-2$

		try
		{
			copyFile( from, temp );
			render_HTML( tempDoc, output, pageRange );
		}
		catch ( Exception e )
		{
			throw e;
		}
		finally
		{
			// remove the temp file on exit.
			File tempFile = new File( temp );
			if ( tempFile.exists( ) )
				tempFile.delete( );
		}
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param golden
	 *            the reader for golden file
	 * @param output
	 *            the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception
	 *             if any exception
	 */

	private boolean compareTextFile( Reader golden, Reader output,
			String fileName ) throws Exception
	{
		StringBuffer errorText = new StringBuffer( );

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try
		{
			lineReaderA = new BufferedReader( golden );
			lineReaderB = new BufferedReader( output );

			String strA = lineReaderA.readLine( ).trim( );
			String strB = lineReaderB.readLine( ).trim( );
			while ( strA != null )
			{
				// filter the random part of the page.

				String filterA = this.filterLine( strA );
				String filterB = this.filterLine( strB );

				same = filterA.trim( ).equals( filterB.trim( ) );

				if ( !same )
				{
					StringBuffer message = new StringBuffer( );

					message.append( "line=" ); //$NON-NLS-1$
					message.append( lineNo );
					message.append( "(" ); //$NON-NLS-1$
					message.append( fileName );
					message.append( ")" ); //$NON-NLS-1$
					message.append( " is different:\n" );//$NON-NLS-1$
					message.append( " The line from golden file: " );//$NON-NLS-1$
					message.append( strA );
					message.append( "\n" );//$NON-NLS-1$
					message.append( " The line from result file: " );//$NON-NLS-1$
					message.append( strB );
					message.append( "\n" );//$NON-NLS-1$
					throw new Exception( message.toString( ) );
				}

				strA = lineReaderA.readLine( );
				strB = lineReaderB.readLine( );

				lineNo++;
			}

			same = ( strA == null ) && ( strB == null );
		}
		finally
		{
			try
			{
				lineReaderA.close( );
				lineReaderB.close( );
			}
			catch ( Exception e )
			{
				lineReaderA = null;
				lineReaderB = null;

				errorText.append( e.toString( ) );

				throw new Exception( errorText.toString( ) );
			}
		}

		return same;
	}

	/**
	 * All kinds of filter-pattern pairs that will be filtered and replace
	 * during comparasion.
	 */

	private final static Pattern PATTERN_ID_AUTOBOOKMARK = Pattern
			.compile( "id[\\s]*=[\\s]*\"AUTOGENBOOKMARK_[\\d]+\"" ); //$NON-NLS-1$
	private final static Pattern PATTERN_NAME_AUTOBOOKMARK = Pattern
			.compile( "name[\\s]*=[\\s]*\"AUTOGENBOOKMARK_[\\d]+\"" ); //$NON-NLS-1$
	private final static Pattern PATTERN_IID = Pattern
			.compile( "iid[\\s]*=[\\s]*\"/.*(.*)\"" ); //$NON-NLS-1$
	private final static Pattern PATTERN_BG_IMAGE = Pattern
			.compile( "background-image[\\s]*: url[(]'" + IMAGE_DIR + "/.*'[)]" ); //$NON-NLS-1$ //$NON-NLS-2$
	private final static Pattern PATTERN_IMAGE_SOURCE = Pattern
			.compile( "src=\"" + IMAGE_DIR + "/design[\\d]+\"" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Normalize some seeding values, lines that matches certain patterns will
	 * be repalced by a replacement.
	 */

	private static Object[][] FILTER_PATTERNS = {
			{PATTERN_ID_AUTOBOOKMARK, "REPLACEMENT_ID_AUTOBOOKMARK"}, //$NON-NLS-1$
			{PATTERN_NAME_AUTOBOOKMARK, "REPLACEMENT_NAME_AUTOBOOKMARK"}, //$NON-NLS-1$
			{PATTERN_IID, "REPLACEMENT_IID"}, //$NON-NLS-1$
			{PATTERN_BG_IMAGE, "REPLACEMENT_BG_IMAGE"}, //$NON-NLS-1$
			{PATTERN_IMAGE_SOURCE, "REPLACEMENT_IMAGE_SOURCE"} //$NON-NLS-1$
	};

	/**
	 * Replace the given string with a replacement if it matches a certain
	 * pattern.
	 * 
	 * @param str
	 * @return filtered string, the tokens that matches the patterns are
	 *         replaced with replacement.
	 */

	protected String filterLine( String str )
	{
		String result = str;

		for ( int i = 0; i < FILTER_PATTERNS.length; i++ )
		{
			Pattern pattern = (Pattern) FILTER_PATTERNS[i][0];
			String replacement = (String) FILTER_PATTERNS[i][1];

			Matcher matcher = pattern.matcher( result );
			result = matcher.replaceAll( replacement );
		}

		return result;
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path where the test java source file locates.
	 */
	protected String getBaseFolder( )
	{
		String className = getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
		className = className.substring( 0, lastDotIndex );
		return PLUGIN_PATH + className.replace( '.', '/' );
	}

}
