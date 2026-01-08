<h1>What is this?</h1>
- simulated swerve drive using logical interfaces

- a way to kill time/refine my understanding of swerve

- an implementation of a way to test swerve logic without having a robot (this method may be compatible with commandswervedrivetrain)

- logic that defines four swerve modules and a drivetrain

I originally wanted this to be full on hardware abstraction, but to do that I would need to implement I/O abstraction and rewrite all of this. I am still doing that, but in a different repo to preserve this one for future reference. 

<h2>Running this</h2>
How to run this:

<h3>NetworkTables config:</h3>

Tested on a keyboard simulating a joystick, some values are reversed when using an actual joystick

Drag keyboard 0 to joystick 0 

Add 5 axis

Axis 0 increase/decrease set to keys a/d

Axis 1 increase/decrease set to keys w/s

Axis 4 increase/decrease set to keys up and down arrows

<h3>Advantagescope config: </h3>

Open the swerve tab

Open the advantagescope folder and drag module speed,direction to the Sources section
