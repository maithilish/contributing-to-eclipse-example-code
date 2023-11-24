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
import org.eclipse.contribution.junit.internal.ui.TestReportView;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import junit.framework.TestCase;

public class TestReportTest extends TestCase {

	private TestReportView view;
	private TestProject testProject;
	private IPackageFragment pack;
	private IType type;

	@Override
	protected void setUp() throws Exception {
		testProject = new TestProject();
		testProject.addPluginJarToClasspath("org.junit");
		pack = testProject.createPackage("pack1");
		type = testProject.createType(pack, "FailTest.java",
				"public class FailTest extends junit.framework.TestCase {" + " public void testFailure() {fail();}}");
		testProject.build();
	}

	@Override
	protected void tearDown() throws Exception {
		getPage().closeAllEditors(false);
		testProject.dispose();
	}

	public void testReportView() throws Exception {
		showView();
		JUnitPlugin.getPlugin().run(type);
		final Table table = (Table) view.getViewer().getControl();
		assertEquals(1, table.getItemCount());
		final TableItem item = table.getItem(0);
		assertEquals("testFailure - pack1.FailTest", item.getText(0));
	}

	public void testOpenEditor() throws Exception {
		showView();
		JUnitPlugin.getPlugin().run(type);
		final Table table = (Table) view.getViewer().getControl();
		table.select(0);
		view.handleDefaultSelected();
		final IEditorReference editor = assertEditorOpen();
		assertEditorInput(editor);
	}

	public void testActionInToolbar() throws Exception {
		showView();
		final IActionBars actionBars = view.getViewSite().getActionBars();
		final IToolBarManager manager = actionBars.getToolBarManager();
		assertEquals(1, manager.getItems().length);
	}

	private IEditorReference assertEditorOpen() {
		final IWorkbenchPage page = getPage();
		final IEditorReference[] editors = page.getEditorReferences();
		assertEquals(1, editors.length);
		return editors[0];
	}

	private void assertEditorInput(final IEditorReference editor) throws JavaModelException {
		final IEditorInput expected = new FileEditorInput((IFile) type.getUnderlyingResource());
		final IEditorPart part = editor.getEditor(true);
		assertEquals(expected, part.getEditorInput());
	}

	private void showView() throws PartInitException {
		view = (TestReportView) getPage().showView("org.eclipse.contribution.junit.testReportView");
	}

	private IWorkbenchPage getPage() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}
}