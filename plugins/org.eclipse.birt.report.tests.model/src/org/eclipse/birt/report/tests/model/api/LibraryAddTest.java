package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;
;

public class LibraryAddTest extends BaseTestCase
{	
	private String fileName = "Library_Addin_test.xml";
	private String LibImpFile = "Library_Import_test.xml";
	private String inputLibraryName = "LibraryCreatLib.xml";
	private String  libname = "LibA.xml";
    private String outFileName = "Library_Addin_Test_out.xml"; 
	private String goldenFileName = "Library_Addin_Test_golden.xml"; 
	String LibFile= inputLibraryName;
	String LibFileError1 = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + "LibY.xml";

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		//copyResource_INPUT( fileName, fileName );
		//copyResource_INPUT( inputLibraryName, inputLibraryName );
		//copyResource_INPUT( libname, libname );
		//copyResource_INPUT( LibImpFile, LibImpFile );
		//copyResource_GOLDEN( goldenFileName, goldenFileName );
		
		copyInputToFile ( INPUT_FOLDER + "/" + fileName );
		copyInputToFile ( INPUT_FOLDER + "/" + inputLibraryName );
		copyInputToFile ( INPUT_FOLDER + "/" + libname );
		copyInputToFile ( INPUT_FOLDER + "/" + LibImpFile );
		
		copyGoldenToFile ( GOLDEN_FOLDER + "/" + goldenFileName );

		
	}
	public void tearDown( )
	{
		removeResource( );
	}
	public LibraryAddTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(LibraryAddTest.class);
	}
	
	public void testAddinLibrary( ) throws Exception
	{
		openDesign( LibImpFile );
		designHandle.includeLibrary( LibFile, "LibB" );
		designHandle.includeLibrary( libname , "");
		//saveAs( outFileName );
	    //assertTrue( compareTextFile( goldenFileName, outFileName ) );
		String TempFile=this.genOutputFile(outFileName);
		designHandle.saveAs( TempFile );
		assertTrue( compareTextFile( goldenFileName, outFileName ) );

		
	    
	    try
	    {
	    designHandle.includeLibrary(LibFileError1, "LibY" );
	    }
	    catch(SemanticException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(DesignFileException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(Exception e)
	    {
	    	assertNotNull(e);
	    }
	    
	    try
	    {
	    designHandle.includeLibrary( "../inputLibZ.xml", "LibZ" );
	    }
	    catch(SemanticException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(DesignFileException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(Exception e)
	    {
	    	assertNotNull(e);
	    }
	    
	    
	    	}
	public void testRemoveLibrary( ) throws Exception
	{
		openDesign( LibImpFile );
		designHandle.includeLibrary( LibFile, "LibB" );
		designHandle.includeLibrary( "../input/LibA.xml" , "");
		LibraryHandle lib1 = designHandle.findLibrary( "LibraryCreatLib.xml" );
		LibraryHandle lib2 = designHandle.findLibrary( "LibA.xml" );
		assertNotNull(lib1);
		assertNotNull(lib2);
		
		designHandle.dropLibrary(lib1);
		assertEquals(1,designHandle.getListProperty( ReportDesign.LIBRARIES_PROP).size( ));
		
		designHandle.dropLibrary( lib2 );
		assertEquals(0,designHandle.getListProperty( ReportDesign.LIBRARIES_PROP).size( ));
		
	}
}
