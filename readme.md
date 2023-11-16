# Chapter 20 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-20`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The Chapter 20 explains Exception Handling in Eclipse Plugin.

As explained in the chapter 20 force problem to occur in AutoTestPropertyPage.performOk() method. Run plugins with Run As -> Eclipse Application. In runtime workbench open the demo project's Properties page. Toggle Auto-Test property and save, and it displays error dialog.

Make changes in MarkerCreator$Listener, as explained in the chapter, to force problem to occur. Run plugins with Run As -> Eclipse Application. In runtime workbench run test on FailTest. Open Error Log view with Window -> Show View -> Error Log, and it should contain an error entry - Problem deleting markers. Discard changes with `git restore .`

Note, the PassTest and FailTest are in demo project that we created in Chapter 5. If you have missed out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.

