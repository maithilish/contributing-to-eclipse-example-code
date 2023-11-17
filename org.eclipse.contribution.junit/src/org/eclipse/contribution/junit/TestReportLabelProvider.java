/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Erich Gamma (erich_gamma@ch.ibm.com) and
 * 	   Kent Beck (kent@threeriversinstitute.org)
 * 
 * Code fixes to run on Eclipse Platform 3 and 4. 
 * 
 * Contributor:
 *     Maithilish Bhat (maithilish@gmail.com)
 *
 *******************************************************************************/
package org.eclipse.contribution.junit;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class TestReportLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {

	private final Image[] images;

	public TestReportLabelProvider() {
		images = new Image[2];
		images[TestResult.OK] = createImage("icons/testok.gif");
		images[TestResult.FAILED] = createImage("icons/testerr.gif");
	}

	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		final TestResult result = (TestResult) element;
		switch (columnIndex) {
		case 0:
			return result.method + " - " + result.klass;
		case 1:
			return Long.toString(result.testDuration());
		}
		return null;
	}

	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {
		if (columnIndex == 0)
			return images[((TestResult) element).status];
		return null;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < images.length; i++)
			images[i].dispose();
	}

	@Override
	public Color getForeground(final Object element) {
		if (((TestResult) element).isFailure())
			return Display.getDefault().getSystemColor(SWT.COLOR_RED);
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		return null;
	}

	private static Image createImage(final String path) {
		final URL url = JUnitPlugin.getPlugin().getInstallURL();
		ImageDescriptor descriptor = null;
		try {
			descriptor = ImageDescriptor.createFromURL(new URL(url, path));
		} catch (final MalformedURLException e) {
			descriptor = ImageDescriptor.getMissingImageDescriptor();
		}
		return descriptor.createImage();
	}
}