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

import org.eclipse.contribution.junit.TestSearcher;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import junit.framework.TestCase;

public class SearchTest extends TestCase {

	private TestProject testProject;
	private IPackageFragment pack;

	@Override
	protected void setUp() throws Exception {
		testProject = new TestProject();
		testProject.addPluginJarToClasspath("org.junit");
		pack = testProject.createPackage("pack1");
	}

	@Override
	protected void tearDown() throws Exception {
		testProject.dispose();
	}

	public void testSearch() throws Exception {

		testProject.createType(pack, "AbstractTest.java",
				"public abstract class AbstractTest " + " extends junit.framework.TestCase { }");

		testProject.createType(pack, "NotATest.java", "public class NotATest { }");

		final IType type = testProject.createType(pack, "ATest.java",
				"public class ATest extends junit.framework.TestCase { }");

		final TestSearcher searcher = new TestSearcher();
		final IType[] tests = searcher.findAll(testProject.getJavaProject(), null);
		assertEquals(1, tests.length);
		assertEquals(type, tests[0]);
	}
}
