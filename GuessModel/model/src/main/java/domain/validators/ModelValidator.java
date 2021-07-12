package domain.validators;

import domain.Model;

public class ModelValidator implements Validator<Model> {

    /**
     * validate the word
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Model entity) throws ValidationException {
        String word = entity.getWord();
        String username = entity.getUsername();
        Integer id = entity.getId();

        String errors = "";

        if (word.equals(""))
            errors += "Word not valid! ";
        if (word.length() < 6)
            errors += "Word should have at least six characters! ";

        if (username.equals(""))
            errors += "Username not valid! ";
        if (username.length() < 4)
            errors += "Username should have at least four characters! ";

        if(id < 0)
            errors += "Id not valid! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);

    }
}