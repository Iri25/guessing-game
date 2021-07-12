package service;

import domain.Model;
import domain.Player;
import domain.Round;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {

    void loggedIn(Player player) throws RemoteException;

    void loggedOut(Player player) throws RemoteException;

    void addRound(Round round) throws RemoteException;

}
