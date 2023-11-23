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

import org.eclipse.osgi.util.NLS;

public class JUnitMessages extends NLS {
	private static final String BUNDLE_NAME = JUnitMessages.class.getPackageName() + ".JUnitMessages"; //$NON-NLS-1$
	public static String ExcludeTestAction_0;
	public static String ExcludeTestAction_1;
	public static String ExcludeTestAction_3;
	public static String ExcludeTestAction_4;
	public static String TestReportView_0;
	public static String TestReportView_1;
	public static String TestReportView_3;
	public static String TestReportView_4;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, JUnitMessages.class);
	}

	private JUnitMessages() {
	}
}
