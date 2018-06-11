# jTicTacToe

Java implementation of the game [Tic-Tac-Toe](https://en.wikipedia.org/wiki/Tic-tac-toe) using Java SE 8 and JavaFX

Features 3 different kinds of opponents:
* RandomAI - Randomly selects an available square
* MaxWinsAI - Selects the square which gives the best score based on winning and losing
* MinimaxAI - Selects the square based on the [Minimax algorithm](https://en.wikipedia.org/wiki/Minimax)

## Getting Started

[Link to Latest Release](https://github.com/rodrickjones/jtictactoe/releases/latest)

Download the precompiled JAR or download the source, compile it, and run it using your java JRE.

### Parameters

| Parameter | Values | Default | Description | Example |
| :---: | :---: | :---: | :---: | :---: | 
| --difficulty | easy, medium, hard, impossible | hard | The difficulty of the AI opponent | --difficulty=impossible
| --theme | dark, light | light | The style theme to use for the application | --theme=dark

E.g. if you want to verse the impossible AI with the Dark theme, use ``` java -jar jtictactoe.jar --difficulty=impossible --theme=dark``` via the command line

### Prerequisites

[Java JRE 8+](https://java.com/en/download/)
