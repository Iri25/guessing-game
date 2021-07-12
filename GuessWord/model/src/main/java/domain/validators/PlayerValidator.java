package domain.validators;

import domain.Player;

public class PlayerValidator implements Validator<Player> {

    /**
     * validate the player
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Player entity) throws ValidationException {
        String username = entity.getUsername();
        String password = entity.getPassword();
        Integer id = entity.getId();

        String errors = "";

        if (username.equals(""))
            errors += "Username not valid! ";
        if (username.length() < 4)
            errors += "Username should have at least four characters! ";

        if (password.equals(""))
            errors += "Password not valid! ";
        if (password.length() < 4)
            errors += "Password should have at least four characters! ";

        if(id < 0)
            errors += "Id not valid! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);

    }
}