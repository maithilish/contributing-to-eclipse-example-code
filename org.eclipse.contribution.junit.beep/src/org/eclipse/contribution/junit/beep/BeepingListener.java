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
package org.eclipse.contribution.junit.beep;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Display;

public class BeepingListener implements ITestRunListener {

	@Override
	public void testFailed(final IJavaProject p, final String klass, final String m, final String t) {
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed())
					display.beep();
			}
		});
	}

	@Override
	public void testsStarted(final IJavaProject p, final int count) {
	}

	@Override
	public void testsFinished(final IJavaProject p) {
	}

	@Override
	public void testStarted(final IJavaProject p, final String klass, final String method) {
	}
}