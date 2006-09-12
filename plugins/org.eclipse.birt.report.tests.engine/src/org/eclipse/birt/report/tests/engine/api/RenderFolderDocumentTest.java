
package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>RenderFolderDocument test</b>
 * <p>
 * This case tests rendering folder-based report document.
 * 
 */
public class RenderFolderDocumentTest extends EngineCase
{

	private String separator = System.getProperty( "file.separator" );
	private String INPUT = getClassFolder( ) + separator + INPUT_FOLDER
			+ separator;
	private String OUTPUT = getClassFolder( ) + separator + OUTPUT_FOLDER
			+ separator;
	private String folderArchive, htmlOutput;
	private IReportDocument reportDoc;
	private IRenderTask renderTask;
	private IRenderOption htmlOption, pdfOption;

	public RenderFolderDocumentTest( String name )
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

		htmlOption = new HTMLRenderOption( );
		htmlOption.setOutputFormat( HTMLRenderOption.HTML );
	}

	public void testRenderFolderDocument1( )
	{
		String renderDoc = "folderdocument_case1";
		renderFolderDocument( renderDoc );
	}

	public void testRenderFolderDocument2( )
	{
		String renderDoc = "folderdocument_long_text";
		renderFolderDocument( renderDoc );

	}

	public void testRenderFolderDocument3( )
	{
		String renderDoc = "folderdocument_master_page";
		renderFolderDocument( renderDoc );

	}

	public void testRenderFolderDocument4( )
	{
		String renderDoc = "folderdocument_multiple_datasets";
		renderFolderDocument( renderDoc );

	}

	public void testRenderFolderDocument5( )
	{
		String renderDoc = "folderdocument_pages9";
		renderFolderDocument( renderDoc );

	}

	public void testRenderFolderDocument6( )
	{
		String renderDoc = "folderdocument_table_nest_pages";
		renderFolderDocument( renderDoc );

	}

	public void testRenderFolderDocument7( )
	{
		String renderDoc = "folderdocument_chart";
		renderFolderDocument( renderDoc );

	}

	/*
	 * Verification: Test whether folder and files can be dropped from
	 * folder-based report document Test Method: FolderArchiveWriter.dropStream(
	 * String relativePath )
	 */
	public void testDropDocumentFolder1( )
	{
		String report_design = "report_document";
		dropFolder( report_design, "content" );
	}

	public void testDropDocumentFolder2( )
	{
		String report_design = "report_document";
		dropFolder( report_design, "Data" );
	}

	public void testDropDocumentFolder3( )
	{
		String report_design = "report_document";
		dropFolder( report_design, "design" );
	}

	public void testDropDocumentFolder4( )
	{
		String report_design = "report_document";
		dropFolder( report_design, "" );
	}

	public void testDropDocumentFolder5( )
	{
		String report_design = "report_document";
		dropFolder( report_design, "nonexist" );
	}

	private void dropFolder( String report_design, String dropDir )
	{

		folderArchive = OUTPUT + "drop_" + report_design + separator;
		String design = INPUT + report_design + ".rptdesign";
		try
		{
			createFolderDocument( design, folderArchive );
			FolderArchiveWriter writer = new FolderArchiveWriter( folderArchive );
			File doc = new File( folderArchive );

			if ( doc.exists( ) )
			{
				// delete content folder
				doc = new File( folderArchive + separator + dropDir );
				if ( doc.exists( ) )
				{
					writer.dropStream( dropDir );
					assertFalse( "FolderArchiveWriter failed to drop folder"
							+ dropDir + " in document", doc.exists( ) );
				}
			}
			writer.finish( );
		}
		catch ( EngineException e )
		{
			e.printStackTrace( );
			fail( "RunTask failed to create folder-based document!"
					+ e.getLocalizedMessage( ) );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
			fail( "RunTask failed to create folder-based document!"
					+ e.getLocalizedMessage( ) );
		}
	}

	/**
	 * create folder-based report document
	 * 
	 * @param design
	 *            source report design with absolute path
	 * @param folderDoc
	 *            folderdocument with absolute path like "c:/doc/"
	 * @throws IOException
	 * @throws EngineException
	 */
	private void createFolderDocument( String design, String folderDoc )
			throws IOException, EngineException
	{
		IRunTask runTask;
		FolderArchiveWriter writer;
		folderArchive = folderDoc;

		writer = new FolderArchiveWriter( folderArchive );
		IReportRunnable runnable = engine.openReportDesign( design );
		runTask = engine.createRunTask( runnable );
		runTask.run( writer );
		runTask.close( );
		writer.finish( );

	}

	/**
	 * render output html from folder-based document
	 * 
	 * @param docName.
	 *            The value must be "folderdocument_reportname"
	 */
	private void renderFolderDocument( String docName )
	{

		String designName, report_design;
		designName = docName.substring( 15 );
		report_design = INPUT + designName + ".rptdesign";

		folderArchive = OUTPUT + docName + separator;
		htmlOutput = OUTPUT + docName + ".html";
		new File( OUTPUT ).mkdirs( );
		try
		{
			createFolderDocument( report_design, folderArchive );

			FolderArchiveReader reader = new FolderArchiveReader( folderArchive );
			reportDoc = engine.openReportDocument( folderArchive );
			renderTask = engine.createRenderTask( reportDoc );

			htmlOption.setOutputFileName( htmlOutput );
			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );

			renderTask.setRenderOption( htmlOption );
			renderTask.setAppContext( appContext );
			renderTask.setLocale( Locale.ENGLISH );
			renderTask.setPageRange( "All" );
			renderTask.render( );
			renderTask.close( );

			assertNotNull( docName
					+ ".html failed to render from folder-based document",
					htmlOutput );

			reader.close( );

		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace( );
			assertTrue( "IOException (when create and render " + docName
					+ " folder-based document)", false );
		}
		catch ( EngineException ee )
		{
			ee.printStackTrace( );
			assertTrue( "EngineException (when create and render " + docName
					+ " folder-based document)", false );
		}

	}

}
