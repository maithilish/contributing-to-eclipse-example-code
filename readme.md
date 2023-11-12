# Chapter 10 - Contributing to Eclipse 

## Installation

For installation of the example code in Eclipse IDE see <a href="https://www.codetab.org/post/contributing-to-eclipse/">Contributing to Eclipse Installation</a>. If you have already done this then no further installation is required. Now on, you have to do only the chapter wise checkout and setup. 

## Setup

You are here after switching branch with `git checkout chapter-10`. Next, select all the plugin projects in Project Explorer and refresh with F5.

## Run

Chapter 10 discusses publishing the plugins. We have not included them in the repository as you can easily generate them with IDE wizards.

The book explains three ways to publish,
 
  - Package the plug-in for use on your machine
  - Package the plug-in for installation by others (a feature)
  - Package the feature for downloading and installation (an update site)
  
To package for use on your machine, use File -> Export -> Plug-in-Development -> Deployable Plug-ins and Fragments wizard. Note that the runtime element in plugin.xml is depreacated and no longer required to generate the distribution.  

To publish as a feature, create a feature project using File -> New -> Others -> Plug-in Development -> Feature Project wizard. Then open feature.xml file and in Overview tab, click Export Deployable Feature button to zip the feature.

To publish as update site, create a Update Site Project using File -> New -> Others -> Plug-in Development -> Update Site Project wizard. Then open the site.xml file, use Add Feature to add the feature and finally, build the update site with Build option. 

