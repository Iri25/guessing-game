package repository.jdbc.rest;

import domain.Word;
import domain.validators.WordValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.WordRepository;
import repository.jdbc.JdbcUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@org.springframework.stereotype.Repository
public class WordJdbcRestRepository implements WordRepository {

    private WordValidator wordValidator;
    private final JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public WordJdbcRestRepository(WordValidator wordValidator) {
        this.wordValidator = wordValidator;

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Users\\Andreea\\Desktop\\GuessWord\\app.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Word findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Word word = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Words WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String guessWord = resultSet.getString("word");
                    String username = resultSet.getString("username");

                    word = new Word(guessWord, username);
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
    public Iterable<Word> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Word> words = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Words")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String guessWord = resultSet.getString("word");
                    String username = resultSet.getString("username");

                    Word word = new Word(guessWord, username);
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
    public void save(Word entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Words VALUES (?, ?, ?)")) {
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

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Words WHERE id = ?")){
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
    public void update(Word entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Words SET word  = ?, username = ? WHERE id = ?")) {
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
    public Word findWordByUsername(String username) {
        logger.traceEntry("Finding task with username and password {} ", username);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Words WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String word = resultSet.getString("word");
                    username = resultSet.getString("username");
                    Integer id = resultSet.getInt("id");

                    Word wordGuess = new Word(word, username);
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

