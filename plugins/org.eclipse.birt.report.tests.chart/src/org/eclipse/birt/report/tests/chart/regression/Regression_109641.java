/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.tests.chart.regression;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Exception is thrown out when have one or more data points missing from a
 * series
 * </p>
 * Test description:
 * </p>
 * The chart allows missing values and no exception is thrown out when have one
 * or more data points missing from a series
 * </p>
 */

public class Regression_109641 extends ChartTestCase
{

	private static String GOLDEN = "Regression_109641.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_109641.jpg"; //$NON-NLS-1$

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A chart model instance
	 */
	private Chart cm = null;

	/**
	 * The swing rendering device
	 */
	private IDeviceRenderer dRenderer = null;

	private GeneratedChartState gcs = null;

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		Regression_109641 st = new Regression_109641( );
	}

	/**
	 * Constructor
	 */
	public Regression_109641( )
	{
		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			dRenderer = ps.getDevice( "dv.JPG" );//$NON-NLS-1$

		}
		catch ( ChartException ex )
		{
			ex.printStackTrace( );
		}
		cm = createChart( );
		bindGroupingData( cm );

		BufferedImage img = new BufferedImage( 500, 500,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.getClassFolder( )
				+ OUTPUT_FOLDER + OUTPUT );

		Bounds bo = BoundsImpl.create( 0, 0, 500, 500 );
		bo.scale( 72d / dRenderer.getDisplayServer( ).getDpiResolution( ) );

		Generator gr = Generator.instance( );

		try
		{
			gcs = gr.build( dRenderer.getDisplayServer( ), cm, null, bo, null );
			gr.render( dRenderer, gcs );
		}
		catch ( ChartException e )
		{
			e.printStackTrace( );
		}
	}

	public void test( ) throws Exception
	{
		Regression_109641 st = new Regression_109641( );
		assertTrue( this.compareBytes( GOLDEN, OUTPUT ) );
	}

	private void bindGroupingData( Chart chart )

	{

		// Data Set

		final Object[][] data = new Object[][]{
				{"Chicago", new Integer( 1980 ), new Double( 2783726 )},
				{"New York", new Integer( 1980 ), new Double( 7322564 )},
				{"Los Angeles", new Integer( 1990 ), new Double( 4694820 )},
				{"Chicago", new Integer( 1990 ), new Double( 3196016 )},
				{"New York", new Integer( 1990 ), new Double( 8008278 )},
				{"Los Angeles", new Integer( 2000 ), new Double( 6819951 )},
				{"Chicago", new Integer( 2000 ), new Double( 2569121 )},
				{"New York", new Integer( 2000 ), new Double( 8085742 )},};
		try
		{
			Generator gr = Generator.instance( );
			gr.bindData( new IDataRowExpressionEvaluator( ) {

				int idx = 0;

				public void close( )
				{
				}

				public Object evaluate( String expression )
				{
					if ( "X".equals( expression ) )
					{
						return data[idx][1];
					}
					else if ( "Y".equals( expression ) )
					{
						return data[idx][2];
					}
					else if ( "G".equals( expression ) )
					{
						return data[idx][0];
					}
					return null;
				}

				public Object evaluateGlobal( String expression )
				{
					return evaluate( expression );
				}

				public boolean first( )
				{
					idx = 0;
					return true;
				}

				public boolean next( )
				{
					idx++;
					return ( idx < 7 );
				}
			}, chart, new RunTimeContext( ) );

		}

		catch ( ChartException e )
		{
			e.printStackTrace( );
		}
	}

	private Chart createChart( )

	{

		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

		// X-Axis

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );

		// Y-Axis

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );

		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );

		// X-Series

		Series seCategory = SeriesImpl.create( );

		Query xQ = QueryImpl.create( "G" );

		seCategory.getDataDefinition( ).add( xQ );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );

		xAxisPrimary.getSeriesDefinitions( ).add( sdX );

		sdX.getSeries( ).add( seCategory );

		// -------------------------------------------------------------

		sdX.setSorting( SortOption.ASCENDING_LITERAL );

		sdX.getGrouping( ).setEnabled( true );

		sdX.getGrouping( ).setGroupType( DataType.TEXT_LITERAL );

		sdX.getGrouping( ).setAggregateExpression( "Sum" );

		sdX.getGrouping( ).setGroupingInterval( 0 );

		// -------------------------------------------------------------

		// Y-Series

		BarSeries bs = (BarSeries) BarSeriesImpl.create( );

		bs.getLabel( ).setVisible( false );

		Query yQ = QueryImpl.create( "Y" );

		bs.getDataDefinition( ).add( yQ );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );

		yAxisPrimary.getSeriesDefinitions( ).add( sdY );

		sdY.getSeriesPalette( ).update( 0 );

		sdY.getSeries( ).add( bs );

		return cwaBar;

	}

}
