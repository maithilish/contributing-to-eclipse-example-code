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

import java.io.IOException;

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import junit.framework.TestCase;

public class MarkerTest extends TestCase {
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

	public void testErrorMarker() throws Exception {
		JUnitPlugin.getPlugin().run(type);
		final IMarker marker = getFailureMarkers()[0];
		final IMethod method = type.getMethods()[0];

		final int start = method.getSourceRange().getOffset();
		assertEquals(start, marker.getAttribute(IMarker.CHAR_START, 0));

		final int end = start + method.getSourceRange().getLength();
		assertEquals(end, marker.getAttribute(IMarker.CHAR_END, 0));

		assertTrue(marker.isSubtypeOf(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER));
		assertEquals(IMarker.SEVERITY_ERROR, marker.getAttribute(IMarker.SEVERITY, -1));
	}

	public void testMarkerClearing() throws CoreException, IOException {
		JUnitPlugin.getPlugin().run(type);
		JUnitPlugin.getPlugin().run(type);
		final IMarker[] markers = getFailureMarkers();
		assertEquals(1, markers.length);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE);
	}
}