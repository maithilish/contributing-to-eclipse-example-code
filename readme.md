# Chapter 3 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup 

You are here after switching branch with `git checkout chapter-3`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

Open org.eclipse.contribution.hello/plugin.xml and in Overview tab, click Launch an Eclipse Application, the Run icon on top right of the editor. This opens a new instance of Eclipse IDE called 'runtime workbench' with runtime-EclipseApplication as its workspace. The runtime workbench loads all the plugins we are developing in the host workbench, including org.eclipse.contribution.hello plugin.

The 'Hello Action Set', configured in the chapter, is not enabled by default. To enable it, open Window -> Perspective -> Customize Perspective..., go to Action Set Availability tab and enable Hello Action Set. This makes the Hello button visible in the toolbar; on click it opens, 'Hello Eclipse World', message dialog.


