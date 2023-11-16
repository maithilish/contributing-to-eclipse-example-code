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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

public class AutoTestPropertyPage extends PropertyPage {

	private Button autoTest;

	public AutoTestPropertyPage() {
	}

	@Override
	protected Control createContents(final Composite parent) {
		noDefaultAndApplyButton();
		final Control composite = addControl(parent);
		try {
			final boolean hasNature = getProject().hasNature(JUnitPlugin.AUTO_TEST_NATURE);
			autoTest.setSelection(hasNature);
		} catch (final CoreException e) {
			// TODO Error dialog
		}
		return composite;
	}

	@Override
	public boolean performOk() {
		try {
			final JUnitPlugin plugin = JUnitPlugin.getPlugin();
			if (autoTest.getSelection()) {
				plugin.addAutoBuildNature(getProject());
			} else {
				plugin.removeAutoBuildNature(getProject());
			}
		} catch (final CoreException e) {
			ErrorDialog.openError(getShell(), "Error", "Cannot set auto-test property", e.getStatus());
		}
		return true;
	}

	private Control addControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		final GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);
		final Font font = parent.getFont();
		final Label label = new Label(composite, SWT.NONE);
		label.setText("When auto-testing, after every build all " + "tests in this project will be run.");
		label.setFont(font);
		autoTest = new Button(composite, SWT.CHECK);
		autoTest.setText("Auto-test");
		autoTest.setFont(font);
		return composite;
	}

	private IProject getProject() {
		return (IProject) getElement();
	}
}