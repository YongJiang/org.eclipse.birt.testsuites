package org.eclipse.birt.report.tests.model.api;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;


public class MoveLibraryTest extends BaseTestCase
{
	String fileName = "BlankReport.xml";
	
	private static String LibA = "LibA.xml";
	private static String LibB = "LibB.xml";
	private static String LibC = "LibC.xml";
	
	private String LibD = "LibA.xml";
	
	
	public MoveLibraryTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(MoveLibraryTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		copyInputToFile ( INPUT_FOLDER + "/" + fileName );
		copyInputToFile ( INPUT_FOLDER + "/" + LibA );
		copyInputToFile ( INPUT_FOLDER + "/" + LibB );
		copyInputToFile ( INPUT_FOLDER + "/" + LibC );
		openDesign(fileName);
		
	}
	public void tearDown( )
	{
		removeResource( );
	}
	
	public void testCopyLibA( ) throws Exception
	{
		sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		assertNotNull( sessionHandle );

		libraryHandle = sessionHandle.openLibrary(getTempFolder()+ "/"+ INPUT_FOLDER + "/" + LibA );
		assertNotNull(libraryHandle);
		
		String TempFile=this.genOutputFile(LibD);
		libraryHandle.saveAs( TempFile );
		//super.saveLibraryAs(LibD);
		//libraryHandle.saveAs(LibD);
	}
	public void testMoveLibrary( ) throws Exception
	{
		openDesign(fileName);
		designHandle.includeLibrary( LibD, "LibD" );
		LibraryHandle libHandle = designHandle.getLibrary( "LibD" );
		
		TextItemHandle textLibHandle = (TextItemHandle)libHandle.findElement( "text1" );
		assertNotNull("Text should not be null", textLibHandle);
		DataItemHandle dataLibHandle = (DataItemHandle)libHandle.findElement( "data1" );
		assertNotNull("Data should not be null", dataLibHandle);
	//  SharedStyleHandle styleLibHandle = (SharedStyleHandle)libHandle.findStyle( "style1" );
	//	assertNotNull("Style should not be null", styleLibHandle);
		
		TextItemHandle textHandle = (TextItemHandle)designHandle.getElementFactory().newElementFrom( textLibHandle, "text1" );
		DataItemHandle dataHandle = (DataItemHandle)designHandle.getElementFactory().newElementFrom( dataLibHandle, "data1" );
	//	StyleHandle styleHandle = (StyleHandle)designHandle.getElementFactory().newStyle( "style1" );
		
		designHandle.getBody().add( dataHandle );
		designHandle.getBody().add( textHandle );
		//designHandle.getStyles().add( styleHandle );

		
		
		assertEquals( "yellow" , dataHandle.getExtends().getStringProperty("backgroundColor"));
		assertEquals( "red" , textHandle.getExtends().getStringProperty("backgroundColor"));
		
		File deleteLibD = new File(LibD);
		deleteLibD.delete();
		String TempFile=this.genOutputFile("SavedReport.xml");
		designHandle.saveAs( TempFile);
		
		openDesign("SavedReport.xml");
		assertNotNull((TextItemHandle)designHandle.findElement("text1"));
		assertNotNull((DataItemHandle)designHandle.findElement("data1"));
	    assertEquals( null , ((TextItemHandle)designHandle.findElement("text1")).getStringProperty("backgroundColor"));
		assertEquals( null , ((DataItemHandle)designHandle.findElement("data1")).getStringProperty("backgroundColor"));

	}
}
