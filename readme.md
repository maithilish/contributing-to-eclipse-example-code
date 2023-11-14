# Chapter 16 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-16`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The Chapter 16 introduces MarkerCreator which adds error marker to the test method that fails.

Run eclipse.contribution.junit.test.MarkerTest.java and test should pass. Instead, you can run all the test created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Next, run plugins as Eclipse Application - Run As -> Eclipse Application. In runtime workbench open FailTest.java, and select FailTest class in Outline View, and invoke 'Run Test' action from its context menu; as the test fails, the method testFail() is marked with error marker and a error record is inserted in Problems view. Repeat the same for PassTest.java and error marker from FailTest is deleted. If you run FailTest again, error marker reappears.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

