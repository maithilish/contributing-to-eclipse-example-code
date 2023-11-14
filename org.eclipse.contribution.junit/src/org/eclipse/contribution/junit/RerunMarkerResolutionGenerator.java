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

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

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
				// TODO implement re-run
				System.out.println("rerun test");
			}
		};
		return new IMarkerResolution[] { resolution };
	}

}