# guessing-game

JavaSpring project that contains 2 client-server applications structured on 7 modules with layered architecture:
- The model module comprises the data access layer with its validation (domain package).
- The entity data has been mapped with `Hibernate ORM` (Object Relational Mapping).
- The persistence module comprises the persistence layer with `Java Database Connectivity` (JDBC) for accessing the relational databases from Java program (repository package) and the services module contains the business layer which is an interface of all services (service package).
- The server module implements all the functionalities required for the client module that comprises the presentation layer (controller package).
- The rest module implements all rest services for controllers.
- The data is saved in the `SQLite database` using the connection configurations from the app.config file (client and server package).
- The Java library, `Apache Log4j` was used for messages history and the all log events are retained in log files with `Loggers objects` (client and server package).
- The interaction with the user is done through a graphical interface (GUI), developed in `JavaFX` (fxml files are found in views package).

## Key concepts:
- abstraction
- encapsulation
- inheritance
- polymorphism
- validations
- exceptions
- alerts
- mapping
- reading from a database
- storing from a database
- transactions

## requirements:

Desktop applications for 3-player guessing games with a graphical interface:
- [Guess the word](https://github.com/Iri25/mpp-games-project-Iri25/tree/main/GuessWord)
- [Guess the model](https://github.com/Iri25/mpp-games-project-Iri25/tree/main/GuessModel).
  
Each player proposes a word/model consisting of at least 6 letters and each player tries to guess the words/models proposed by the other players. Player/Players who get the most points after 3 rounds, win the game. Each user can do the following:
1. Login - After successful authentication, a new window opens in which a field is displayed for entering the word proposed by the player and a <i>Start game</i> button. Only after three players log in to the application, enter their word/model and press the <i>Start game</i> button, the game will start. At the start of the game, the server will send all players the ids/usernames of the other players and the positions for each letter of the word proposed by each player.
2. Guess the word/model. Each player chooses one of the other two players and enters a letter that he believes that it appears in the word proposed by that player. After all players have submitted their proposals to the server.
3. View words/models proposed by the players at the start of the game for a specific game.
4. View the 3 proposals and the score obtained in each round for a specific game and a specific player.
