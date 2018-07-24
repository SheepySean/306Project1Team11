<p align="center"> 
<img src="https://github.com/SheepySean/306Project1Team11/blob/master/img.png">
</p>

# 306 Project 1		|		Team 11		|		Para11el


**Using AI and parallel processing power to solve difficult scheduling problem**

Brief Description of the project to go here

## Getting Started

These instructions will get you a running version of the project on your local machine.

### Prerequisites

The project should be able to run on all operating systems namely **Linux**.

The project requires:
```
GraphStream
```

### (Optional) Additional Requirements


## Running the Code (For a Developer)

These instructions are designed to get the project up and running on your system so that you may develop it further.

### Cloning the Repository

First you must clone this repo onto your local machine with:
```git
git clone https://github.com/SheepySean/306Project1Team11.git
```
This will create a folder called **306Project1Team11** on your system.

### Import the project to an IDE

With the project cloned, you can choose between **Eclipse** and **IntelliJ** as your development IDE.

#### Eclipse
1. Open Eclipse on your computer and go *File* -> *New* -> *Java Project*.
2. Untick *Use default location* and select the **306Project1Team11** folder that you have just cloned.
3. Click *Finish*
You will now be able to interact with the Java files as per normal.

##### Deployment with Maven
1. Right-click *pom.xml* and select *Run as* then *Run Configurations...*.
2. Make sure *Maven Build* is selected in the list on the left hand side.
3. Press the *New* button in the top left corner.
4. Select the base directory as *Workspace...*
5. Then type the following into *Goals:*
```maven
clean compile assembly:single
```
6. Click *Run*
7. You will see the command line at the bottom of Eclipse fill with output. Wait for *BUILD SUCCESS* to display.**If you get build failures with the error saying there is bad central directory size, delete your .m2 folder and run the pom.xml file again**
8. Once the build is completed, navigate to your project folder using explorer and double click the JAR *scheduling-solver-x.x-SNAPSHOT-jar-with-dependencies.jar*

#### IntelliJ
1. Open IntelliJ on your computer and go *Import Project*.
2. Navigate to the **306Project1Team11** Folder, select it and click *OK*.
3. Select *Import project from external model*, select *Maven*, and click *Next*.
4. Confirm the *Root directory* at the top of the window ends in **306Project1Team11** and click *Next*.
5. In the next window, make sure **com.para11el:scheduling-solver-x.x-SNAPSHOT** is selected and click *Next*.
6. Select the JDK you would like to work with and click *Next*.
7. Check you're happy with the project name and click *Finish*.
You will now be able to interact with the Java files as per normal.

##### Deployment with Maven
1. Double click *pom.xml* in the file tree.
2. Along the top of the IntelliJ Window, click *Run* -> *Edit Configurations...*.
3. At the top left corner click the green plus button to add a new run configuration, and select *Maven*.
4. Feel free to name the configuration as something meaningful such as *Project Deployment*.
5. Then type the following into *Command line:*
```maven
clean compile assembly:single
```
6. Click *OK*.
7. Along the top right, click the green play button to run the maven build.
8. You will see the command line at the bottom of IntelliJ fill with output. Wait for *BUILD SUCCESS* to display.**If you get build failures with the error saying there is bad central directory size, delete your .m2 folder and run the pom.xml file again**
9. Once the build is completed, navigate to your project folder using explorer and double click the JAR *scheduling-solver-x.x-SNAPSHOT-jar-with-dependencies.jar*

## Running the Code (For a User)

With Java (plus dependant packages) installed you can run the project code. 

To do this *Provide Step by step instructions here*:
```java
java .........
```

## Folder Structure

The projects folder structure is as follows:

```
306Project1Team11
|
|__example_graphs (Sample graphs for utilising the application)
|  |__example.dot
|  |__...
|  |__...
|  
|__lib (Libraries folder for any additional project libraries)
|  |__ext (External Libraries)
|  |  |__graph_stream (GraphStream Library for graph management)
|  |     |__gs-algo-1.3.jar
|  |     |__gs-core-1.3.jar
|  |     |__gs-ui-1.3.jar
|  |__... ()
|
|__src (.java files for compiling and running the project)
|  |__main (Runner Files)
|  |  |__java
|  |  |  |__com
|  |  |     |__para11el
|  |  |        |__scheduler (Main project package)
|  |  |           |__util (Utilities)
|  |  |           |  |__GraphFileManager.java
|  |  |           |
|  |  |           |__...
|  |  |
|  |  |__resources (Other External Resources)
|  |     |__...
|  |
|  |__test (Test files)
|     |__java
|        |__...
|
|
|__.gitignore
|
|__pom.xml (Maven file for building the project)
|
|__README.md (You Are Here)
```

## Helpful Links

*These will be locations where additional install information can be found including dependent packages*

*  [GraphStream](http://graphstream-project.org/) - GraphStream project for graph management and visualisation. Simple tutorials can be found [here](http://graphstream-project.org/doc/Tutorials/)
*  [Link 2 (Currently links to Google)](https://www.google.com/) - Description of the link (i.e. Google search browser for finding information.


## Authors

* **Sean Oldfield** - *Development* - [SheepySean](https://github.com/SheepySean)
* **Tina Chen** - *Development* - [twchen97](https://github.com/twchen97)
* **Rebekah Berriman** - *Development* - [rmberriman](https://github.com/rmberriman)
* **Jessica Alcantara** - *Development* - [Jess-Alcantara](https://github.com/Jess-Alcantara)
* **Holly Hagenson** - *Development* - [hhagenson28](https://github.com/hhagenson28)

## Acknowledgments

* Any additional acknowledgments i.e. StackOverflow

---