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
package org.eclipse.contribution.junit.internal.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.core.TestSearcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class ExcludeTestAction extends Action {
	private ITextEditor editor;

	public ExcludeTestAction() {
		setText(JUnitMessages.ExcludeTestAction_0);
		setToolTipText(JUnitMessages.ExcludeTestAction_1);
		setImageDescriptor(createImage("icons/test.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		final IType type = selectTest();
		if (type != null) {
			appendTest(type.getFullyQualifiedName());
		}
	}

	public void setActiveEditor(final ITextEditor target) {
		editor = target;
	}

	private ImageDescriptor createImage(final String path) {
		final URL url = JUnitPlugin.getPlugin().getInstallURL();
		ImageDescriptor descriptor = null;
		try {
			descriptor = ImageDescriptor.createFromURL(new URL(url, path));
		} catch (final MalformedURLException e) {
			descriptor = ImageDescriptor.getMissingImageDescriptor();
		}
		return descriptor;
	}

	private IType selectTest() {
		final IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			final IFile file = ((IFileEditorInput) input).getFile();
			final IJavaProject jproject = JavaCore.create(file.getProject());
			try {
				final IType[] types = new TestSearcher().findAll(jproject, new NullProgressMonitor());
				return showDialog(types);
			} catch (final JavaModelException e) {
			}
		}
		return null;
	}

	private IType showDialog(final IType[] types) {
		final ElementListSelectionDialog dialog = new ElementListSelectionDialog(editor.getSite().getShell(),
				new JavaElementLabelProvider());
		dialog.setTitle(JUnitMessages.ExcludeTestAction_3);
		dialog.setMessage(JUnitMessages.ExcludeTestAction_4);
		dialog.setElements(types);
		if (dialog.open() == Window.OK) {
			return (IType) dialog.getFirstResult();
		}
		return null;
	}

	private void appendTest(final String testName) {
		final IDocumentProvider provider = editor.getDocumentProvider();
		final IEditorInput input = editor.getEditorInput();
		final IDocument document = provider.getDocument(input);
		if (document == null) {
			return;
		}
		try {
			document.replace(document.getLength(), 0, testName + "\n"); //$NON-NLS-1$
		} catch (final BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}