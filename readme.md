# Chapter 29 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-29`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 29 moves the junit plugin classes to org.eclipse.contribution.junit.internal.core and org.eclipse.contribution.junit.internal.ui packages. It also adds schema/listeners.exsd to validate listeners extension-point contributed by the plugin. Apart from this, there is no new code or test, but you can check plugin behaviour after the reorganization of packages.

Run all the tests created so far by selecting org.eclipse.contribution.junit.test package and Run As -> JUnit Plug-in Test and all tests should pass.

Run the plugin using Run As -> Eclipse Application and check the UI behaviour in runtime workbench.

