package server;

import domain.Player;
import domain.Round;
import domain.Word;
import repository.jdbc.PlayerJdbcRepository;
import repository.jdbc.RoundJdbcRepository;
import repository.jdbc.WordJdbcRepository;
import service.IObserver;
import service.IServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServices {

    private final PlayerJdbcRepository playerRepository;
    private final WordJdbcRepository wordRepository;
    private final RoundJdbcRepository roundRepository;

    private final Map<Integer, IObserver> loggedClients;

    public Server(PlayerJdbcRepository playerRepository, WordJdbcRepository wordRepository, RoundJdbcRepository roundRepository) {
        this.playerRepository = playerRepository;
        this.wordRepository = wordRepository;
        this.roundRepository = roundRepository;
        loggedClients = new ConcurrentHashMap<>();
    }


    @Override
    public Player getUser(Player player) {
        return playerRepository.findOneByUsernamePassword(player.getUsername(), player.getPassword());
    }

    @Override
    public synchronized void login(Player player, IObserver client) throws Exception {
        if(player != null) {
            if (loggedClients.get(player.getId()) != null)
                throw new Exception("User already logged in!");
            loggedClients.put(player.getId(), client);
            notifyLoggedIn(player);
        }
        else
            throw new Exception("Authentication failed!");
    }

    @Override
    public synchronized void logout(Player player, IObserver client) throws Exception {
        if (!loggedClients.containsKey(player.getId()))
            throw new Exception("User already logout!");

        IObserver localClient = loggedClients.remove(player.getId());
        if (localClient == null)
            throw new Exception("User " + player.getUsername() + " is not logged in.");
        notifyLoggedOut(player);
    }

    @Override
    public synchronized List<Player> getOpponents(Player player) {
        List<Player> players = new ArrayList<>();
        for (Player playerLogin:playerRepository.findAll()) {
            if (loggedClients.get(playerLogin.getId()) != null && !playerLogin.getId().equals(player.getId())){
                players.add(player);
            }
        }
        return players;
    }

    @Override
    public synchronized void addWord(Word word) {

        try {
            Integer freeId = 0;
            for(Word wordId: wordRepository.findAll()) {
                freeId++;
                if (!freeId.equals(wordId.getId())) {
                    break;
                }
            }
            freeId++;

            Word guessWord = new Word(word.getWord(), word.getUsername());
            guessWord.setId(freeId);
            wordRepository.save(guessWord);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Round findPosition(String username) {

        StringBuilder str = new StringBuilder();
        String word = wordRepository.findWordByUsername(username).getWord();
        str.append("_".repeat(word.length()));


        Integer freeId = 0;
        for(Round roundId: roundRepository.findAll()) {
            freeId++;
            if (!freeId.equals(roundId.getId())) {
                break;
            }
        }
        freeId++;

        Round round = new Round(username, str.toString(), 0);
        round.setId(freeId);

        roundRepository.save(round);
        return round;
    }

    @Override
    public synchronized Round searchLetter(String username, char letter){

        char[] charsWord = wordRepository.findWordByUsername(username).getWord().toCharArray();
        Round round = roundRepository.findWordByUsername(username);

        StringBuilder word = new StringBuilder(round.getWord());
        Integer points = round.getPoints();
        int index = -1;
        for(char characterWord : charsWord){
            index ++;
            if(Character.valueOf(characterWord).equals(letter)) {
                word.setCharAt(index, letter);
                points ++;
            }
        }
        Round roundCurrent = new Round(username, word.toString(), points);
        roundCurrent.setId(round.getId());
        roundRepository.update(roundCurrent);
        notifyGame(roundCurrent);
        return roundCurrent;
    }

    @Override
    public synchronized int getRanking(Player player) {
        return 0;
    }

    private void notifyLoggedIn(Player player) {
        List<Player> players = getOpponents(player);
        if(players.size() != 0) {
            ExecutorService executor = Executors.newFixedThreadPool(players.size());

            for (Player playerLogin : players) {
                IObserver localClient = loggedClients.get(playerLogin.getId());
                if (localClient != null)
                    executor.execute(() -> {
                        try {
                            localClient.loggedIn(player);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            }
            executor.shutdown();
        }
    }

    private void notifyLoggedOut(Player player) {
        List<Player> players = (List<Player>) playerRepository.findAll();
        ExecutorService executor= Executors.newFixedThreadPool(players.size());

        for(Player playerLogout : players){
            IObserver localClient = loggedClients.get(playerLogout.getId());
            if (localClient!=null)
                executor.execute(() -> {
                    try {
                        localClient.loggedOut(player);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        }
        executor.shutdown();
    }

    private void notifyGame(Round round){
        List<Player> players = (List<Player>) playerRepository.findAll();
        ExecutorService executor= Executors.newFixedThreadPool(2);

        for(Player playerNew :players){
            IObserver localClient = loggedClients.get(playerNew.getId());
            if (localClient != null)
                executor.execute(() -> {
                    try {
                        localClient.newGame(round);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        }
        executor.shutdown();
    }
}
