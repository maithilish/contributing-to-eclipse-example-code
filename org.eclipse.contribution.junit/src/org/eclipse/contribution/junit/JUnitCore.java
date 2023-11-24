package org.eclipse.contribution.junit;

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

public class JUnitCore {
	public static void addTestListener(final ITestRunListener listener) {
		JUnitPlugin.getPlugin().getListeners().add(listener);
	}

	public static void removeTestListener(final ITestRunListener listener) {
		JUnitPlugin.getPlugin().getListeners().remove(listener);
	}

	public static void run(final IType type) throws CoreException {
		JUnitPlugin.getPlugin().run(type);
	}

	public static void run(final IType[] types, final IJavaProject project) throws CoreException {
		JUnitPlugin.getPlugin().run(types, project);
	}
}