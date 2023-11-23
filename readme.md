# Chapter 28 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-28`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 28 externalized strings and adds mnemonics.

As example, we have externalized strings ExcludeTestAction.java, TestReportView.java to JUnitMessages.properties. We have also externalized some of the strings in plugin.xml to plugin.properties.

To check mnemonics, run the plugin using Run As -> Eclipse Application. In runtime workbench, select FailTest in Outline view and open its context menu. The mnemonics 'T' is attached to two items - Refactor and Run Test. Press T twice to select 'Run Test'.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.
