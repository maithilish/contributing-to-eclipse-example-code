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

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

public class ResultView extends ViewPart {

	private ITestRunListener listener;
	private Label control;
	private Action rerunAction;
	private DirtyListener dirtyListener;

	@Override
	public void createPartControl(final Composite parent) {
		listener = new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		dirtyListener = new DirtyListener();
		/*
		 * PRE_AUTO_BUILD is deprecated, use POST_CHANGE
		 */
		JavaCore.addElementChangedListener(dirtyListener, ElementChangedEvent.POST_CHANGE);
		control = new Label(parent, SWT.NONE);
		createContextMenu();
		rerunAction = new RerunTestAction();
	}

	@Override
	public void setFocus() {
		control.setFocus();
	}

	@Override
	public void dispose() {
		if (listener != null) {
			JUnitPlugin.getPlugin().removeTestListener(listener);
		}
		if (dirtyListener != null) {
			JavaCore.removeElementChangedListener(dirtyListener);
		}
		listener = null;
		dirtyListener = null;
	}

	private void createContextMenu() {
		final MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		final Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);
		getSite().registerContextMenu(menuManager, getSite().getSelectionProvider());
	}

	private void fillContextMenu(final IMenuManager menu) {
		menu.add(rerunAction);
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end"));
	}

	public Control getControl() {
		return control;
	}

	public ITestRunListener getListener() {
		return listener;
	}

	private class Listener implements ITestRunListener {
		private boolean success;

		@Override
		public void testsStarted(final IJavaProject project, final int testCount) {
			success = true;
		}

		@Override
		public void testsFinished(final IJavaProject project) {
			if (success) {
				changeColor(success);
			}
		}

		@Override
		public void testStarted(final IJavaProject project, final String klass, final String method) {
			// TODO Auto-generated method stub
		}

		@Override
		public void testFailed(final IJavaProject project, final String klass, final String method,
				final String trace) {
			success = false;
			changeColor(success);
		}

	}

	private void changeColor(final boolean success) {
		final Display display = getSite().getShell().getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (control.isDisposed()) {
					return;
				}
				if (success) {
					final Display display = control.getDisplay();
					final Color green = display.getSystemColor(SWT.COLOR_GREEN);
					control.setBackground(green);
				} else {
					final Color red = display.getSystemColor(SWT.COLOR_RED);
					control.setBackground(red);
				}
			}
		});
	}

	private class RerunTestAction extends Action {
		private RerunTestAction() {
			setText("Re-run");
		}

		@Override
		public void run() {
			rerunTest();
		}
	}

	private void rerunTest() {
		// TODO not implemented yet
		System.out.println("rerun");
	}

	private class DirtyListener implements IElementChangedListener {
		@Override
		public void elementChanged(final ElementChangedEvent event) {
			processDelta(event.getDelta());
		}

		private boolean processDelta(final IJavaElementDelta delta) {
			final int kind = delta.getKind();
			final int type = delta.getElement().getElementType();

			switch (type) {
			case IJavaElement.JAVA_MODEL:
			case IJavaElement.JAVA_PROJECT:
			case IJavaElement.PACKAGE_FRAGMENT_ROOT:
			case IJavaElement.PACKAGE_FRAGMENT:
				if (kind == IJavaElementDelta.ADDED || kind == IJavaElementDelta.REMOVED)
					return testResultDirty();
				break;
			case IJavaElement.COMPILATION_UNIT:
				final ICompilationUnit unit = (ICompilationUnit) delta.getElement();
				if (unit.isWorkingCopy()) {
					return true;
				}
				return testResultDirty();
			default:
				return testResultDirty();
			}

			final IJavaElementDelta[] affectedChildren = delta.getAffectedChildren();
			for (int i = 0; i < affectedChildren.length; i++) {
				if (!processDelta(affectedChildren[i])) {
					return false;
				}
			}
			return true;
		}
	}

	private boolean testResultDirty() {
		final Display display = getSite().getShell().getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!control.isDisposed()) {
					final Color background = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
					control.setBackground(background);
				}
			}
		});
		return false;
	}
}