package repository;

import domain.Round;
import repository.Repository;

public interface RoundRepository extends Repository<Integer, Round> {

    /**
     *
     * @param username the username of the entity to be returned
     *                 username must not be null
     * @return the entity with the specified username
     *         or null - if there is no entity with the given username
     */
    Round findWordByUsername(String username);
}
