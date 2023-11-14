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

	@Override
	public void createPartControl(Composite parent) {
		listener = new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
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
		listener = null;
	}

	private void createContextMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);
		getSite().registerContextMenu(menuManager, getSite().getSelectionProvider());
	}

	private void fillContextMenu(IMenuManager menu) {
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
		public void testsStarted(int testCount) {
			success = true;
		}

		@Override
		public void testsFinished() {
			if (success) {
				Display display = control.getDisplay();
				Color green = display.getSystemColor(SWT.COLOR_GREEN);
				control.setBackground(green);
			}
		}

		@Override
		public void testStarted(String klass, String method) {
			// TODO Auto-generated method stub
		}

		@Override
		public void testFailed(String klass, String method, String trace) {
			Color red = control.getDisplay().getSystemColor(SWT.COLOR_RED);
			control.setBackground(red);
			success = false;
		}

	}

	private class RerunTestAction extends Action {
		private RerunTestAction() {
			setText("Re-run");
		}

		public void run() {
			rerunTest();
		}
	}

	private void rerunTest() {
		// TODO not implemented yet
		System.out.println("rerun");
	}
}