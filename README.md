# Snowman Game

## Overview
Snowman is an engaging puzzle game where players control a monster to push and combine snowballs to create snowmen. The game features multiple levels of increasing difficulty, with a focus on strategic thinking and planning.

## Features
- **Interactive Gameplay**: Control a monster to push and combine snowballs
- **Multiple Levels**: Progress through various challenging board layouts
- **Snowball Mechanics**:
    - Three sizes of snowballs (Small, Medium, Large)
    - Combine snowballs to create partial and complete snowmen
    - Collect snow from the ground to increase snowball size
- **Game Controls**:
    - Arrow keys for monster movement
    - CTRL+Z for undo
    - CTRL+X for redo
    - R key to restart level
- **Score System**: Track moves and maintain a leaderboard
- **Save System**: Save game progress and state

## Technical Implementation
### Core Components
- **BoardModel**: Manages game state and logic
- **GameState**: Handles state snapshots for undo/redo functionality
- **Snowball**: Implements snowball behavior and combinations
- **Monster**: Controls player movement and interactions

### Key Features
1. **State Management**
    - Undo/Redo system
    - Level reset capability
    - Game state persistence

2. **Movement System**
    - Collision detection
    - Valid move verification
    - Board boundary checking

3. **Snowball Mechanics**
    - Size progression (Small → Medium → Large)
    - Combination rules
    - Snowman formation logic

4. **User Interface**
    - Visual board representation
    - Movement feedback
    - Score display
    - Game controls

## Getting Started
### Prerequisites
- Java 17 or higher
- JavaFX runtime

### Installation
1. Clone the repository
2. Build the project using your preferred Java IDE
3. Run the main application class

### Controls
- **Arrow Keys**: Move the monster
- **CTRL+Z**: Undo last move
- **CTRL+X**: Redo last undone move
- **R**: Restart current level

## Game Rules
1. Push snowballs to combine them
2. Create a complete snowman by combining:
    - Small + Medium → Medium-Small
    - Small + Large → Large-Small
    - Medium + Large → Large-Medium
3. Final combinations for complete snowman:
    - Large-Medium + Small
    - Large-Small + Medium
    - Medium-Small + Large

## Save System
- Games can be saved to the Documents folder
- Leaderboard tracks player scores and completion times
- Each save includes:
    - Board state
    - Movement history
    - Snowball positions
    - Monster location

## Future Enhancements
- Additional levels
- Custom level creation
- Multiplayer support
- Achievement system
- Sound effects and background music

## Authors
- Ângelo Dias
- Edgar Brito