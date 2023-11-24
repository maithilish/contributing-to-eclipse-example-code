# Chapter 30 - Contributing to Eclipse

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching the branch with `git checkout chapter-30`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

The chapter 30 adds a new plugin project org.eclipse.contribution.junit.beep which hooks to the listeners extension-point published by the org.eclipse.contribution.junit plugin to emit a beep when test fails.

Import the new plugin project in to the workspace. Go to Import -> Plug-ins and Fragments, in the wizard do the following,

    In 'Import From' dialog, select Directory option, then Browse and select the eclipse workspace where you have downloaded the git repository.
    In 'Import As' dialog, select Projects with source folders option; click Next.
    In Selection dialog, select org.eclipse.contribution.junit.beep and click Add; click Finish to complete the import.
    
Launch the application with Run As -> Eclipse Application. In runtime workbench, open FailTest.java and invoke 'Run Test' action; if your system supports beeps then a beep is emitted on test failure. Auto Test build or Project clean also emits the beep.

Note, the PassTest and FailTest are in demo project we created in Chapter 5. If you have missed that out, then you can view the setup instructions in readme.md with `git show chapter-5:readme.md`.


