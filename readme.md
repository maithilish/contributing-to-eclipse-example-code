# Chapter 23 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-23`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 23 adds a new view TestReport View.

Run eclipse.contribution.junit.test.TestReportTest.java and test should pass. Instead, you can run all the test created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Run the plugin as Eclipse Application - Run As -> Eclipse Application. In runtime workbench, open project demo's Properties page and enable Auto Test. Go to Window -> Show View -> Other -> Other and select 'Contributed Test Report' to open the view. Open FailTest.java, and make some change, undo the change and save to trigger Auto Test build. The 'Contributed Test Report' view displays the test report; the PassTest in default color and FailTest in red. The report also show time taken to run the tests. Next, edit FailTest and modify assertTrue(false) statement as assertTrue(true) and save to trigger auto test build; report should clear the earlier entries and show both tests in default color as both tests pass. Undo the changes to FailTest and save; now FailtTest is shown in red as test fails.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

