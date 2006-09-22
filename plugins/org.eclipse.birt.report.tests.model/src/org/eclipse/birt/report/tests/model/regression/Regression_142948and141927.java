/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * <b>142948:</b>
 * Invoke DefaultResourceLocator.findResource() with a url string like
 * "file:/....." will return null, even the file exists</p>
 * <b>141927:</b>
 * Resource locator doesn't work when set as "http://.."
 * </p>
 * Test description:
 * <p>
 * Find resource like "file:/.....", if it exists, won't return null. If it
 * doesn't exist, return null</p>
 * Find resource with HTTP protocol
 */

public class Regression_142948and141927 extends BaseTestCase
{

	private String filename = "Regression_142948and141927.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws IOException
	 */
	public void test_regression_142948and141927( ) throws DesignFileException, IOException
	{
		openDesign( filename );

		designHandle.setFileName( null );
		String filePath = "file:/" + getClassFolder( ) + INPUT_FOLDER //$NON-NLS-1$
				+ filename;

		designHandle.setFileName( filePath );
		URL url = designHandle
				.findResource( filename, IResourceLocator.LIBRARY );
		assertNotNull( url );
		
		url = designHandle.findResource( "NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY );
		assertNull( url );

		//Find resource with HTTP protocol
		designHandle.getModule( ).setSystemId(
				new URL( "http://www.eclipse.org/" ) ); //$NON-NLS-1$

		url = designHandle.findResource( "images/EclipseBannerPic.jpg", //$NON-NLS-1$
				IResourceLocator.IMAGE );

		assertEquals( "http://www.eclipse.org/images/EclipseBannerPic.jpg", //$NON-NLS-1$
				url.toString( ) );
		
	}

}
