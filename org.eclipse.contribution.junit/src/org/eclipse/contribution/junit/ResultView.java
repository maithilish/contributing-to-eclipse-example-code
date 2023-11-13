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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class ResultView extends ViewPart {

	private ITestRunListener listener;
	private Label control;

	public void createPartControl(Composite parent) {
		listener = new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		control = new Label(parent, SWT.NONE);
	}

	public void setFocus() {
		control.setFocus();
	}

	public void dispose() {
		if (listener != null) {
			JUnitPlugin.getPlugin().removeTestListener(listener);
		}
		listener = null;
	}

	public Control getControl() {
		return control;
	}

	public ITestRunListener getListener() {
		return listener;
	}

	private class Listener implements ITestRunListener {
		private boolean success;

		public void testsStarted(int testCount) {
			success = true;
		}

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

		public void testFailed(String klass, String method, String trace) {
			Color red = control.getDisplay().getSystemColor(SWT.COLOR_RED);
			control.setBackground(red);
			success = false;
		}

	}
}