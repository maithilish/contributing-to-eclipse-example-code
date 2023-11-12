# Chapter 9 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-9`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

Open org.eclipse.contribution.junit/plugin.xml and in Overview tab, click Launch an Eclipse Application, the Run icon on top right of the editor. This opens a new instance of Eclipse IDE called 'runtime workbench' with runtime-EclipseApplication as its workspace. The runtime workbench loads all the plugins we are developing in the host workbench, including org.eclipse.contribution.junit plugin.

In runtime workbench Outline view, select PassTest class, right click to open the context menu and click Run Test. This action launches SocketTestRunner in a new VM which runs the test and sends the test pass/fail message to TestRunner which listens, in runtime workbench, for result and shows result as pass in info dialog. The host workbench console shows the interactions between SocketTestRunner and TestRunner as log output. Next, run test for FailTest which shows test result as fail.

This chapter discusses safely notiﬁed extensions using ISafeRunnable and SafeRunner.run(). There is no UI change.

