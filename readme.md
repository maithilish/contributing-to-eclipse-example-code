# Chapter 5 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-5`. Now, select all the plugin projects in Project Explorer and refresh with F5.

## Run

Open org.eclipse.contribution.junit/plugin.xml and in Overview tab, click Launch an Eclipse Application, the Run icon on top right of the editor. This opens a new instance of Eclipse IDE called 'runtime workbench' with runtime-EclipseApplication as its workspace. The runtime workbench loads all the plugins we are developing in the host workbench, including org.eclipse.contribution.junit plugin.

In runtime workbench Project Explorer (not the host workbench), create a new Java Project named 'demo' and create a package named 'demo'. Delete module-info.java file from src folder. Next we need to add two junit test cases.

Select package demo and go to New -> JUnit Test Case. In New JUnit Test Case wizard, choose New Junit 3 and create JUnit Test Case named PassTest. Make sure to choose JUnit 3 test radio and not JUnit 4 or JUnit Jupiter radio button! When wizard prompts to Add JUnit 3 library to build path, click OK. Add following content to PassTest,

        import junit.framework.TestCase;
        public class PassTest extends TestCase {
	        public void testPass() {
		        assertTrue(true);
	        }
        }

Same way create one more test case named FailTest with following content,

        import junit.framework.TestCase;
        public class FailTest extends TestCase {
	        public void testFail() {
		        assertTrue(false);
	        }
        }

In Project Explorer expand PassTest.java, select PassTest class and right click to open the context menu. A new menu option, Run Test, appears in the popup menu. Note that it appears only for java type PassTest and not for java file PassTest.java. Alternatively, you can use choose PassTest type in Outline view instead of Project Explorer.

On click, it opens Information Dialog with message 'The chosen operation is not currently available'.

