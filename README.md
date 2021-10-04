# gruppe104-ATeam-projekt3_Siedler
## Siedler von Catan

# To start the game:
To start the game you have to call the main method in the IDE(eclipse/intelliJ) in class GameSequence
start GameSequence-main-methode.

First you have to specify the number of players. It's a game for 2 to 4 players. In the following short example it's a game with 2 players.
# Startphase
Player one can now set his first settlement and immediately the first road. Now it's player's 2 turn to placeInitialSettlement and placeInitialRoad. Player two once again, as well as player one.
To set a structure you have to enter first the x coordinate, then the y coordinate of the location, if you build a road, give both coordinates twice, for the startposition and the endposition of the road. 
It's easy, just enter what the prompter commands you to, to move forward in the  game.
# Game
After phase 1 and phase 2, you can roll the dice and build as you like, with available resources. For every built settlement you earn one point, for every city you earn one more. A settlement can be developed into a city. Every time when the turn goes to the next player he throws the dice and if you are lucky you can earn some resources. But if someone rolls a 7, a robber comes and steals half of your resources, if you have more than 7.
There are extra points if a player is in possession of the longest road.
The game ends when a player reaches the maximum score of 7.

If it's your turn the prompter ask you what you want to do. The selection you have:

```1: build```           you can build ```1: SETTLEMENT```, ```2: CITY```, ```3: ROAD```

```2: trade```           trade your resources with the bank with a ratio 4:1

```3: finish turn```    no more activities desired? get the dice to the next player

```4: show board```     displays the board with all the set buildings and roads

```5: show resouces```  displays your currently available resources

for playing also good to know: the cost for the different structures:
- Road: 1x WOOD, 1x CLAY 
- Settlement: 1x WOOD, 1x CLAY, 1x WOOL, 1x GRAIN
- City: 3x STONE, 2x GRAIN
 
and you __need__ to have a coordinate sheet at hand (see below)                                         
                                        


The official and detailed rules of *The Siedler of catan* can you read [here](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=12&ved=2ahUKEwiM6Nq8tqDmAhUG3qQKHaIgDFsQFjALegQIAxAC&url=https%3A%2F%2Fwww.catan.de%2Ffiles%2Fdownloads%2F4002051693602_catan_-_das_spiel_0.pdf&usg=AOvVaw2MNR7CWP5HNokU1vrm-k27)
## the empty playing field:
![empty playing field](https://github.zhaw.ch/pm1-it19azh-ehri-fame-muon/gruppe104-ATeam-projekt3_Siedler/blob/master/doc/gamempty_noard.png) 
## the coordinate sheet:
![coordinate sheet](https://github.zhaw.ch/pm1-it19azh-ehri-fame-muon/gruppe104-ATeam-projekt3_Siedler/blob/master/doc/game%20board%20with%20coordinates.png) 

## Authors

* **Samuel Stalder**
* **Joel Plambeck**
* **Sophie Daenzer**
* **Dario Züllig**

