package repository.jdbc;

import domain.Model;
import domain.validators.ModelValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.ModelRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ModelJdbcRepository implements ModelRepository {

    private ModelValidator wordValidator;
    private final JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public ModelJdbcRepository(Properties properties, ModelValidator wordValidator) {
        this.jdbcUtils = new JdbcUtils(properties);
        this.wordValidator = wordValidator;
    }


    @Override
    public Model findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Model word = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Models WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String guessWord = resultSet.getString("word");
                    String username = resultSet.getString("username");

                    word = new Model(guessWord, username);
                    word.setId(id);

                    return word;
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with id {}", id);
        return word;
    }

    @Override
    public Iterable<Model> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Model> words = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Models")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String guessWord = resultSet.getString("word");
                    String username = resultSet.getString("username");

                    Model word = new Model(guessWord, username);
                    word.setId(id);

                    words.add(word);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(words);
        return words;
    }

    @Override
    public void save(Model entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Models VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getWord());
            preparedStatement.setString(3, entity.getUsername());
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Models WHERE id = ?")){
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Model entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Models SET word  = ?, username = ? WHERE id = ?")) {
            preparedStatement.setString(1, entity.getWord());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setInt(3, entity.getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Model findModelByUsername(String username) {
        logger.traceEntry("Finding task with username and password {} ", username);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Models WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String word = resultSet.getString("word");
                    username = resultSet.getString("username");
                    Integer id = resultSet.getInt("id");

                    Model wordGuess = new Model(word, username);
                    wordGuess.setId(id);

                    return wordGuess;
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with username {}, ", username);
        return null;
    }
}
