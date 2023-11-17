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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class RerunMarkerResolutionGenerator implements IMarkerResolutionGenerator {

	public RerunMarkerResolutionGenerator() {
	}

	@Override
	public IMarkerResolution[] getResolutions(final IMarker marker) {
		final IMarkerResolution resolution = new IMarkerResolution() {
			@Override
			public String getLabel() {
				return "Re-run test";
			}

			@Override
			public void run(final IMarker marker) {
				ConsoleLogger.log(this, "rerun tests");
				if (!save()) {
					return;
				}
				if (!build()) {
					return;
				}
				final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
					@Override
					public void run(final IProgressMonitor monitor) throws CoreException {
						final IType type = findTest(marker);
						JUnitPlugin.getPlugin().run(type);
					}
				};
				try {
					ResourcesPlugin.getWorkspace().run(runnable, null);
				} catch (final CoreException e) {
					// TODO error handling
				}
			}
		};
		return new IMarkerResolution[] { resolution };
	}

	protected boolean build() {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException {
					AutoTestBuilder.setEnabled(false);
					try {
						ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
					} catch (final CoreException e) {
						throw new InvocationTargetException(e);
					} finally {
						AutoTestBuilder.setEnabled(true);
					}
				}
			});
		} catch (final InterruptedException e) {
			return false;
		} catch (final InvocationTargetException e) {
			@SuppressWarnings("unused")
			final Throwable target = e.getTargetException();
			// TODO inform about target exception
			return false;
		}
		return true;
	}

	protected IType findTest(final IMarker m) {
		final IResource resource = m.getResource();
		final ICompilationUnit cu = (ICompilationUnit) JavaCore.create(resource);
		return cu.findPrimaryType();
	}

	private boolean save() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		return workbench.saveAllEditors(false);
	}

	private Shell getShell() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getShell();
	}
}