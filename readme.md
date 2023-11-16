# Chapter 18 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-18`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The Chapter 18 adds AutoTestNature, AutoTestBuilder and a test case BuilderTest.

Run eclipse.contribution.junit.test.BuilderTest.java with Run As -> JUnit Plug-in Test and test should pass. Instead, you can run all the test created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Next, run plugins with Run As -> Eclipse Application. In runtime workbench open FailTest.java, and the test method testFail() is marked as error. Modify assertTrue(false) statement to assertTrue(true) and demo project auto builds; error marker disappears. Undo the statement back to assertTrue(false) and again, auto build is triggered and marker appears back.

Note, the PassTest and FailTest are in demo project that we created in Chapter 5. If you have missed out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

