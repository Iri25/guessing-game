package repository;

import domain.Model;

public interface ModelRepository extends Repository<Integer, Model> {

    /**
     *
     * @param username the username of the entity to be returned
     *                 username must not be null
     * @return the entity with the specified username
     *         or null - if there is no entity with the given username
     */
    Model findModelByUsername(String username);
}
