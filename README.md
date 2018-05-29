# frc-score-simulator

This is a score simulator for FRC games, in which you input rules and it outputs a graph of points versus time.

**Game Object format:**<br>
1x object, 3x object, 100x object<br>
can include "point" or "points" as an object (both will be counted as points on the graph)

**Rule Types:**
* Score: adds the specified game objects
* Threshold: adds the specified game objects when a certain number of game objects is reached
* Ratio: adds the specified game objects each time a certain number of game objects is added

**Planned Rule Types:**
* Time: adds game objects from a start time to an end time
