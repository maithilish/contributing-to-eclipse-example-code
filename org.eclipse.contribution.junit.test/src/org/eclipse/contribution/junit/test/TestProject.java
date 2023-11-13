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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.Bundle;

public class TestProject {
	private IProject project;
	private IJavaProject javaProject;
	private IPackageFragmentRoot sourceFolder;

	public TestProject() throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject("TestProject");
		project.create(null);
		project.open(null);
		javaProject = JavaCore.create(project);

		IFolder binFolder = createBinFolder();

		setJavaNature();
		javaProject.setRawClasspath(new IClasspathEntry[0], null);

		createOutputFolder(binFolder);
		addSystemLibraries();
	}

	public IProject getProject() {
		return project;
	}

	public IJavaProject getJavaProject() {
		return javaProject;
	}

	/**
	 * The addJar(...) is replaced with new method addPluginJarToClasspath(...). In
	 * the distributed plugin the plugin code is no longer packaged as jar instead
	 * they are in expanded form.
	 * 
	 * @param plugin
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JavaModelException
	 */
	public void addPluginJarToClasspath(String plugin) throws MalformedURLException, IOException, JavaModelException {
		Bundle bundle = Platform.getBundle(plugin);
		Optional<File> bundlePath = FileLocator.getBundleFileLocation(bundle);
		if (bundlePath.isPresent()) {
			Path bundleAbsPath = new Path(bundlePath.get().getAbsolutePath());
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newLibraryEntry(bundleAbsPath, null, null);
			javaProject.setRawClasspath(newEntries, null);
		}
	}

	public IPackageFragment createPackage(String name) throws CoreException {
		if (sourceFolder == null) {
			sourceFolder = createSourceFolder();
		}
		return sourceFolder.createPackageFragment(name, false, null);
	}

	public IType createType(IPackageFragment pack, String cuName, String source) throws JavaModelException {
		StringBuffer buf = new StringBuffer();
		buf.append("package " + pack.getElementName() + ";\n");
		buf.append("\n");
		buf.append(source);
		ICompilationUnit cu = pack.createCompilationUnit(cuName, buf.toString(), false, null);
		return cu.getTypes()[0];
	}

	public void dispose() throws CoreException {
		waitForIndexer();
		project.delete(true, true, null);
	}

	/**
	 * The example code doesn't explicitly build the test project. However, the
	 * SocketTestRunner, in some run, throws ClassNotFoundException for the Test
	 * class as project is not yet auto built. To avoid the error, we introduced
	 * explicit build which is called after test type is added to the test project.
	 * 
	 * @throws CoreException
	 */
	public void build() throws CoreException {
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
	}

	private IFolder createBinFolder() throws CoreException {
		IFolder binFolder = project.getFolder("bin");
		binFolder.create(false, true, null);
		return binFolder;
	}

	private void setJavaNature() throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);
	}

	private void createOutputFolder(IFolder binFolder) throws JavaModelException {
		IPath outputLocation = binFolder.getFullPath();
		javaProject.setOutputLocation(outputLocation, null);
	}

	private IPackageFragmentRoot createSourceFolder() throws CoreException {
		IFolder folder = project.getFolder("src");
		folder.create(false, true, null);
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);

		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
		javaProject.setRawClasspath(newEntries, null);
		return root;
	}

	private void addSystemLibraries() throws JavaModelException {
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaRuntime.getDefaultJREContainerEntry();
		javaProject.setRawClasspath(newEntries, null);
	}

	private void waitForIndexer() throws JavaModelException {
		char[] packageName = null;
		char[] typeName = null;
		int searchFor = IJavaSearchConstants.CLASS;
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[0]);
		int waitingPolicy = IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH;
		int packageMatchRule = SearchPattern.R_EXACT_MATCH;
		int typeMatchRule = SearchPattern.R_EXACT_MATCH;

		TypeNameRequestor typeNameRequestor = new TypeNameRequestor() {
			@Override
			public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName,
					char[][] enclosingTypeNames, String path) {
				super.acceptType(modifiers, packageName, simpleTypeName, enclosingTypeNames, path);
			}
		};

		IProgressMonitor progressMonitor = null;
		new SearchEngine().searchAllTypeNames(packageName, packageMatchRule, typeName, typeMatchRule, searchFor, scope,
				typeNameRequestor, waitingPolicy, progressMonitor);
	}

	/**
	 * old method uses deprecated API
	 * 
	 * @throws JavaModelException
	 */
	// private void waitForIndexerOld() throws JavaModelException {
	// IWorkspace workspace = ResourcesPlugin.getWorkspace();
	// char[] packageName = null;
	// char[] typeName = null;
	// int matchMode = IJavaSearchConstants.EXACT_MATCH;
	// boolean isCaseSensitive = IJavaSearchConstants.CASE_SENSITIVE;
	// int searchFor = IJavaSearchConstants.CLASS;
	// IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new
	// IJavaElement[0]);
	// ITypeNameRequestor nameRequestor = new ITypeNameRequestor() {
	// public void acceptClass(char[] packageName, char[] simpleTypeName, char[][]
	// enclosingTypeNames,
	// String path) {
	// }
	//
	// public void acceptInterface(char[] packageName, char[] simpleTypeName,
	// char[][] enclosingTypeNames,
	// String path) {
	// }
	// };
	// int waitingPolicy = IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH;
	// IProgressMonitor progressMonitor = null;
	//
	// new SearchEngine().searchAllTypeNames(workspace, null, null, matchMode,
	// isCaseSensitive, searchFor, scope,
	// nameRequestor, waitingPolicy, null);
	// }
}