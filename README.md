# Animation-Engine

Many things can be visualized in a way that illustrates how they work more succinctly than descriptions or code. For example, the Towers of Hanoi problem, shown below, is much easier understood with a visual demonstration that a textual algorithm. During my Object-Oriented Design course, my partner Zain Aaban and I worked to solve this problem over 3 weeks. We worked to create a very clearly diivided MVC architecture, while also allowing for abstraction of outputs. The input file format for the generation is a text file as seen in the example below:

```
canvas 10 0 640 225
shape S0 rectangle
shape S1 oval
motion S0 0 100 75 20 15 255 0 0   4 100 75 20 15 255 0 0
motion S0 5 100 75 20 15 255 0 0   15 100 0 20 15 255 0 0
motion S0 19 100 75 20 15 255 0 0  24 100 0 20 15 255 0 0
motion S1 0 100 0 20 15 255 0 0    10 320 0 20 15 178 0 0
motion S1 15 320 0 20 15 178 0 0   20 320 210 20 15 178 0 0
...
```

Which would then be processed to allow for our custom UI implementation:

![](https://github.com/liamgabrielknowles/Animation-Engine/blob/main/output-onlinegiftools.gif)

An XML SVG output format: 

```
<svg width="800" height="800" version="1.1"
     xmlns="http://www.w3.org/2000/svg">
<rect>
<animate id="base" begin="0;base.end" dur="7700ms" attributeName="visibility" from="hide" to="hide"/></rect><rect id="background" x="0" y="0" width="800" height="800" fill="rgb(33,94,248)" visibility="visible" >
<animate attributeType="xml" begin="base.begin+2500ms" dur="2000ms" attributeName="fill" to="rgb(16,45,248)" fill="freeze" />
</rect><rect id="B0" x="80" y="424" width="100" height="326" fill="rgb(0,0,0)" visibility="visible" >
</rect><rect id="B1" x="260" y="365" width="100" height="385" fill="rgb(0,0,0)" visibility="visible" >
...
```

and a textual description of the shapes

```
Columns:
t x y w h r g b      t x y w h r g b
shape disk1 rectangle
25 190 180 20 30 0 49 90      35 190 50 20 30 0 49 90
36 190 50 20 30 0 49 90      46 490 50 20 30 0 49 90
47 490 50 20 30 0 49 90      57 490 240 20 30 0 49 90
89 490 240 20 30 0 49 90      99 490 50 20 30 0 49 90
100 490 50 20 30 0 49 90      110 340 50 20 30 0 49 90
111 340 50 20 30 0 49 90      121 340 210 20 30 0 49 90
153 340 210 20 30 0 49 90      163 340 50 20 30 0 49 90
164 340 50 20 30 0 49 90      174 190 50 20 30 0 49 90
175 190 50 20 30 0 49 90      185 190 240 20 30 0 49 90
217 190 240 20 30 0 49 90      227 190 50 20 30 0 49 90
228 190 50 20 30 0 49 90      238 490 50 20 30 0 49 90
239 490 50 20 30 0 49 90      249 490 180 20 30 0 49 90
249 490 180 20 30 0 49 90      257 490 180 20 30 0 255 0
shape disk2 rectangle
57 167 210 65 30 6 247 41      67 167 50 65 30 6 247 41
68 167 50 65 30 6 247 41      78 317 50 65 30 6 247 41
79 317 50 65 30 6 247 41      89 317 240 65 30 6 247 41
```

> A few sample animations that were generated using the engine:

![GIF](https://course.ccs.neu.edu/cs3500f18/night.gif)
![GIF](https://course.ccs.neu.edu/cs3500f18/toh-5.gif)


> A demonstration of the UI of the application:

![](output-onlinegiftools.gif)

# Documentation 

For full documentation, navigate to the docs branch of the project and download the branch contents as a zip. Then opening the index.html in a browser on local machine will yield a Javadoc of the entire project. 

Here is a brief overview of my classes and interfaces:
## IAnimationModel
IAnimationModel - This is my overarching interface that kind of rules over my code for 
the model. It has methods such as add, remove, createShape, and a few more methods that 
are the bare essentials for creating a new model class that would be able to run in the 
same way. This leaves a lot of room to expand my potential model implementation down 
the road with just the essential methods included.

## AnimationModelImpl
AnimationModelImpl - This is my first class for my IAnimationModel. This class consists of a
`Map<String, List<IAnimation>>` and a `Map<String, IShape>`. Since these are my two fields it 
keeps my code simple, but it also allows us to name shapes like was asked for in the assignment 
specifications. An invariant is that a key for a shape map is the same key as the list of 
animations for that shape in the other map. This naming convention allows us to call a shape 
and its corresponding animations while also making sure that no other shape or animations can 
have duplicate names (as that would just be confusing).

## IAnimation
IAnimation - This interface represents a singular animation for a given time period. It can only 
act on one shape and we have methods that are essential that we have put in the interface to allow 
expansion later.

## AAnimation
AAnimation - This is my abstract class that does a lot of the heavy lifting for the subclasses 
and gives a lot of leeway for subclass expansions down the road. It also has a constructor that 
makes sure it has a start time, and end time, and a list of IAnimationCommands. An invariant for 
the class is that there can be no more than one of each AnimationCommand in an IAnimation, as 
this would make a conflicting state. AAnimationImpl is just a simple animation implementation 
for a start, end and list of animation commands to be applied to one shape for a given amount of 
time.

## IAnimationCommand
IAnimationCommand - This is the interface that holds the command classes for a command that mutates 
a shape. We chose to use this because it allows for us to keep track of what operations are being 
done to a shape for a given amount of time, which would be much more difficult if we were just
calling methods spontaneously. We can know that one move is being done to a shape for a given amount 
of time and throw an exception if someone tries to add another move to an IAnimation that already 
has a ChangePosition. This also allows us to expand my available commands and make more complex 
ones that will also be valid as long as there arent conflicting states, which is cool.


## Colors, Posns, and Dimensions
The Color, Posn, and Dimension classes are just to be more organized with my fields of a shape so 
that we can throw exceptions easier and control what dimensions, colors, and posns are allowed. It 
also allows us to add an interface down the road that extends a given class to expand the
definition of a posn, color, or dimension.

## IShape
The IShape interface is the last one that represents a shape in my code. It has a few methods 
in the interface, but a lot of the lifting is done by the AShape, which has a broad constructor. 
This will allow us to add shapes easily in the future and give new fields that will expand the 
options when the animations get more complex.

Also, as a note, my Model takes in multiple commands to alter a shape, but if they overlap, then 
it automatically reconfigures them to merge them and make it simpler for the machine to handle. 

# Running the Project 

In order to run the program, the user can download the Animator.jar from the folder resources as well as all the text files from the folder demos. Then, put all the files in a new folder, type out the configuration in the command-prompt/terminal. In the run configuration, the user can also specify command-line arguments, such as the file you want to read in, the location you want the output to be printed, the view name you want to use, and the speed of the animation. The options for the view name are "text," "visual, and "svg". For example,

To run the application on a local machine, a user can download the Animator.jar file from the folder resources and all the text files in the root foler. Then putting those files in a new folder and specifying run configurations will allow the user to control and run the app from the command line. The options are: 

1. java -jar Animator.jar 
2. Inputs: -in <desired demo file to run>
3. Speed in ticks/second: -speed (int from 1-100)
4. View Type: -view (view-type)
     * view-type 1 (Custom UI view): visual
     * view-type 2 (svg file view): svg
     * view-type 3 (texual output): text
5. Output file name: -out (file-name)
     
Example command: 

```java -jar Animator.jar -in big-bang-big-crunch.txt -speed 1 -view visual -out out.txt```
     
This command will use big-bang-big-crunch.txt for the animation file with its output going to the file out.txt, and create a visual view to show the animation at a speed of 1 ticks per second.
