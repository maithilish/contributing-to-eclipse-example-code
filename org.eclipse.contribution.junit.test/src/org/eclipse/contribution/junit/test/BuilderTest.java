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

import org.eclipse.contribution.junit.JUnitPlugin;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import junit.framework.TestCase;

public class BuilderTest extends TestCase {

	private TestProject testProject;

	@Override
	protected void setUp() throws Exception {
		testProject = new TestProject();
		testProject.addPluginJarToClasspath("org.junit");
	}

	@Override
	protected void tearDown() throws Exception {
		testProject.dispose();
	}

	public void testNatureAddAndRemove() throws CoreException {
		final IProject project = testProject.getProject();
		JUnitPlugin.getPlugin().addAutoBuildNature(project);
		assertTrue(project.hasNature(JUnitPlugin.AUTO_TEST_NATURE));
		final ICommand[] commands = project.getDescription().getBuildSpec();
		boolean found = false;
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(JUnitPlugin.AUTO_TEST_BUILDER)) {
				found = true;
			}
		}
		assertTrue(found);
		JUnitPlugin.getPlugin().removeAutoBuildNature(project);
		assertFalse(project.hasNature(JUnitPlugin.AUTO_TEST_NATURE));
	}

	public void testAutoTesting() throws Exception {
		final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			@Override
			public void run(final IProgressMonitor monitor) throws CoreException {
				final IProject project = testProject.getProject();
				JUnitPlugin.getPlugin().addAutoBuildNature(project);
				final IPackageFragment pack = testProject.createPackage("pack1");
				@SuppressWarnings("unused")
				final IType type = testProject.createType(pack, "FailTest.java",
						"public class FailTest extends junit.framework.TestCase{"
								+ "public void testFailure() {fail();}}");
				/*
				 * The explicit build() is to compile the test project. AutoTestBuildier.build()
				 * finds all tests and run them and create error markers.
				 */
				testProject.build();
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);
		final IMarker[] markers = getFailureMarkers();
		assertEquals(1, markers.length);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE);
	}
}
