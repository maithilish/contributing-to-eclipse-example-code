# Chapter 6 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-6`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

Open org.eclipse.contribution.junit/plugin.xml and in Overview tab, click Launch an Eclipse Application, the Run icon on top right of the editor. This opens a new instance of Eclipse IDE called 'runtime workbench' with runtime-EclipseApplication as its workspace. The runtime workbench loads all the plugins we are developing in the host workbench, including org.eclipse.contribution.junit plugin.

In Project Explorer expand PassTest.java in demo project, select PassTest class and right click to open the context menu. Alternatively, you can select the class in Outline view. In the popup, click Run Test menu item and it does nothing. The action is fixed in the next chapter.

