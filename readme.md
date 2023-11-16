# Chapter 21 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-21`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The Chapter 21 enables tracing.

As explained in the chapter, enable tracing in Eclipse Application run configuration for a single item org.eclipse.contribution.junit - trace/testfinding. Run the application, and in runtime workbench trigger auto build. The host workbench console displays, among other logs, the following trace,

        Auto Test:
	        demo.FailTest
	        demo.PassTest

