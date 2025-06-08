❄️ Snowman Game ❄️
🎮 Overview
Snowman is a charming and strategic puzzle game where you control a monster tasked with pushing and combining snowballs to create complete snowmen. Each level introduces new challenges, requiring thoughtful planning and smart moves to progress.

✨ Features
Engaging Gameplay
Control a monster to push and combine snowballs of different sizes on a grid.

Multiple Levels
Explore a variety of thoughtfully designed levels, increasing in complexity.

Unique Snowball Mechanics

Three snowball sizes: Small, Medium, Large

Combine snowballs to form partial and full snowmen

Collect snow on the ground to grow snowballs

Intuitive Controls

Arrow keys for movement

CTRL + Z to undo moves

CTRL + X to redo moves

R to restart the level

Score Tracking & Leaderboard
Keep track of your moves and compete for the best score.

Save & Load Progress
Save your game state anytime and pick up where you left off.

⚙️ Technical Architecture
Core Modules
Component	Description
BoardModel	Handles the game state, board layout, and elements
GameState	Manages undo/redo snapshots and game history
Snowball	Implements snowball behaviors and combination logic
Monster	Controls player movement and interactions

Key Systems
State Management — Undo, redo, and reset level functionality

Movement & Collision — Ensures valid moves within board boundaries

Snowball Mechanics — Growth and combination rules for snowballs

Persistence — Save/load game progress and leaderboard data

🚀 Getting Started
Prerequisites
Java 17 or later

JavaFX runtime installed and configured

Installation Steps
bash
Copiar
Editar
git clone https://github.com/angelodias01/Snowman.git
cd snowman-game

# Open in your favorite Java IDE, build and run
🎮 How to Play
Action	Control
Move Monster	Arrow Keys
Undo Move	CTRL + Z
Redo Move	CTRL + X
Restart Level	R

Push snowballs to combine them and build snowmen.

Collect snow from the ground to grow snowballs.

Use undo/redo to refine your strategy.

🧊 Game Rules & Snowball Combinations
Snowball Sizes:
Small (S)

Medium (M)

Large (L)

Combining Snowballs:
Combination	Result
Small + Medium	Medium-Small (partial)
Small + Large	Large-Small (partial)
Medium + Large	Large-Medium (partial)

Creating Complete Snowmen:
Combination	Result
Large-Medium + Small	Complete Snowman
Large-Small + Medium	Complete Snowman
Medium-Small + Large	Complete Snowman

💾 Save System
Saved games stored in Documents/Snowman/

Each save contains:

Board layout and snowball positions

Monster location

Movement history

Leaderboard tracks player names, scores, and completion times.

🌟 Future Enhancements
More challenging levels

Level editor for custom puzzles

Multiplayer modes

Achievement and reward system

Immersive sound effects and background music

👥 Authors
Ângelo Dias

Edgar Brito

🤝 Contribution & Contact
Found a bug or want to add a feature? Open an issue or submit a pull request — contributions are welcome!
