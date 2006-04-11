
package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;

import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class RenderTaskTest extends EngineCase
{

	private String report_design;
	private String report_document;
	private IDocArchiveReader archive;
	private IReportDocument reportDoc;
	private String outputFileName;
	private String separator = System.getProperty( "file.separator" );
	protected String path = getClassFolder( ) + separator;
	private String outputPath = path + OUTPUT_FOLDER + separator;
	private String inputPath = path + INPUT_FOLDER + separator;

	/*
	 * protected String path =
	 * "D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/";
	 * 
	 * protected String input = "input", output = "output";
	 */
	public RenderTaskTest( String name )
	{
		super( name );
	}

	public static Test Suite( )
	{
		return new TestSuite( RenderTaskTest.class );
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Test render(long pageNumber) method
	 */
	public void testRender( )
	{

		// Test render(long) //renderReport("case1","no"); //Test render()
		renderReport( "case1", "All" );
		renderReport( "table_pages", "All" );
		renderReport( "long_text", "All" );

		renderReport( "multiple_datasets", "All" );
		renderReport( "table_nest_pages", "All" );
		renderReport( "oncreate-style-label", "All" );
		renderReport( "javascript-support-data", "All" );
		renderReport( "master_page", "All" );
		renderReport( "chart", "All" );
		// test arrive here. renderReport("complex_report","All");
		renderReport( "area3dChart", "All" );
		renderReport( "MeterChart", "All" );
		renderReport( "image_in_DB", "All" );
		renderReport( "multiple_masterpage", "All" ); //
		renderReport( "report_from_library1", "All" );

		// Test render(string) renderReport("pages9","All");
		renderReport( "pages9", null );
		renderReport( "pages9", "" );
		renderReport( "pages9", "2" );
		renderReport( "pages9", "3,10" );
		renderReport( "pages9", "2-9" );
		renderReport( "pages9", "0-100" );
		renderReport( "pages9", "0" );
		renderReport( "pages9", "abc" );

	}

	public void testRenderBookmark( )
	{
		renderReport( "items_bookmark", "bookmark_label" );
		renderReport( "items_bookmark", "bookmark_text" );
		renderReport( "items_bookmark", "bookmark_image" );
		renderReport( "items_bookmark", "bookmark_gridrow" );
		renderReport( "items_bookmark", "bookmark_chart" );
		renderReport( "multiple_masterpage", "bookmark_label" );
		renderReport( "multiple_masterpage", "bookmark_text" );
		renderReport( "multiple_masterpage", "bookmark_image" );
		renderReport( "multiple_masterpage", "bookmark_gridrow" );
		renderReport( "multiple_masterpage", "bookmark_chart" );
	}

	public void testRenderReportlet( )
	{
		InstanceID iid;
		iid = findIid( "iid_reportlet", "LIST" );
		renderReportlet( "iid_reportlet", iid, "LIST" );

		iid = findIid( "iid_reportlet", "TABLE" );
		renderReportlet( "iid_reportlet", iid, "TABLE" );

		iid = findIid( "iid_reportlet", "EXTENDED" );
		renderReportlet( "iid_reportlet", iid, "EXTENDED" );

	}

	/*
	 * Find instance id according to element type and design file.
	 */
	private InstanceID findIid( String fileName, String type )
	{
		InstanceID iid = null;
		report_document = inputPath + fileName + ".rptdocument";
		report_design = inputPath + fileName + ".rptdesign";
		IRenderTask task;

		try
		{
			createReportDocument( report_design, report_document );
			reportDoc = engine.openReportDocument( report_document );
			task = engine.createRenderTask( reportDoc );
			task.setLocale( Locale.ENGLISH );
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			HashMap appContext = new HashMap( );
			task.setAppContext( appContext );

			ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
			htmlRenderOptions.setOutputStream( ostream );
			htmlRenderOptions.setOutputFormat( "html" );

			task.setRenderOption( htmlRenderOptions );
			task.render( );
			task.close( );

			String content = ostream.toString( "utf-8" );
			Pattern typePattern = Pattern.compile( "(element_type=\"" + type
					+ "\".*iid=\".*\")" );
			Matcher matcher = typePattern.matcher( content );

			if ( matcher.find( ) )
			{
				String tmp_type = matcher.group( 1 ), strIid;
				strIid = tmp_type.substring( tmp_type.indexOf( "iid" ) );

				strIid = strIid.substring( 5, strIid.indexOf( "\"", 6 ) );
				iid = InstanceID.parse( strIid );
				return iid;
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertFalse( "Failed to find instance id of " + type, true );
		}

		return iid;
	}

	/*
	 * render reportlet according to docfile and instance id
	 */
	protected void renderReportlet( String docName, InstanceID iid, String type )
	{
		if ( iid == null )
		{
			assertFalse( "Failed to find instance id of " + type, true );
		}
		else
		{
			report_document = inputPath + docName + ".rptdocument";

			IRenderTask task;

			// create directories to deposit output files
			createDir( docName );
			try
			{
				reportDoc = engine.openReportDocument( report_document );
				task = engine.createRenderTask( reportDoc );
				IRenderOption htmlRenderOptions = new HTMLRenderOption( );
				HTMLRenderContext renderContext = new HTMLRenderContext( );
				renderContext.setImageDirectory( "image" );
				HashMap appContext = new HashMap( );
				appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						renderContext );
				task.setAppContext( appContext );
				outputFileName = outputPath + docName + "/html/" + type
						+ ".html";
				htmlRenderOptions.setOutputFileName( outputFileName );
				htmlRenderOptions.setOutputFormat( "html" );
				task.setRenderOption( htmlRenderOptions );
				task.setInstanceID( iid );
				task.render( );
				task.close( );

			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				assertTrue( "Render reportlet " + type + "from" + docName
						+ " failed. " + e.getLocalizedMessage( ), false );
			}
		}
	}

	protected void renderReport( String fileName, String pageRange )
	{
		report_design = inputPath + fileName + ".rptdesign";
		report_document = outputPath + fileName + "_reportdocument";

		IRenderTask task;

		// create directories to deposit output files
		createDir( fileName );
		try
		{
			createReportDocument( report_design, report_document );
			// open the document in the archive.
			reportDoc = engine.openReportDocument( report_document );

			// Set IRenderOption
			IRenderOption htmlRenderOptions = new HTMLRenderOption( );
			IRenderOption pdfRenderOptions = new HTMLRenderOption( );

			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );

			htmlRenderOptions.setOutputFormat( "html" );
			pdfRenderOptions.setOutputFormat( "pdf" );
			htmlRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING, "UTF-8" );
			pdfRenderOptions.getOutputSetting( ).put(
					HTMLRenderOption.URL_ENCODING, "UTF-8" );

			int i_bookmark = pageRange.indexOf( "bookmark_" );
			if ( i_bookmark == -1 )
			{
				if ( pageRange != null && pageRange.equals( "no" ) )
				{
					/* set page number 1 and then render the first page */
					// render html output
					outputFileName = outputPath + fileName + "/html/page1"
							+ ".html";
					removeFile( outputFileName );
					htmlRenderOptions.setOutputFileName( outputFileName );

					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( appContext );
					task.setRenderOption( htmlRenderOptions );
					task.setPageNumber( 1 );
					task.render( );
					task.close( );

					File htmlFile = new File( outputFileName );
					assertTrue( "Render " + fileName + " to html failed. ",
							htmlFile.exists( ) );
					assertTrue( "Render " + fileName + " to html failed. ",
							htmlFile.length( ) != 0 );
					// render pdf output
					outputFileName = outputPath + fileName + "/pdf/page1"
							+ ".pdf";
					removeFile( outputFileName );
					pdfRenderOptions.setOutputFileName( outputFileName );
					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( appContext );
					task.setRenderOption( pdfRenderOptions );
					task.setPageNumber( 1 );
					task.render( );
					task.close( );
					File pdfFile = new File( outputFileName );
					assertTrue( "Render " + fileName + " to pdf failed. ",
							pdfFile.exists( ) );
					assertTrue( "Render " + fileName + " to pdf failed. ",
							pdfFile.length( ) != 0 );

				}
				else
				{
					/* set page range and then render according to range */

					// render html output
					outputFileName = outputPath + fileName + "/html/page"
							+ pageRange + ".html";
					removeFile( outputFileName );
					htmlRenderOptions.setOutputFileName( outputFileName );
					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( appContext );
					task.setRenderOption( htmlRenderOptions );
					task.setPageRange( pageRange );
					task.render( );
					task.close( );

					File htmlFile = new File( outputFileName );
					if ( pageRange != null
							&& ( pageRange.equals( "0" ) || pageRange
									.equals( "abc" ) ) )
					{
						assertFalse( htmlFile.exists( ) );
					}
					else
					{
						assertTrue( "Render " + fileName + " to html failed. "
								+ pageRange, htmlFile.exists( ) );
						assertTrue( "Render " + fileName + " to html failed. "
								+ pageRange, htmlFile.length( ) != 0 );
					}
					// render pdf output
					outputFileName = outputPath + fileName + "/pdf/page"
							+ pageRange + ".pdf";
					removeFile( outputFileName );
					pdfRenderOptions.setOutputFileName( outputFileName );

					task = engine.createRenderTask( reportDoc );
					task.setLocale( Locale.ENGLISH );
					task.setAppContext( new HashMap( ) );
					task.setRenderOption( pdfRenderOptions );
					task.setPageRange( pageRange );
					task.render( );
					task.close( );

					File pdfFile = new File( outputFileName );
					if ( pageRange != null
							&& ( pageRange.equals( "0" ) || pageRange
									.equals( "abc" ) ) )
					{
						assertFalse( pdfFile.exists( ) );
					}
					else
					{
						assertTrue( "Render " + fileName + " to pdf failed. "
								+ pageRange, pdfFile.exists( ) );
						assertTrue( "Render " + fileName + " to pdf failed. "
								+ pageRange, pdfFile.length( ) != 0 );
					}

				}
			}
			else
			{
				String bookmark = pageRange.substring( 9 );
				// render html output
				outputFileName = outputPath + fileName + "/html/bookmark_"
						+ bookmark + ".html";
				removeFile( outputFileName );
				htmlRenderOptions.setOutputFileName( outputFileName );

				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setBookmark( bookmark );
				task.setRenderOption( htmlRenderOptions );
				task.render( );
				task.close( );

				File htmlFile = new File( outputFileName );
				assertTrue( "Render " + fileName + " to html failed. "
						+ pageRange, htmlFile.exists( ) );
				assertTrue( "Render " + fileName + " to html failed. "
						+ pageRange, htmlFile.length( ) != 0 );
				// render pdf output
				outputFileName = outputPath + fileName + "/pdf/bookmark_"
						+ bookmark + ".pdf";
				removeFile( outputFileName );
				pdfRenderOptions.setOutputFileName( outputFileName );

				task = engine.createRenderTask( reportDoc );
				task.setLocale( Locale.ENGLISH );
				task.setAppContext( appContext );
				task.setBookmark( bookmark );
				task.setRenderOption( pdfRenderOptions );
				task.render( );
				task.close( );

				File pdfFile = new File( outputFileName );
				assertTrue( "Render " + fileName + " to pdf failed. "
						+ pageRange, pdfFile.exists( ) );
				assertTrue( "Render " + fileName + " to pdf failed. "
						+ pageRange, pdfFile.length( ) != 0 );

			}
			task.close( );

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			assertTrue( "Render " + fileName + " failed. "
					+ e.getLocalizedMessage( ), false );
		}

	}

	/**
	 * create the report document.
	 * 
	 * @throws Exception
	 */
	protected void createReportDocument( String reportdesign,
			String reportdocument ) throws Exception
	{
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter( reportdocument );
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign( reportdesign );
		// create an IRunTask
		IRunTask runTask = engine.createRunTask( report );
		// execute the report to create the report document.
		runTask.setAppContext( new HashMap( ) );
		runTask.run( archive );
		// close the task, release the resource.
		runTask.close( );
	}

	/**
	 * create need directory creat html and pdf directory under the need
	 * directory
	 */
	protected void createDir( String name )
	{
		String out = OUTPUT_FOLDER;
		File fdir = new File( path + out + "/" + name + "/" );
		if ( !fdir.mkdir( ) )
		{
			System.err.println( "Cannot create output directories" );
		}
		fdir = new File( path + out + "/" + name + "/html/" );
		if ( !fdir.mkdir( ) )
		{
			System.err.println( "Cannot create output html directories" );
		}
		fdir = new File( path + out + "/" + name + "/pdf/" );
		if ( !fdir.mkdir( ) )
		{
			System.err.println( "Cannot create output pdf directories" );
		}
	}

}
