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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

public class TestRunner {

	static final String MAIN_CLASS = "org.eclipse.contribution.junit.internal.core.SocketTestRunner"; //$NON-NLS-1$

	private int port;
	private IJavaProject project;
	private BufferedReader reader;

	public TestRunner() {
	}

	public TestRunner(final IJavaProject project) {
		this.project = project;
	}

	public void run(final IType type) throws CoreException {
		project = type.getJavaProject();
		run(new IType[] { type });
	}

	public void run(final IType[] classes) throws CoreException {
		final IVMInstall vmInstall = getVMInstall();
		if (vmInstall == null) {
			return;
		}
		final IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
		if (vmRunner == null) {
			return;
		}

		final String[] classPath = computeClasspath();
		final VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(MAIN_CLASS, classPath);

		final String[] args = new String[classes.length + 1];
		// port = SocketUtil.findUnusedLocalPort("localhost", 10000, 15000);
		port = SocketUtil.findFreePort();
		args[0] = Integer.toString(port);
		for (int i = 0; i < classes.length; i++) {
			args[i + 1] = classes[i].getFullyQualifiedName();
		}
		vmConfig.setProgramArguments(args);

		final ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
		vmRunner.run(vmConfig, launch, null);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		connect();
	}

	private String[] computeClasspath() throws CoreException {
		final String[] defaultPath = JavaRuntime.computeDefaultRuntimeClassPath(project);
		final String[] classPath = new String[defaultPath.length + 2];
		System.arraycopy(defaultPath, 0, classPath, 2, defaultPath.length);

		final JUnitPlugin plugin = JUnitPlugin.getPlugin();
		final URL url = plugin.getInstallURL();
		try {
			// classPath[0] = Platform.asLocalURL(new URL(url, "bin")).getFile();
			// classPath[1] = Platform.asLocalURL(new URL(url,
			// "contribjunit.jar")).getFile();
			classPath[0] = FileLocator.toFileURL(new URL(url, "bin")).getFile();
		} catch (final IOException e) {
			final IStatus status = new Status(IStatus.ERROR, plugin.getPluginName(), IStatus.OK,
					"Could not determine path", //$NON-NLS-1$
					e);
			throw new CoreException(status);
		}
		return classPath;
	}

	private void connect() throws CoreException {
		try {
			ServerSocket server;
			ConsoleLogger.log(this, "start server at port: " + port);
			ConsoleLogger.log(this, "listen to SocketTestRunner...");
			server = new ServerSocket(port);
			try {
				final Socket socket = server.accept();
				try {
					readMessage(socket);
				} finally {
					socket.close();
				}
			} finally {
				ConsoleLogger.log(this, "close server");
				server.close();
			}
		} catch (final IOException e) {
			final IStatus status = new Status(IStatus.ERROR, JUnitPlugin.getPlugin().getPluginName(), IStatus.OK,
					"Could not connect", e); //$NON-NLS-1$
			throw new CoreException(status);
		}
	}

	private void readMessage(final Socket socket) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				parseMessage(line);
			}
		} finally {
			reader.close();
		}
	}

	private void parseMessage(final String line) {
		final JUnitPlugin plugin = JUnitPlugin.getPlugin();
		ConsoleLogger.log(this, "  received message: " + line);
		if (line.startsWith("starting tests ")) {
			final int start = "starting tests ".length(); //$NON-NLS-1$
			final int count = Integer.parseInt(line.substring(start));
			plugin.fireTestsStarted(project, count);
		}

		if (line.startsWith("ending tests")) { //$NON-NLS-1$
			plugin.fireTestsFinished(project);
		}

		if (line.startsWith("starting test ")) { //$NON-NLS-1$
			final int start = "starting test ".length(); //$NON-NLS-1$
			final String method = line.substring(start, line.indexOf("(")); //$NON-NLS-1$
			final String klass = line.substring(line.indexOf("(") + 1, line.indexOf(")")); //$NON-NLS-1$ //$NON-NLS-2$
			plugin.fireTestStarted(project, klass, method);
		}

		if (line.startsWith("failing test ")) { //$NON-NLS-1$
			final int start = "failing test ".length(); //$NON-NLS-1$
			final String method = line.substring(start, line.indexOf("(")); //$NON-NLS-1$
			final String klass = line.substring(line.indexOf("(") + 1, line.indexOf(")")); //$NON-NLS-1$ //$NON-NLS-2$
			final StringWriter buffer = new StringWriter();
			final PrintWriter writer = new PrintWriter(buffer);
			String frame = null;
			try {
				while ((frame = reader.readLine()) != null && (!frame.equals("END TRACE"))) //$NON-NLS-1$
					writer.println(frame);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final String trace = buffer.getBuffer().toString();
			plugin.fireTestFailed(project, klass, method, trace);
		}
	}

	private IVMInstall getVMInstall() throws CoreException {
		IVMInstall vmInstall = JavaRuntime.getVMInstall(project);
		if (vmInstall == null) {
			vmInstall = JavaRuntime.getDefaultVMInstall();
		}
		return vmInstall;
	}
}