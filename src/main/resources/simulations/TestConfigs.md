# Test Configurations

## Game of Life

### Corner Cross
* Four half-crosses positioned in the corners that should result in a 2x2 block in each corner

### Explosion
* Initial symmetrical configuration that expands outwards before all becoming inactive

### Fancy Cross
* Cross design that shoudln't move upon simulation start

### Fluctuating Hashtag
* A "hashtag" pattern that expands and retracts continuously

### Gosper Glider Gun
* KNOWN PATTERN: A "gun" that continuously generates gliders soaring to the bottom right. The gliders should hit the bottom right corner and become a square before being destroyed by the next glider.

### Pulsar
* KNOWN PATTERN: A 3-period oscillator that resembles a star

### Toad
* KNOWN PATTERN: 2-period oscillator that connect and separate with each period

## Percolation

### Small Maze
* A small maze to demonstrate the percolation simulation's ability to find a way out

### Big Maze
* A larger maze to show percolation can travel for larger stretches

### Heart
* Percolation pattern that resembles a heart that should start filling from the top of the outer heart then from the bottom of the inner heart

## Spreading of Fire

### Basic Fire
* Single fire block in a small forest to showcase simulation rules

### Corner Fires
* Starting fires in the corners with a lower ignitionWithoutNeighbors factor and a lower regrowth rate

### Horizontal Spread
* Horizontal line of trees that will catch fire from right to left. Both parameters set to zero to demonstrate basic spreading.

### Large Forest
* A larger forest with a lower chance for fire to pop up without already caught neighbors

### Spotty Forest
* Separated chunks of trees and a normal ignitionWithoutNeighbors rate to show how wind can spread fire to non-connected regions. Lower tree regrowth rate to show separate regions for longer.

## Segregation

### Large Checkerboard
* Large grid resembling a checkerboard to show that very mixed populations will inevitably separate

### Lines
* Horizontal lines of alternating neighbors that will also inevitably separate into two halves

### Mitosis
* Several blue groups separated and surrounded by red neighbors

### Random Assortment
* Neighbors placed at random with a lower toleranceThreshold

### Wisconsin
* Just one I had fun with that resembles a wisconsin election map

## Wa-Tor World

### Small Pond
* A single shark in a small pond with several individual fish that should result in the sharks eating all the fish and eventually dying out

### Arrows
* Shark-fish pattern resembling perpendicular arrows pointing into the corners

### Planet
* Shark-fish pattern resembling a planet

### Schools and Sharks
* Multiple spread out schools of fish and individual sharks around them. Should result in a continuous cycle of stable fish-eating and reproduction.

### Surround the Fish
* Many sharks surround a small school of fish with a higher shark reproduction time. Should result in both species dying out.

### Surround the Shark
* Many fish surrounding a single shark with gaps to allow for movement

### SFLoom
* A braid of sharks, fish and water to allow for fish movement

### Giant Random
* Huge body of water with random assortments of fish and sharks

