package server;

import domain.Model;
import domain.Player;
import domain.Round;
import repository.jdbc.ModelJdbcRepository;
import repository.jdbc.PlayerJdbcRepository;
import repository.jdbc.RoundJdbcRepository;
import service.IObserver;
import service.IServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServices {

    private final PlayerJdbcRepository playerRepository;
    private final ModelJdbcRepository modelRepository;
    private final RoundJdbcRepository roundRepository;

    private final Map<Integer, IObserver> loggedClients;

    public Server(PlayerJdbcRepository playerRepository, ModelJdbcRepository wordRepository, RoundJdbcRepository roundRepository) {
        this.playerRepository = playerRepository;
        this.modelRepository = wordRepository;
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
            if (loggedClients.get(playerLogin.getId()) != null){
                if(playerLogin.getId().equals(player.getId()))
                    System.out.println("Get Opponents");
                else
                    players.add(playerLogin);
            }
        }
        return players;
    }

    @Override
    public List<Model> getModels() {
        return (List<Model>) modelRepository.findAll();
    }

    @Override
    public int getNumberOfModels() {
        List<Model> words = (List<Model>) modelRepository.findAll();
        return words.size();
    }

    @Override
    public synchronized void addWord(Model word) {

        try {
            Integer freeId = 0;
            for(Model wordId: modelRepository.findAll()) {
                freeId++;
                if (!freeId.equals(wordId.getId())) {
                    break;
                }
            }
            freeId++;

            Model guessWord = new Model(word.getWord(), word.getUsername());
            guessWord.setId(freeId);
            modelRepository.save(guessWord);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Round findPosition(String username) {

        StringBuilder str = new StringBuilder();
        String word = modelRepository.findModelByUsername(username).getWord();
        str.append("_".repeat(word.length()));


        Integer freeId = 0;
        for(Round roundId: roundRepository.findAll()) {
            freeId++;
            if (!freeId.equals(roundId.getId())) {
                break;
            }
        }
        freeId++;

        Round round = new Round(username, str.toString(), 0.0);
        round.setId(freeId);

        roundRepository.save(round);

        Model model = modelRepository.findModelByUsername(username);
        model.setModel(str.toString());

        return round;
    }

    @Override
    public synchronized Round searchProposal(String username, String proposal){

        char[] charsModel = modelRepository.findModelByUsername(username).getWord().toCharArray();
        Round round = roundRepository.findWordByUsername(username);
        StringBuilder word = new StringBuilder(round.getWord());
        String wordCopy = round.getWord();

        Double pointsNo = round.getPoints();
        if(modelRepository.findModelByUsername(username).getWord().length() != proposal.length()) {
            pointsNo -= 2;
            Round roundCurrent = new Round(username, word.toString(), pointsNo);
            roundCurrent.setId(round.getId());
            roundRepository.update(roundCurrent);
            notifyAddRound(roundCurrent);
            return roundCurrent;
        }
        else
        {
            Double points = round.getPoints();
            int index = -1;
            for(char characterWord : charsModel){
                index ++;
                if(Character.valueOf(characterWord).equals(proposal.charAt(index))) {
                    word.setCharAt(index, proposal.charAt(index));
                    points += 1.0;
                }
                else {
                    if (wordCopy.contains(String.valueOf(proposal.charAt(index)))) {
                        points += 0.5;
                    }
                }
            }

            Round roundCurrent = new Round(username, word.toString(), points);
            roundCurrent.setId(round.getId());
            roundRepository.update(roundCurrent);
            notifyAddRound(roundCurrent);
            return roundCurrent;
        }
    }

    @Override
    public synchronized Double getRanking(Player player) {
        return 0.0;
    }

    private void notifyLoggedIn(Player player) {
        List<Player> players = (List<Player>) playerRepository.findAll();
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

    private void notifyAddRound(Round round){
        List<Player> players = (List<Player>) playerRepository.findAll();
        ExecutorService executor= Executors.newFixedThreadPool(players.size());

        for(Player playerNew : players){
            IObserver localClient = loggedClients.get(playerNew.getId());
            if (localClient != null)
                executor.execute(() -> {
                    try {
                        localClient.addRound(round);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        }
        executor.shutdown();
    }
}
