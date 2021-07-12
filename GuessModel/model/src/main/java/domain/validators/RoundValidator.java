package domain.validators;

import domain.Round;

public class RoundValidator implements Validator<Round>{

    /**
     * validate the round
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Round entity) throws ValidationException {
        String username = entity.getUsername();
        String word = entity.getWord();
        Double points = entity.getPoints();
        Integer id = entity.getId();

        String errors = "";

        if (username.equals(""))
            errors += "Username not valid! ";
        if (username.length() < 4)
            errors += "Username should have at least four characters! ";

        if (word.equals(""))
            errors += "Word not valid! ";
        if (word.length() < 4)
            errors += "Word should have at least six characters! ";

        if(points < 0)
            errors += "Points not valid! ";

        if(id < 0)
            errors += "Id not valid! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);

    }
}
