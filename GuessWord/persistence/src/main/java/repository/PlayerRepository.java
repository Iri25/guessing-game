package repository;

import domain.Player;
import domain.validators.ValidationException;
import repository.Repository;

public interface PlayerRepository extends Repository<Integer, Player> {

    /**
     *
     * @param username the username of the entity to be returned
     *                 username must not be null
     * @param password the entity with the specified password
     *                 password must not be null
     * @return the entity with the specified username and password
     *         or null - if there is no entity with the given username and password
     */
    Player findOneByUsernamePassword(String username, String password);

}

