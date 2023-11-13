# Chapter 13 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-13`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

Run eclipse.contribution.junit.test.ViewColorTest.java and eclipse.contribution.junit.test.ViewTest.java as Junit Plug-in Test. Both should pass.

Next, run plugins as Eclipse Application (Run As -> Eclipse Application) and in runtime workbench open 'Contributed Result View' with Window -> Show View -> Other -> Other -> Contributed Result View. Now, in the same workbench open PassTest.java file. In Outline View, select PassTest class and invoke 'Run Test' action from its context menu; The view - Contributed Result View - should turn green. Repeat the same for FailTest.java, and view turns red indicating a test failure.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.
