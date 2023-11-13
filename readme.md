# Chapter 12 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-12`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 12 adds new plugin project org.eclipse.contribution.junit.test which contains junit tests to test org.eclipse.contribution.junit plugin developed in the book.

Right click on org.eclipse.contribution.junit.test.ListenerTest.java and Run As -> JUnit Plug-in Test. The runtime Eclipse creates a new workspace junit-workspace and the ListenerTest.testFailure() test case creates, in junit-workspace, a Project named TestProject, a package pack1 and a test case pack1.FailTest.java. After test run the test asserts whether FailTest is really failed. The tearDown() method deletes the TestProject from junit-workspace after each test and leaves the junit-workspace in a clean state.

You can check whether TestProject is created by setting a break point project.dispose() call in ListenerTest.tearDown(). Run the test in debug mode and when debugger stops, open junit-workspace in File Manager and analyze the contents of TestProject folder. Next step over (F6) the break point, and the folder is deleted.

