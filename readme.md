# Chapter 22 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-22`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 16.6 introduced RerunMarkerResolutionGenerator which shows quick fix on selecting the error marker, but its implementation was postponed. The present Chapter implements the left out marker resolution. 

Run eclipse.contribution.junit.test.MarkerResolutionTest.java and test should pass. Instead, you can run all the test created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Next, run the plugin as Eclipse Application - Run As -> Eclipse Application. In runtime workbench, open project demo's Properties page and disable Auto Test. Next, open FailTest.java, and select FailTest class in Outline View, and invoke 'Run Test' action from its context menu; as the test fails, the method testFail() is marked with error marker. Edit the statement assertTrue(false) as assertTrue(true) and save; even through the error is fixed, the error marker still continues as we have disabled the auto test. Select the error marker, and the popup displays 'Re-run Test' quick fix. Apply the quick fix and marker disappears.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

