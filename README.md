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

First you must clone this repo onto your local machine with
```git
git clone https://github.com/SheepySean/306Project1Team11.git
```
This will create a folder called 306Project1Team11 on your system.

### Import the project to an IDE

With the project cloned, you can choose between **Eclipse** and **IntelliJ** as your development IDE.

#### Eclipse
1. Open Eclipse on your computer and go *File* -> *New* -> *Java Project*.
2. Untick *Use default location* and select the 306Project1Team11 folder that you have just cloned.
3. Click *Finish*
You will now be able to interact with the Java files as per normal.

###### Deployment with Maven
1. Right-click *pom.xml* and select *Run as* then *Run Configurations...*.
2. Make sure *Maven Build* is selected in the list on the left hand side.
3. Press the *New* button in the top left corner.
4. Select the base directory as *Workspace...*
5. Then type the following into *Goals:*
```cmd
clean compile assembly:single
```
6. Click *Run*
7. You will see the command line at the bottom of Eclipse fill with output. Wait for *BUILD SUCCESS* to display.
8. Once the build is completed, navigate to your project folder using explorer and double click the JAR *scheduling-solver-x.x-SNAPSHOT-jar-with-dependencies.jar*
**If you get build failures with the error saying there is bad central directory size, delete your .m2 folder and run the pom.xml file again**

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
|  |__GraphFileManager.java (File I/O)
|
|__.gitignore
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