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

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class AutoTestBuilder extends IncrementalProjectBuilder {

	public AutoTestBuilder() {
	}

	@Override
	protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor pm)
			throws CoreException {
		ConsoleLogger.log(this, "auto build the project");
		if (hasBuildErrors()) {
			ConsoleLogger.log(this, "project has errors, not auto build");
			return null;
		}
		final IJavaProject javaProject = JavaCore.create(getProject());
		final IType[] types = new TestSearcher().findAll(javaProject, pm);
		JUnitPlugin.getPlugin().run(types, javaProject);
		ConsoleLogger.log(this, "auto build finished");
		return null;
	}

	public boolean hasBuildErrors() throws CoreException {
		final IMarker[] markers = getProject().findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, false,
				IResource.DEPTH_INFINITE);
		for (int i = 0; i < markers.length; i++) {
			final IMarker marker = markers[i];
			if (marker.getAttribute(IMarker.SEVERITY, 0) == IMarker.SEVERITY_ERROR) {
				return true;
			}
		}
		return false;
	}
}