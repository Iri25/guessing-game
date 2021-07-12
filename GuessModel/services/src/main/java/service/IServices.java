package service;

import domain.Player;
import domain.Round;
import domain.Model;

import java.util.List;

public interface IServices {

    Player getUser(Player player);

    void login(Player player, IObserver client) throws Exception;

    void logout(Player player, IObserver client) throws Exception;

    List<Player> getOpponents(Player player);

    List<Model> getModels();

    int getNumberOfModels();

    void addWord(Model word);

    Round findPosition(String username);

    Round searchProposal(String username, String proposal);

    Double getRanking(Player player);

}
