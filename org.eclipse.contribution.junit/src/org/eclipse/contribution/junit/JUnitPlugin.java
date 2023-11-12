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

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.IType;

public class JUnitPlugin extends Plugin {

	private static JUnitPlugin instance;
	private List<ITestRunListener> listeners = new ArrayList<>();

	/**
	 * Note: IPluginDescriptor is removed in Eclipse 4.12.
	 */
	public JUnitPlugin() {
		super();
		instance = this;
	}

	public static JUnitPlugin getPlugin() {
		if (instance == null) {
			instance = new JUnitPlugin();
		}
		return instance;
	}

	public String getPluginName() {
		return getBundle().getSymbolicName();
	}

	public URL getInstallURL() {
		return getBundle().getEntry("/");
	}

	public void run(IType type) throws CoreException {
		new TestRunner().run(type);
	}

	public List<ITestRunListener> getListeners() {
		return listeners;
	}

	public void addTestListener(ITestRunListener listener) {
		getListeners().add(listener);
	}

	public void removeTestListener(ITestRunListener listener) {
		getListeners().remove(listener);
	}

	public void fireTestsStarted(int count) {
		for (Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			ITestRunListener each = (ITestRunListener) all.next();
			each.testsStarted(count);
		}
	}

	public void fireTestsFinished() {
		for (Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			ITestRunListener each = (ITestRunListener) all.next();
			each.testsFinished();
		}
	}

	public void fireTestStarted(String klass, String methodName) {
		for (Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			ITestRunListener each = (ITestRunListener) all.next();
			each.testStarted(klass, methodName);
		}
	}

	public void fireTestFailed(String klass, String method, String trace) {
		for (Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			ITestRunListener each = (ITestRunListener) all.next();
			each.testFailed(klass, method, trace);
		}
	}
}