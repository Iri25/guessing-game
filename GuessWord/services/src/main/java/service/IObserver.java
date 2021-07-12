package service;

import domain.Player;
import domain.Round;
import domain.Word;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {

    void loggedIn(Player player) throws RemoteException;

    void loggedOut(Player player) throws RemoteException;

    void newGame(Round round) throws RemoteException;
}
