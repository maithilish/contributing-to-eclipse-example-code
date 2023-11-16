# Chapter 19 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-19`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The Chapter 19 adds AutoTestPropertyPage.

Run plugins with Run As -> Eclipse Application. In runtime workbench open Properties of demo project from its context menu. The Properties page has a new item Auto-Test where you enable/disable auto set property. First disable it and save; now if you select Builders from Properties page, the demo will have only the Java Builder nature. Next, enable Auto-Test and Builders will have two natures - Java Builder and Auto Test Builder natures. 

To see its impact on auto build, disable the Auto Test property. Open the FailTest.java, and the test method testFail() is marked as error. Modify assertTrue(false) statement to assertTrue(true) and demo project doesn't auto build; error marker stays. Undo the changes. Next, enable the Auto Test property, and repeat the FailTest.java modification; error markers appears and disappears based on test result as auto build gets triggered on every save.

Note, the PassTest and FailTest are in demo project that we created in Chapter 5. If you have missed out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

