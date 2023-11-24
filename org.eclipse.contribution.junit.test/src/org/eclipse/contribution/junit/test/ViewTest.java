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

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.ui.ResultView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import junit.framework.TestCase;

public class ViewTest extends TestCase {

	private ResultView view;

	public void testShowHide() throws PartInitException {
		IViewPart view = getPage().showView("org.eclipse.contribution.junit.resultView");
		getPage().hideView(view);
	}

	public void testViewListener() throws PartInitException {
		int count = JUnitPlugin.getPlugin().getListeners().size();
		showView();
		try {
			assertEquals(count + 1, JUnitPlugin.getPlugin().getListeners().size());
		} finally {
			hideView();
		}
		assertEquals(count, JUnitPlugin.getPlugin().getListeners().size());
	}

	public void showView() throws PartInitException {
		view = (ResultView) getPage().showView("org.eclipse.contribution.junit.resultView");
	}

	public void hideView() {
		getPage().hideView(view);
	}

	private IWorkbenchPage getPage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}
}
