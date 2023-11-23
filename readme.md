# Chapter 27 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-27`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 27 enables Help for the plugin and context helps for Test Report view and Run Test action.

Run the plugin using Run As -> Eclipse Application. In runtime workbench, go to Help -> Help Contents; the help TOC now contains a new item 'Auto Testing'.

Again in the runtime workbench, Go to Window -> Show View and open 'Contributed Test Report'. Clean the project to trigger auto build, and in the Test Report view press Ctrl + F1 which opens the context help for the view. Next, select FailTest in Outline view and open its context menu. Scroll through items and highlight 'Run Test', and click Ctrl + F1 to open its context menu. 

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.
