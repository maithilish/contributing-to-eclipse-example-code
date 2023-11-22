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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class AutoTestBuilder extends IncrementalProjectBuilder {

	private static boolean trace = false;
	private static boolean enabled = true;

	public AutoTestBuilder() {
	}

	@Override
	protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor pm)
			throws CoreException {

		ConsoleLogger.log(this, "auto build the project");
		if (hasBuildErrors() || !AutoTestBuilder.enabled) {
			ConsoleLogger.log(this, "no auto build, project has errors or auto build not enabled");
			return null;
		}
		final IJavaProject javaProject = JavaCore.create(getProject());
		IType[] types = new TestSearcher().findAll(javaProject, pm);
		types = exclude(types);

		if (AutoTestBuilder.trace) {
			printTestTypes(types);
		}

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

	public static void setEnabled(final boolean isEnabled) {
		AutoTestBuilder.enabled = isEnabled;
	}

	static {
		final String value = Platform.getDebugOption("org.eclipse.contribution.junit/trace/testfinding");
		if (value != null && value.equalsIgnoreCase("true")) {
			AutoTestBuilder.trace = true;
		}
	}

	private static void printTestTypes(final IType[] tests) {
		System.out.println("Auto Test: ");
		for (int i = 0; i < tests.length; i++) {
			System.out.println("\t" + tests[i].getFullyQualifiedName());
		}
	}

	private IType[] exclude(final IType[] types) {
		try {
			final Set<String> exclusions = readExclusions(getProject().getFile(new Path("test.exclusion")));
			final List<IType> result = new ArrayList<>(types.length);
			for (int i = 0; i < types.length; i++) {
				final IType type = types[i];
				final String typeName = type.getFullyQualifiedName();
				if (!exclusions.contains(typeName)) {
					result.add(type);
				}
			}
			return (result.toArray(new IType[result.size()]));
		} catch (final Exception e) {
			// fall through
		}
		return types;
	}

	private Set<String> readExclusions(final IFile file) throws IOException, CoreException {
		final Set<String> result = new HashSet<>();
		final BufferedReader br = new BufferedReader(new InputStreamReader(file.getContents()));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0)
					result.add(line);
			}
		} finally {
			br.close();
		}
		return result;
	}
}