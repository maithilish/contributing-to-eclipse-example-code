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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class RunTestAction implements IObjectActionDelegate {

	private ISelection selection;

	@Override
	public void run(IAction action) {
		if (!(selection instanceof IStructuredSelection)) {
			return;
		}
		IStructuredSelection structured = (IStructuredSelection) selection;
		IType type = (IType) structured.getFirstElement();

		ITestRunListener listener = new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		try {
			JUnitPlugin.getPlugin().run(type);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		JUnitPlugin.getPlugin().removeTestListener(listener);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
	}

	public static class Listener implements ITestRunListener {
		private boolean passed;

		public void testsStarted(int testCount) {
			passed = true;
		}

		public void testsFinished() {
			String message = passed ? "Pass" : "Fail";
			MessageDialog.openInformation(null, "Test Results", message);
		}

		public void testStarted(String klass, String method) {
		}

		public void testFailed(String klass, String method, String trace) {
			passed = false;
		}
	}
}