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
package org.eclipse.contribution.junit.test;

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.ui.RerunMarkerResolutionGenerator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IMarkerResolution;

import junit.framework.TestCase;

public class MarkerResolutionTest extends TestCase {
	private TestProject testProject;
	private IPackageFragment pack;
	private IType type;

	@Override
	protected void setUp() throws Exception {
		testProject = new TestProject();
		testProject.addPluginJarToClasspath("org.junit");
		pack = testProject.createPackage("pack1");
		type = testProject.createType(pack, "FailTest.java",
				"public class FailTest extends junit.framework.TestCase {" + "public void testFailure() {fail();}}");
		testProject.build();
	}

	@Override
	protected void tearDown() throws Exception {
		testProject.dispose();
	}

	public void testMarkerResolution() throws Exception {
		JUnitPlugin.getPlugin().run(type);
		IMarker[] markers = getFailureMarkers();
		assertEquals(1, markers.length);

		fixProblem();

		final RerunMarkerResolutionGenerator generator = new RerunMarkerResolutionGenerator();
		final IMarkerResolution[] resolutions = generator.getResolutions(markers[0]);
		resolutions[0].run(markers[0]);

		markers = getFailureMarkers();
		assertEquals(0, markers.length);
	}

	private void fixProblem() throws JavaModelException {
		final IMethod testMethod = type.getMethod("testFailure", new String[0]);
		testMethod.delete(true, null);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE);
	}
}