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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * A test runner that communicates the test results over a socket. The socket
 * protocol is string based. The following messages are sent:
 * 
 * <pre>
 * <"starting tests "+number>
 *      test run started with the given number of tests
 * <"ending tests">: test run ended
 * <"starting test "+testname+"("+classname+")">
 * 		the given test has started
 * <"failing test "+testname+"("+classname+")">
 * 		test failed. The stack trace is sent
 * 		in the following lines and terminated by "END TRACE"
 * </pre>
 */
public class SocketTestRunner implements TestListener {
	private int port;
	private Socket socket;
	private PrintWriter writer;
	private Path logFile;

	/**
	 * The entry point for the test runner. The arguments are: args[0]: the port
	 * number to connect to args[1-n]: the name of test classes
	 */
	public static void main(String[] args) {
		new SocketTestRunner().runTests(args);
	}

	@SuppressWarnings("unchecked")
	private void runTests(String[] args) {

		logFile = Paths.get(System.getProperty("java.io.tmpdir"), "sockettestrunner.log");

		port = Integer.parseInt(args[0]);
		openClientSocket();
		try {
			log("INFO", "prepare tests");
			TestSuite suite = new TestSuite();
			for (int i = 1; i < args.length; i++) {
				Class<?> testClz = Class.forName(args[i]);
				suite.addTestSuite((Class<? extends TestCase>) testClz);
				log("INFO", "add test to testsuite: " + testClz);
			}
			writer.println("starting tests " + suite.countTestCases()); //$NON-NLS-1$
			TestResult result = new TestResult();
			result.addListener(this);
			suite.run(result);
			writer.println("ending tests"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			log("ERROR", e.getClass().getSimpleName() + " " + e.getMessage());
			if (e instanceof ClassNotFoundException) {
				log("WARN", "ensure project.build() is called after the creation of project in the test");
			}
		} finally {
			log("INFO", "close client socket");
			closeClientSocket();
		}
	}

	private void openClientSocket() {
		for (int i = 0; i < 10; i++) {
			try {
				socket = new Socket("localhost", port); //$NON-NLS-1$
				writer = new PrintWriter(socket.getOutputStream(), true);
				log("INFO", "open client socket #" + (i + 1) + " port: " + port);
				return;
			} catch (Exception e) {
				log("ERROR", getStackTrace(e));
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}
		}
	}

	private void closeClientSocket() {
		writer.close();
		try {
			socket.close();
		} catch (IOException e) {
			log("ERROR", getStackTrace(e));
		}
	}

	public void addError(Test test, Throwable t) {
		writer.println("failing test " + test); //$NON-NLS-1$
		t.printStackTrace(writer);
		writer.println("END TRACE"); //$NON-NLS-1$
	}

	public void addFailure(Test test, AssertionFailedError t) {
		addError(test, t);
	}

	public void endTest(Test test) {
	}

	public void startTest(Test test) {
		writer.println("starting test " + test); //$NON-NLS-1$
	}

	/**
	 * It is not possible to debug this class as it runs in separate VM. Added log
	 * method to send log messages to TestRunner.
	 * 
	 * If socket writer is null then output log to /tmp/socketrunner.log
	 * 
	 * @param logLevel
	 * @param message
	 */
	public void log(String logLevel, String message) {
		if (writer == null) {
			logToFile("[" + logLevel + "] " + message);
		} else {
			writer.println("[" + logLevel + "] " + message);
		}
	}

	private void logToFile(String message) {
		try {
			String msg = message + System.lineSeparator();
			if (Files.exists(logFile)) {
				Files.writeString(logFile, msg, StandardOpenOption.APPEND);
			} else {
				Files.writeString(logFile, msg, StandardOpenOption.CREATE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getStackTrace(Throwable e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}