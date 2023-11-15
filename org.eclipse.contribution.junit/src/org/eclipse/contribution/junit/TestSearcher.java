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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class TestSearcher {

	public IType[] findAll(final IJavaProject project, final IProgressMonitor pm) throws JavaModelException {
		final IType[] candidates = getAllTestCaseSubclasses(project, pm);
		return collectTestsInProject(candidates, project);
	}

	private IType[] getAllTestCaseSubclasses(final IJavaProject project, final IProgressMonitor pm)
			throws JavaModelException {
		final IType testCase = project.findType("junit.framework.TestCase");
		if (testCase == null) {
			return new IType[0];
		}
		final ITypeHierarchy hierarchy = testCase.newTypeHierarchy(project, pm);
		return hierarchy.getAllSubtypes(testCase);
	}

	private IType[] collectTestsInProject(final IType[] candidates, final IJavaProject project) {
		final List<IType> result = new ArrayList<>();
		for (int i = 0; i < candidates.length; i++) {
			try {
				if (isTestInProject(candidates[i], project)) {
					result.add(candidates[i]);
				}
			} catch (final JavaModelException e) {
				// Fall through
			}
		}
		return result.toArray(IType[]::new);
	}

	private boolean isTestInProject(final IType type, final IJavaProject project) throws JavaModelException {
		final IResource resource = type.getUnderlyingResource();
		if (resource == null) {
			return false;
		}
		if (!resource.getProject().equals(project.getProject())) {
			return false;
		}
		return !Flags.isAbstract(type.getFlags());
	}
}