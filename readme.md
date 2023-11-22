# Chapter 24 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-24`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 24 adds a new editor to edit test.exclusion file which excludes tests from auto test builder. 

Run eclipse.contribution.junit.test.BuilderTest.java and ExclusionEditorTest.java; both tests should pass. Instead, you can run all the tests created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Run the plugin using Run As -> Eclipse Application. In runtime workbench, open project demo's Properties page and enable Auto Test. Using New -> File option create a file named test.exclusion file in demo project folder: it should be beside src folder at top level of the demo project. Once file is opened a new menu option named 'Exclude Test Case' is displayed in toolbar. It is also added under Edit menu. On click, it opens Test class selection dialog where you can select tests to exclude; select PassTest.java and it is added to test.exclusion file; save the file. It triggers auto builder which reads the exclusions and runs only the FailTest.java; The 'Contributed Test Report' view shows the result of auto build.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

