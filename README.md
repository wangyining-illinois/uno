## sp21-cs242-assignment1.0
This is an UNO game simulator. 
To start a game, simple navigate to src/uno/Main.java and run the main function. <br/>
This game is interactive, which means the player's card, current game state, and instructions are shown inside the terminal. 
Players can enter numbers to play out a card, or type the color to set colors if they have played out a WILD or WILD DRAW FOUR. 
For example, if you want to play out a card with index 1, just type 1 and press enter. <br/>
For this project, I have implemented five classes. 
* Util is for defining stuff. It is not functional comparing to other classes. 
* Card represents a card in UNO, so in a game there would be 108 cards and they would be either in pile or in players' hands. 
* Pile represents the discard pile and the draw pile in UNO. Once the draw pile is empty, the Pile Class will automatically reuse all cards from the discard pile. 
* Player represents a single player in the game. This Class is responsible for keeping track of all cards in the player's hand. 
* The Server Class contains the main game loop. It has all the logic of the game and is responsible for the interaction with the player. 

## sp21-cs242-assignment1.1
This branch contains GUIs of the UNO game simulator. For this project, I have created three classes.
* startGUI is the game start scene. It will allow the user to enter a valid number (2-10) indicating the number of players 
  in this game. Any invalid input will be rejected. After clicking the Play button, the game will start.
* Once the game starts, a gameGUI object will be rendered. gameGUI is the main game scene where players can see the current 
  game state and make moves. The current game states including current color, number, special type and stacked penalty
  are shown on the top. The draw pile and discard pile are in the middle of the scene. The numbers of cards in both pile
  are also displayed. The current player and his/her cards are displayed on the bottom. The cards are buttons which the 
  current player can click on to play out the card. The selected card must be valid according to the current game state. 
  Otherwise it will be rejected. On the right hand side of the hand cards are draw button and hide/reveal button. 
  Player can press the draw button to draw a card, or press hide/reveal button to hide or reveal hand cards.
* Once a player has no card, he/she will be the winner of the game. By then, the game will switch to the winner scene 
  and an winnerGUI object will be rendered. This scene will display the winner of the game. 
  
## sp21-cs242-assignment1.2
* This branch contains the complete UNO game simulator built with MVC model. <br>
* The uno package contains the model, which contains all the game logic and components in the game, such as Card, Player etc. 
The Server class in uno package is where the components and logic of the game are combined together, and Server is the class
which the Controller will communicate with. This package contains BaselineAI, which extends Player. It represents 
a naive AI that can participate in the game. This package also contains StrategicAI, which extends BaselineAI. 
It overrides some of the logic for choosing color and cards in BaselineAI. <br>
* The unogui package is the view, which contains the static GUI of uno. In unogui, StartPanel, GamePanel and WinnerPanel are
three classes that extends JPanel. They represents the three scenes in the game. The GUI class is an extension of JFrame, which
can toggle among the three JPanels. The GUI class is the class which the Controller will communicate with.<bc>
* The unocontroller package is the controller, which connects the View and the Model. The Controller class will construct a GUI
object and a Server Object, and it communicates with both of them by adding action listeners to buttons in GUI, and by calling
helper functions in Server to change its states when the buttons in GUI are pressed. 