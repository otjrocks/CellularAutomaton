# Breakout Abstractions Lab Discussion
#### Owen Jennings (otj4), Justin Aronwald (jga18), Troy Ludwig (tdl21)


### Block

We would make the brick class extend Rectangle, so that we do not need methods like getRectangle.
We would also make the Brick class abstract, and create new classes that implement the abstract class for the specific block type.
This would reduce the complexity of if statements in our block class and improve readability by having functions pertaining to a specific block implementation being only in the correct specified file.

This superclass's purpose as an abstraction:
```java
 public abstract class Block extends Rectangle {
     public Block();
     public abstract void hit();
     public abstract int getHealth();
     public abstract Effect getEffect();
     // no implementation, just a comment about its purpose in the abstraction 
 }
```

#### Subclasses

Each subclass's high-level behavorial differences from the superclass
- Multi-hit block should be initialized with extra health. The hit method should only destroy the block when its health is 0.
- Powerup should generate a powerup. The rest will be dealt with the powerup class.
- Exploding should have an effect when hit, which is returned by the getEffect method.


#### Effect on Game/Level class

Which methods are simplified by using this abstraction and why
The handling of brick collisions hit() is definitely simpler since it does not need to check the specific behavior for each block type.
The create block method would also be simpler, since you will no longer need to create all brick types in the same class.
The game and level classes will have less logic for handling the specific brick behaviors since it will only need to be able to call hit() and getEffect() and the specific subclasses will handle the rest of the behavior.
By extending the Rectangle class we no longer will need to have methods to get and set the blocks rectangle shape.

### Power-up

Similarly, this should extend Circle, since the power up was falling. We could also add abstraction to have the specific power up types be in their own class.

This superclass's purpose as an abstraction:
```java
 public abstract class PowerUp extends Circle {
     public PowerUp();
     public abstract String getType();
     public abstract void applyPowerUp();
     // no implementation, just a comment about its purpose in the abstraction 
 }
```

#### Subclasses

Each subclass's high-level behavorial differences from the superclass
- Multi-ball will add a ball when applyPowerUp is called. It will have a type of "multiball"
- Extend-paddle will have an applyPowerUp class that will call the Paddle classes expand()
- Speed-up will have an applyPowerUp class call a ball.speedUp() method.

#### Effect on Game/Level class

Which methods are simplified by using this abstraction and why

The handling and application of power ups was simplified since we do not need to handle all the power up effects in one class.
It also makes it easier to create a new power up type, since you only need to modify one part of code which is the applyPowerUp() method.
If you extend the Circle class you do not need getter and setter methods to get and set the circle for the power up.



### Others?

Other classes such as Paddle and Ball can extend their specific shapes. 