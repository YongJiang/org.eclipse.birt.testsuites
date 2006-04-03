package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class DynamicParameterTest extends BaseTestCase
{
	static final String INPUT_FILE_NAME = "DynamicParameterTest.xml"; //$NON-NLS-1$
	static final String OUTPUT_FILE_NAME = "DynamicParameterTest_out.xml"; //$NON-NLS-1$
	static final String GOLDEN_FILE_NAME = "DynamicParameterTest_golden.xml";
	
	public DynamicParameterTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	 public static Test suite()
	    {
			
			return new TestSuite(DynamicParameterTest.class);
		}
		protected void setUp( ) throws Exception
		{
			super.setUp( );
			openDesign( INPUT_FILE_NAME ); //$NON-NLS-1$
		}
		
		public void testPropertiesOfDynamicParameter( ) throws Exception
		{
			
			SlotHandle params = designHandle.getParameters( );
			ScalarParameterHandle handle = (ScalarParameterHandle) params.get( 0 );
			SlotHandle dataset = designHandle.getDataSets();
			DataSetHandle ds = (DataSetHandle)dataset.get(0);
			assertEquals("dataset1",ds.getName());
			assertEquals("dataset2",dataset.get(1).getName());
			
				
		   // test on the 1st param
			assertEquals(DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC,handle.getValueType());
			
			assertEquals(DesignChoiceConstants.PARAM_TYPE_DATETIME,handle.getDataType());
			
			handle.setPromptText("abc");
			assertEquals("abc",handle.getPromptText());
			
			handle.setDataSetName("dataset1");
			handle.setValueExpr("row[\"ORDERDATE\"]");
			assertEquals("row[\"ORDERDATE\"]",handle.getValueExpr());

			handle.setControlType(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX);
			assertEquals(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX,handle.getControlType());
			
			handle.setDefaultValue("10/15/2005");
			assertEquals("10/15/2005",handle.getDefaultValue());
			
			handle.setFormat(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE);
			assertEquals(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE,handle.getFormat());
			
			handle.setListlimit(20);
			assertEquals(20,handle.getListlimit());
			
			assertTrue(handle.isMustMatch());
			assertTrue(handle.isFixedOrder());
			
			//test on the 2nd param
			handle = (ScalarParameterHandle) params.get( 1 );
			
			assertEquals(DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC,handle.getValueType());
			assertEquals(DesignChoiceConstants.PARAM_TYPE_FLOAT,handle.getDataType());
			
			handle.setDataSetName("dataset2");
			handle.setValueExpr("row[\"Amount\"]");
			assertEquals("row[\"Amount\"]",handle.getValueExpr());
			
			assertEquals(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX,handle.getControlType());
	
			assertFalse(handle.isHidden());
			assertEquals("scalar para help",handle.getHelpText());
			assertFalse(handle.isConcealValue());
			assertFalse(handle.allowBlank());
			assertTrue(handle.allowNull());
			assertEquals(DesignChoiceConstants.SCALAR_PARAM_ALIGN_LEFT,handle.getAlignment());
			assertEquals("##,###.##",handle.getFormat( ));
			assertEquals(5,handle.getListlimit());
			
			//test on params on duplicated name
			try{
				openDesign( "DynamicParamterTest1.xml" );
			   }
			catch (DesignFileException e)
			{}
			
			super.saveAs(OUTPUT_FILE_NAME);
			assertTrue( compareTextFile( GOLDEN_FILE_NAME, OUTPUT_FILE_NAME ));

		}
}



