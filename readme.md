# Chapter 25 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-25`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 25 adds logic to observe changes to Java elements and updates ResultView. 

Run eclipse.contribution.junit.test.ViewColorTest.java; test should pass. Instead, you can run all the tests created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Run the plugin using Run As -> Eclipse Application. In runtime workbench, go to Window -> Show View -> Other -> Other and open 'Contributed Result View'. Open FailTest.java, change assertTrue(false) to assertTrue(true) and save. This triggers auto build and result view's BG color changes to green. Undo the changes and save; color changes to red. Next create new java project named 'foo' and view's changes to default color. Delete the project foo. 

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

