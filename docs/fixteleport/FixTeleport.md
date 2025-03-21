# Fix Teleport

```
The Fix Teleport mod is designed to resolve unfair issues that occur more frequently with higher ping.
Development began in April 2024, and the first release was on April 23, 2024.
With precise and complex algorithms, it provides users with a smooth and clean teleportation logic.
```

## How does it work?
When we use a teleport item, a packet is sent to the server to indicate its usage, and the server responds by sending us the player's expected location.
At the same time the teleport item is used, the mod calculates the teleport location and moves the player instantly, then sends the position data to the server for confirmation.
Simply put, it changes the teleport process from a sequential method to a parallel method, allowing it to function without being affected by ping.

## What is Unfair Disadvantages of High Ping?
Assumptions
Ping: 200ms (0.2s)
Teleport distance: 5 blocks

### 1. Delayed Teleport
You're standing in front of a chest and plan to use a teleport item to move right in front of it and open it.
A low-ping player can open the chest instantly, while you experience a 0.2-second delay before you can interact with it.

### 2. Wasted Time
You're walking forward from coordinate 0.
You use a teleport item and send a packet to the server to teleport to coordinate 5.
Since the teleport hasn't happened yet, you walk from coordinate 0 to 1.
After 0.2 seconds, you are suddenly moved to coordinate 5.
So you teleported 5 blocks and walked 1 block, but in the end, you only moved 5 blocks.
In other words, any movement during your 0.2-second ping delay has no effect.

### 3. Screen Rotation
You use a teleport item while looking straight ahead, then turn your head to the right.
However, 0.2 seconds later, when the teleport happens, your view snaps back to the original forward direction.
This happens because, as explained earlier, any action during the 0.2-second delay is discarded.

## ⚠ Precautions
The teleportation logic is not always perfect.
To put it simply, you might intend to teleport 6 blocks forward, but only move 5 blocks.
Since this differs from what the server expects, the server will correct your position to 6 blocks forward.
This is commonly known as a "lagback".

Lagbacks in Fix Teleport occur when the predicted position is incorrect or when you're trying to teleport in an area where teleporting isn't allowed.
It can also happen for reasons unrelated to the mod.

This alone is not a problem, because lagbacks are common even without Fix Teleport.
They can happen due to server lag, network issues, or random bugs.
However, if Fix Teleport causes excessive or severe lagbacks, the anti-cheat may treat it as cheating.
That’s because it appears as if the player keeps insisting they’re at point B when the server believes they should be at point A.
As long as you don’t intentionally abuse it or spam it heavily, there’s no risk of a false ban.

Fix Teleport includes various checks to prevent this, but users still need to be careful.

One area that especially requires caution is Kuudra.
If you're caught by a tentacle and unable to teleport, repeatedly using the teleport item in that state could increase the risk of a ban.

Also, if the server freezes or lags temporarily, stop using the teleport item until it recovers.
The server will eventually correct your position with a lagback, and you can use the item again after that.

Please use Fix Teleport wisely.
