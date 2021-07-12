package service;

import domain.Player;
import domain.Round;
import domain.Word;

import java.util.List;

public interface IServices {

    Player getUser(Player player);
    void login(Player player, IObserver client) throws Exception;

    void logout(Player player, IObserver client) throws Exception;

    List<Player> getOpponents(Player player);

    void addWord(Word word);

    Round findPosition(String username);

    Round searchLetter(String username, char letter);

    int getRanking(Player player);

}
