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
package org.eclipse.contribution.junit.test;

import org.eclipse.contribution.junit.internal.ui.ResultView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import junit.framework.TestCase;

public class ViewColorTest extends TestCase {

	private ResultView view;

	@Override
	public void setUp() throws PartInitException {
		view = (ResultView) getPage().showView("org.eclipse.contribution.junit.resultView");
	}

	@Override
	public void tearDown() {
		getPage().hideView(view);
	}

	public void testResultViewGreen() throws PartInitException {
		view.getListener().testsStarted(null, 0);
		view.getListener().testsFinished(null);
		final Display display = view.getControl().getDisplay();
		final Color green = display.getSystemColor(SWT.COLOR_GREEN);
		assertEquals(green, view.getControl().getBackground());
	}

	public void testResultViewRed() throws PartInitException {
		view.getListener().testsStarted(null, 0);
		view.getListener().testFailed(null, "class", "method", "trace");
		final Display display = view.getControl().getDisplay();
		final Color red = display.getSystemColor(SWT.COLOR_RED);
		assertEquals(red, view.getControl().getBackground());

		view.getListener().testsFinished(null);
		assertEquals(red, view.getControl().getBackground());
	}

	public void testResultViewColorReset() throws Exception {
		view.getListener().testsStarted(null, 0);
		view.getListener().testFailed(null, "class", "method", "trace");
		view.getListener().testsFinished(null);
		final Display display = view.getControl().getDisplay();
		final Color red = display.getSystemColor(SWT.COLOR_RED);
		assertEquals(red, view.getControl().getBackground());

		final Color background = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		changeWorkspace();
		assertEquals(background, view.getControl().getBackground());
	}

	public void changeWorkspace() throws Exception {
		final TestProject project = new TestProject();
		project.createPackage("pack1");
		project.dispose();
	}

	private IWorkbenchPage getPage() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}
}
