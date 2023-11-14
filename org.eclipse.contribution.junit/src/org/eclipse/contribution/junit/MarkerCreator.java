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

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import junit.runner.BaseTestRunner;

public class MarkerCreator implements ITestRunListener {

	private final IJavaProject project;

	public MarkerCreator(final IJavaProject project) {
		this.project = project;
	}

	@Override
	public void testsStarted(final IJavaProject project, final int testCount) {
		try {
			project.getProject().deleteMarkers("org.eclipse.contribution.junit.failure", false,
					IResource.DEPTH_INFINITE);
		} catch (final CoreException e) {
			// TODO Log later
		}
	}

	@Override
	public void testsFinished(final IJavaProject project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void testStarted(final IJavaProject project, final String klass, final String method) {
		// TODO Auto-generated method stub

	}

	@Override
	public void testFailed(final IJavaProject testProject, final String klass, final String method,
			final String trace) {
		if (!project.equals(testProject)) {
			return;
		}
		IType type = null;
		try {
			type = project.findType(klass);
		} catch (final JavaModelException e) { // Fall through
		}
		if (type == null) {
			return; // TODO: Log later
		}
		final IType finalType = type;
		try {
			final IResource resource = type.getUnderlyingResource();
			final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
				@Override
				public void run(final IProgressMonitor monitor) throws CoreException {
					final IMarker marker = resource.createMarker("org.eclipse.contribution.junit.failure");
					final IMethod testMethod = finalType.getMethod(method, new String[0]);
					setMarkerAttributes(marker, testMethod, trace);
				}
			};
			resource.getWorkspace().run(runnable, null);
		} catch (final CoreException e) {
			// TODO Log later
		}
	}

	private void setMarkerAttributes(final IMarker marker, final IMethod testMethod, final String trace)
			throws JavaModelException, CoreException {
		final ISourceRange range = testMethod.getSourceRange();
		final Map<String, Object> map = new HashMap<>(4);
		map.put(IMarker.CHAR_START, Integer.valueOf(range.getOffset()));
		map.put(IMarker.CHAR_END, Integer.valueOf(range.getOffset() + range.getLength()));
		map.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_ERROR));
		map.put(IMarker.MESSAGE, extractMessage(trace));
		map.put("trace", trace);
		marker.setAttributes(map);
	}

	private String extractMessage(final String trace) {
		final String filteredTrace = BaseTestRunner.getFilteredTrace(trace);
		final BufferedReader br = new BufferedReader(new StringReader(filteredTrace));
		String line, message = trace;
		try {
			if ((line = br.readLine()) != null) {
				message = line;
				if ((line = br.readLine()) != null)
					message += " - " + line;
			}
			return message.replace('\t', ' ');
		} catch (final Exception IOException) {
		}
		return message;
	}
}