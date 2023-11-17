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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.eclipse.ui.part.ViewPart;

public class TestReportView extends ViewPart {

	private TableViewer viewer;

	@Override
	public void createPartControl(final Composite parent) {
		final Table table = new Table(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn column = new TableColumn(table, SWT.NONE, 0);
		column.setText("Test");
		column.setWidth(300);
		column.setAlignment(SWT.LEFT);
		column = new TableColumn(table, SWT.NONE, 1);
		column.setText("Time(ms)");
		column.setWidth(100);
		column.setAlignment(SWT.RIGHT);

		viewer = new TableViewer(table);
		viewer.setLabelProvider(new TestReportLabelProvider());
		viewer.setContentProvider(new TestReportContentProvider());
		viewer.setInput(new ArrayList<>());

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				handleDefaultSelected();
			}
		});

		final IActionBars actionBars = getViewSite().getActionBars();
		actionBars.getToolBarManager().add(new GotoTestAction());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void handleDefaultSelected() {
		final IStructuredSelection s = (IStructuredSelection) viewer.getSelection();
		final Object firstElement = s.getFirstElement();
		if (firstElement != null)
			openTest((TestResult) firstElement);
	}

	private void openTest(final TestResult result) {
		try {
			final IType type = result.project.findType(result.klass);
			final IJavaElement testMethod = type.getMethod(result.method, new String[0]);
			final IEditorPart part = JavaUI.openInEditor(type);
			JavaUI.revealInEditor(part, testMethod);
		} catch (final CoreException e) {
			// TODO error handling
		}
	}

	private class GotoTestAction extends Action {
		private GotoTestAction() {
			setText("Go to Test");
			setToolTipText("Go to the Selected Test");
			final ImageDescriptor descriptor = PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(SharedImages.IMG_OPEN_MARKER);
			setImageDescriptor(descriptor);
		}

		@Override
		public void run() {
			handleDefaultSelected();
		}
	}
}