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
package org.eclipse.contribution.junit.internal.core;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class AutoTestNature implements IProjectNature {
	IProject project;

	public AutoTestNature() {
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(final IProject project) {
		this.project = project;
	}

	@Override
	public void configure() throws CoreException {
		final IProjectDescription description = getProject().getDescription();
		final ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(JUnitPlugin.AUTO_TEST_BUILDER)) {
				return;
			}
		}
		final ICommand command = description.newCommand();
		command.setBuilderName(JUnitPlugin.AUTO_TEST_BUILDER);
		final ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		newCommands[newCommands.length - 1] = command;
		description.setBuildSpec(newCommands);
		getProject().setDescription(description, null);
	}

	@Override
	public void deconfigure() throws CoreException {
		final IProjectDescription description = getProject().getDescription();
		final ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(JUnitPlugin.AUTO_TEST_BUILDER)) {
				final ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
				description.setBuildSpec(newCommands);
				getProject().setDescription(description, null);
				return;
			}
		}
	}
}