package repository;

import domain.Word;
import repository.Repository;

public interface WordRepository extends Repository<Integer, Word> {

    /**
     *
     * @param username the username of the entity to be returned
     *                 username must not be null
     * @return the entity with the specified username
     *         or null - if there is no entity with the given username
     */
    Word findWordByUsername(String username);
}
