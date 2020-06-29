# VRobot
Visually Programmable Mining and Building Robots.

### Current MC Version: 1.15.2

#### Curseforge Project Page
TODO

## About
VRobot allows you to progrmm either harvesting robots or building robots. Both types of robots are programmed using a visual programming language provided by the mod API **[Visibilis](https://github.com/CAS-ual-TY/Visibilis)**.
**Harvesting Robots**
- Can not go through walls (TODO)
- Use tools to harvest; affected by enchantments (like silk touch) and other tool features (TiC hammers!)
- Require energy (any furnace fuel also works) (TODO)
**Building Robots** (TODO)
- Can go through walls
- Can place blocks
- Require energy (any furnace fuel also works) (TODO)

## Visual Coding - How everything works
Lets talk about how it works.
### Robot + Code
The robot can be either Idle or Working. By calling "start" or "continue" (nodes; or you click the buttons) you make the robot go into the Working state. By calling "pause" or "end" you make the robot go into the Idle state.
#### Idle State
The robot does nothing here. It waits for a player to activate it via button click or (if programmed that way) it waits for a redstone signal or possibly other factors it can react to (TODO).
#### Working State
The robot continues to work until interrupted or told to pause/stop. This happens manually or when a node is called. The robot might also stop if the robot is interrupted in doing something or can not execute a certain robot action (eg. it wants to go forward but there is a block) (TODO).
### Code Execution Details
Generally, the robot always follows the following principle:
- Run the visual code (Tick node)
- Work down all queued robot actions
- Repeat
On the initial start, however, the Initialize node is called (this is done only once; only when the robot is started, NOT when it is continued), instead of the Tick node. These 2 mentioned nodes are event nodes; they are the beginning of your code. They get called from outside your coding frame and from that point on you can decide what it called next.
#### You can programm your robot using only the Initialize node
I already mentioned that the robot just works down all robot actions. So you can just queue all actions you want in the Initialize phase and not use the Tick phase at all. Eg. you tell the robot to Harvest Front and then to Move Forward, and now to repeat this 10 times. The robot does all of that and then nothing anymore, as the Initialize node is only called once. The advantage is simplicity and predictability, the disadvantage is that you do not have access to Immediates.
The more advanced strategy is to programm everything in the Tick phase. This gets called every time the robot has worked down the robot action queue. This now also allows you to use Immediates.
### Node Types
There are 2 types of nodes that are used by the robot: **Immediates** and **Robot Actions**. There is also **Other** node types which do not fall under the first 2 categories:
#### Immediates
- Do not cost energy
- Execute immediately on code execution
- Finish immediately (0 time)
#### Robot Actions
- Cost energy
- Buffered in the robot (in a queue); get executed 1 by 1 after code execution is done
- Take time to finish (depending on action and other factors eg. harvesting depends on tool and block)
#### Example: Possible Errors or Immediates
Lets say you call the following code on the Initialize node:
4x Forward (Robot Action), 1x Export Items (Immediate)
This would actually do the reverse, it would first execute the Export Items node, and only then move Foward 4 times.
#### Other
These can be anything, from simple calculations to variable saving. These are generally just helper nodes to calculate or do something needed for Immediates or Robot Actions.
