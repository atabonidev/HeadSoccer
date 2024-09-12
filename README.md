# Head Soccer

**Authors:** Matilde Simonini & Alessandro Taboni

## Introduction

Clone of the homonymous mobile game. In this version, only the multiplayer mode is implemented, where two players compete in a match to reach 3 goals first.

## Rules

- **Number of players:** 2
- **Available actions:** Players can move left, right, jump, and kick.
- **Restart:** Whenever a goal is scored or an out-of-bounds occurs (ball stops above the crossbar), the players and the ball return to their initial positions.
- **Victory:** The player who scores 3 goals first wins.

## Controls

- **Kick (space):** Kick the ball
- **Move Left (left arrow):** Move left
- **Move Right (right arrow):** Move right
- **Jump (up arrow):** Jump

## Game Phases

The following are the various phases of the game:

- **Game Start:** At startup, the game prompts you to enter the player's name and the server IP address to connect to.
- **Waiting Room:** By pressing the "Connect" button, you enter the waiting room until the second player arrives.
- **Game Screen:** Once the second player connects, you enter the game screen, where players can control their characters.
- **End of the Game:** The game ends when one of the two players scores the third goal, showing the final result.

## Project Features

- **MVC Pattern (Model-View-Controller):** Application of the MVC pattern for separation of responsibilities (single responsibility).
- **Client/Server Architecture:** Creation of a client-server architecture for managing real-time multiplayer mode.
- **Graphical User Interface (GUI):** Graphical interface in Java Swing (required by the professor for the project).
- **Collision Management:** Implementation of a simple Physics engine from scratch (handling physical collisions, gravity simulation, friction, and impacts).
- **Sound Management:** Sound handling to enhance the gameplay experience.

## More info
for more detailed information see the following presentation (in italian): [https://github.com/atabonidev/HeadSoccer/blob/main/Presentazione%20progetto.pdf](https://github.com/atabonidev/HeadSoccer/blob/main/Presentazione%20progetto.pdf))
