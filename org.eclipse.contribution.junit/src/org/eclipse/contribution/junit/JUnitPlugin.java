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

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.IType;

public class JUnitPlugin extends Plugin {

	private static JUnitPlugin instance;

	/**
	 * Note: IPluginDescriptor is removed in Eclipse 4.12.
	 */
	public JUnitPlugin() {
		super();
		instance = this;
	}

	public static JUnitPlugin getPlugin() {
		return instance;
	}

	public void run(IType type) {
		// TODO run the tests in the given type
	}
}