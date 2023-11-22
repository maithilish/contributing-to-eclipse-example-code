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

import java.io.ByteArrayInputStream;

import org.eclipse.contribution.junit.ExcludeTestAction;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import junit.framework.TestCase;

public class ExclusionEditorTest extends TestCase {
	private TestProject testProject;

	@Override
	protected void setUp() throws Exception {
		testProject = new TestProject();
	}

	@Override
	protected void tearDown() throws Exception {
		getPage().closeAllEditors(false);
		testProject.dispose();
	}

	public void testEditorContribution() throws CoreException {
		final String editorId = "org.eclipse.contribution.junit.TestExclusionEditor";
		final IFile file = testProject.getProject().getFile(new Path("test.exclusion"));
		file.create(new ByteArrayInputStream("".getBytes()), true, null);
		/*
		 * IWorkbenchPage.openEditor(file); is no longer available.
		 */
		final FileEditorInput input = new FileEditorInput(file);
		final IEditorPart part = getPage().openEditor(input, editorId);
		assertTrue(findContributedAction(part));
	}

	private boolean findContributedAction(final IEditorPart part) {
		final IActionBars actionBars = part.getEditorSite().getActionBars();
		final IToolBarManager manager = actionBars.getToolBarManager();
		final IContributionItem[] items = manager.getItems();
		for (int i = 0; i < items.length; i++) {
			final IContributionItem item = items[i];
			if (item instanceof ActionContributionItem) {
				final IAction a = ((ActionContributionItem) item).getAction();
				if (a.getClass().equals(ExcludeTestAction.class))
					return true;
			}
		}
		return false;
	}

	private IWorkbenchPage getPage() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}
}