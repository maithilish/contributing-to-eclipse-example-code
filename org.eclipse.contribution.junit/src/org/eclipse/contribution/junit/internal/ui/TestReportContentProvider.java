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
package org.eclipse.contribution.junit.internal.ui;

import java.util.List;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

class TestReportContentProvider implements IStructuredContentProvider, ITestRunListener {

	private TableViewer viewer;
	private TestResult currentResult;

	public TestReportContentProvider() {
		JUnitPlugin.getPlugin().addTestListener(this);
	}

	@Override
	public void dispose() {
		JUnitPlugin.getPlugin().removeTestListener(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(final Object inputElement) {
		return ((List<TestResult>) inputElement).toArray();
	}

	@Override
	public void inputChanged(final Viewer tableViewer, final Object oldInput, final Object newInput) {
		viewer = (TableViewer) tableViewer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void testsStarted(final IJavaProject project, final int testCount) {
		((List<TestResult>) viewer.getInput()).clear();
		currentResult = null;

		/*
		 * direct viewer.refresh() doesn't clear the view, safely refresh.
		 */
		final Control ctrl = viewer.getControl();
		if (ctrl == null || ctrl.isDisposed()) {
			return;
		}
		ctrl.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if (!viewer.getControl().isDisposed())
					viewer.refresh();
			}
		});
	}

	@Override
	public void testsFinished(final IJavaProject project) {
		addLastResult();
	}

	@Override
	public void testStarted(final IJavaProject project, final String klass, final String method) {
		addLastResult();
		currentResult = new TestResult(project, klass, method, 0, System.currentTimeMillis());
	}

	@Override
	public void testFailed(final IJavaProject project, final String klass, final String method, final String trace) {
		currentResult.testFailed();
	}

	@SuppressWarnings("unchecked")
	private List<TestResult> getTestResults() {
		return (List<TestResult>) viewer.getInput();
	}

	private void addLastResult() {
		if (currentResult != null) {
			currentResult.testFinished();
			getTestResults().add(currentResult);
			final Control ctrl = viewer.getControl();
			if (ctrl == null || ctrl.isDisposed()) {
				return;
			}
			ctrl.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					if (!viewer.getControl().isDisposed())
						viewer.insert(currentResult, -1);
				}
			});
		}
	}
}