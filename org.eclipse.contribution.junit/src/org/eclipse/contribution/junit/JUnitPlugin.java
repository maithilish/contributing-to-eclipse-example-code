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
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

public class JUnitPlugin extends Plugin {

	private static final String listenerId = "org.eclipse.contribution.junit.listeners";

	private static JUnitPlugin instance;
	private List<ITestRunListener> listeners;

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

	public List<ITestRunListener> getListeners() {
		if (listeners == null) {
			listeners = computeListeners();
		}
		return listeners;
	}

	public void addTestListener(final ITestRunListener listener) {
		getListeners().add(listener);
	}

	public void removeTestListener(final ITestRunListener listener) {
		getListeners().remove(listener);
	}

	private List<ITestRunListener> computeListeners() {
		// get extension points and extensions configured into extension points
		final IExtensionRegistry registry = RegistryFactory.getRegistry();
		final IExtensionPoint extensionPoint = registry.getExtensionPoint(listenerId);
		final IExtension[] extensions = extensionPoint.getExtensions();

		// create listeners for each extension configured
		final ArrayList<ITestRunListener> results = new ArrayList<>();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				try {
					// create listener from class attribute in extension listener element
					final IConfigurationElement configurationElement = elements[j];
					final Object listener = configurationElement.createExecutableExtension("class");
					if (listener instanceof ITestRunListener) {
						results.add((ITestRunListener) listener);
					}
				} catch (final CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	public void fireTestsStarted(final IJavaProject project, final int count) {
		for (final Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each = all.next();

			final ISafeRunnable runnable = new ISafeRunnable() {
				@Override
				public void run() throws Exception {
					each.testsStarted(project, count);
				}
			};
			// Platform.run() is deprecated.
			SafeRunner.run(runnable);
		}
	}

	public void fireTestsFinished(final IJavaProject project) {
		for (final Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each = all.next();

			final ISafeRunnable runnable = new ISafeRunnable() {
				@Override
				public void run() throws Exception {
					each.testsFinished(project);
				}
			};
			// Platform.run() is deprecated.
			SafeRunner.run(runnable);
		}
	}

	public void fireTestStarted(final IJavaProject project, final String klass, final String methodName) {
		for (final Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each = all.next();

			final ISafeRunnable runnable = new ISafeRunnable() {
				@Override
				public void run() throws Exception {
					each.testStarted(project, klass, methodName);
				}
			};
			// Platform.run() is deprecated.
			SafeRunner.run(runnable);
		}
	}

	public void fireTestFailed(final IJavaProject project, final String klass, final String method,
			final String trace) {
		for (final Iterator<ITestRunListener> all = getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each = all.next();

			final ISafeRunnable runnable = new ISafeRunnable() {
				@Override
				public void run() throws Exception {
					each.testFailed(project, klass, method, trace);
				}
			};
			// Platform.run() is deprecated.
			SafeRunner.run(runnable);
		}
	}

	public void run(final IType type) throws CoreException {
		run(new IType[] { type }, type.getJavaProject());
	}

	public void run(final IType[] classes, final IJavaProject project) throws CoreException {
		if (classes.length == 0) {
			return;
		}
		final ITestRunListener listener = new MarkerCreator(project);
		addTestListener(listener);
		try {
			new TestRunner(project).run(classes);
		} finally {
			removeTestListener(listener);
		}
	}
}