This is our implementation of the animations that we need to make for the future. 
We made a few strategic design choices that we believe will benefit us in the future, 
such as making interfaces for potentially expandable sets of objects, using composition 
in our model, and creating a visitor for our shape mutation.

Here is an overview of our classes and interfaces:
IAnimationModel - This is our overarching interface that kind of rules over our code for 
the model. It has methods such as add, remove, createShape, and a few more methods that 
are the bare essentials for creating a new model class that would be able to run in the 
same way. This leaves a lot of room to expand our potential model implementation down 
the road with just the essential methods included.

AnimationModelImpl - This is our first class for our IAnimationModel. This class consists of a
Map<String, List<IAnimation>> and a Map<String, IShape>. Since these are our two fields it 
keeps our code simple, but it also allows us to name shapes like was asked for in the assignment 
specifications. An invariant is that a key for a shape map is the same key as the list of 
animations for that shape in the other map. This naming convention allows us to call a shape 
and its corresponding animations while also making sure that no other shape or animations can 
have duplicate names (as that would just be confusing).

IAnimation - This interface represents a singular animation for a given time period. It can only 
act on one shape and we have methods that are essential that we have put in the interface to allow 
expansion later.

AAnimation - This is our abstract class that does a lot of the heavy lifting for the subclasses 
and gives a lot of leeway for subclass expansions down the road. It also has a constructor that 
makes sure it has a start time, and end time, and a list of IAnimationCommands. An invariant for 
the class is that there can be no more than one of each AnimationCommand in an IAnimation, as 
this would make a conflicting state. AAnimationImpl is just a simple animation implementation 
for a start, end and list of animation commands to be applied to one shape for a given amount of 
time.

IAnimationCommand - This is the interface that holds the command classes for a command that mutates 
a shape. We chose to use this because it allows for us to keep track of what operations are being 
done to a shape for a given amount of time, which would be much more difficult if we were just
calling methods spontaneously. We can know that one move is being done to a shape for a given amount 
of time and throw an exception if someone tries to add another move to an IAnimation that already 
has a ChangePosition. This also allows us to expand our available commands and make more complex 
ones that will also be valid as long as there arent conflicting states, which is cool.

The Color, Posn, and Dimension classes are just to be more organized with our fields of a shape so 
that we can throw exceptions easier and control what dimensions, colors, and posns are allowed. It 
also allows us to add an interface down the road that extends a given class to expand the
definition of a posn, color, or dimension.

The IShape interface is the last one that represents a shape in our code. It has a few methods 
in the interface, but a lot of the lifting is done by the AShape, which has a broad constructor. 
This will allow us to add shapes easily in the future and give new fields that will expand the 
options when the animations get more complex.

Also, as a note, our Model takes in multiple commands to alter a shape, but if they overlap, then 
it automatically reconfigures them to merge them and make it simpler for the machine to handle. 
Thought that was worth adding.


CHANGES: (Assignment 6)
Controller:
- Added: interface IControllerFeatures, represents a controller
- Added: class ControllerImpl, to represent an instance
- Added: run

View:
- Added: interface IView, to represent a view
- Added: ViewImplSVG class to represent an instance of a SVG visual view
- Added: ViewImplTextual class to represent an instance of a textual visual view
- Added: ViewImplVisual class to represent an instance of a visual visual view

Assignment 7:

Now that our project has finally come to a close (thank god), it's much easier to look back on what
we did well and what we could improve on.

UpdateDrawingView - We added this class as the visual view for the animation. The reason why we
decided against composition for this particular class was because the animation that we output
in the view has very limited features, as it just plays the animation and that's it. This design
allowed us to include a variety of new things, such as starting the animation, stopping the
animation, and acting as a helper to the controller to facilitate the necessary functions that
define this view vs. the normal view. I'm glad we went this route, as it let us have a lot more
creative freedom with the way that the image was controlled without being constrained to the
strict bounds of composition of a separate design that serves a different purpose.

IGraphicsEdit - This is our interface that is the overarching structure for our update drawing
class. This allows it to be backwards compatible and gives us the opportunity to expand it in the
future.

ViewImplVisualControllable - The visual controllable is our view for the editor view. This
has all of the features that are desired for this assignment such as the buttons and used the
features class so that we can communicate between the controller and view as we learned in class.
This allows us to keep local things local, but also delegatesome of the overarching commands to
be controlled by the controller.





