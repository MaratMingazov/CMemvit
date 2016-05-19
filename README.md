# CMemvit

## Overview

CMemvit - Eclipse Plugin C/C++ memory visualization tool, integrated into [Eclipse IDE](http://www.eclipse.org/) for C/C++ developers.

CMemvit is based on idea of natural representation of process memory state. The key advantage of CMemvit from the visualization perspective is an opportunity to show stack and heap process memory areas with the maximum level of detail. This means that every element becomes visualized with specifying of its address in memory if it is technically possible.

![alt tag](https://raw.githubusercontent.com/MaratMingazov/CMemvit/dev/images/CMemvitUserInterface.png)

## Installing the Plugin

To install the CMemvit plugin is to download the CMemvit.zip from Outcome folder, and expand it inside the root directory of your Eclipse installation. This package contains compiled versions of plugin to run CMemvit.

[Download](https://raw.githubusercontent.com/MaratMingazov/CMemvit/dev/Outcome/CMemvit.zip)


## Using the Eclipse Plugin

As CMemvit runs on C/C++ projects, you must create or import some. Example:

Step 1: click <b>File</b> -&gt; <b>New</b> -&gt; <b>Project</b><br>
This brings up the New Project Dialog<br>
Step 2: click <b>C/C++</b> then <b>C++ Project</b> then <b>Next</b><br>
Step 3: enter a name for your project, choose the toolchains, and click <b>Finish</b><br>


Once you have installed the CMemvit and restarted Eclipse you must make the menus available by the following:

* Select Window -- Perspective -- Open Perspective -- Debug
* Select Window -- Show View -- Other -- 
* Expand MemoryVisualization in the tree
* Click on CMemvit then OK

<img src="https://raw.githubusercontent.com/MaratMingazov/CMemvit/dev/images/CMemvitDebugPerspective.png" width="800">

* Please put a breakpoint 
* Go DebugConfigurations -- CMemvit configuration -- right click (new).

<img src="https://raw.githubusercontent.com/MaratMingazov/CMemvit/dev/images/CMemvitConfigurationTab.png" width="800">

* Fill in the required fields
* Now we can start debug our C/C++ project. In CMemvit view the relevant information will be displayed.




