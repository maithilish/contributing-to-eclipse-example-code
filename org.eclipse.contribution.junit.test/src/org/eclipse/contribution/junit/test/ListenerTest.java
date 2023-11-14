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

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.JUnitPlugin;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import junit.framework.TestCase;

public class ListenerTest extends TestCase {
	private TestProject project;

	@Override
	protected void setUp() throws Exception {
		project = new TestProject();
	}

	@Override
	protected void tearDown() throws Exception {
		project.dispose();
	}

	public void testFailure() throws Exception {

		project.addPluginJarToClasspath("org.junit");
		IPackageFragment pack = project.createPackage("pack1");
		IType type = project.createType(pack, "FailTest.java",
				"public class FailTest extends junit.framework.TestCase {" + "public void testFailure() {fail();}}");
		project.build();

		Listener listener = new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		JUnitPlugin.getPlugin().run(type);
		assertEquals("testFailure pack1.FailTest", listener.testFailed);
	}

	public static class Listener implements ITestRunListener {
		String testFailed;

		public void testFailed(String klass, String method, String trace) {
			testFailed = method + " " + klass;
		}

		public void testsStarted(int testCount) {
		}

		public void testsFinished() {
		}

		public void testStarted(String klass, String method) {
		}
	}
}