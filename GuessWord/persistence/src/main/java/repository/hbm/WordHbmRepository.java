package repository.hbm;

import domain.Word;
import domain.validators.WordValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.WordRepository;

import java.util.ArrayList;
import java.util.List;

public class WordHbmRepository implements WordRepository {

    private WordValidator wordValidator;
    private static final Logger logger= LogManager.getLogger();

    static SessionFactory sessionFactory;

    public WordHbmRepository(WordValidator wordValidator) {
        this.wordValidator = wordValidator;
        initialize();
    }

    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public static void close() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    @Override
    public Word findOne(Integer id) {
        logger.traceEntry("Find task with {}", id);


        List<Word> words = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                words = session.createQuery("FROM Words WHERE id = ID", Word.class)
                        .setParameter("id", id).list();
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return words.get(0);
    }

    @Override
    public Iterable<Word> findAll() {
        logger.traceEntry();

        List<Word> words = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                words = session.createQuery("FROM Words", Word.class).list();
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
        return words;
    }

    @Override
    public void save(Word entity) {
        logger.traceEntry("Saving task {}", entity);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(entity);
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public void delete(Integer id){
        logger.traceEntry("Deleting task with {}", id);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(id);
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
    }

    @Override
    public void update(Word entity){
        logger.traceEntry("Updating task {}", entity);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Word> word = session.createQuery("FROM Words SET word = WORD, username = USERNAME WHERE id = ID", Word.class)
                        .setParameter("id", entity.getId())
                        .setParameter("word", entity.getWord())
                        .setParameter("username", entity.getUsername())
                        .list();
                session.update(entity);
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    @Override
    public Word findWordByUsername(String username) {
        logger.traceEntry("Finding task with username {}", username);

        List<Word> result = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                result = session.createQuery
                        (" FROM Words WHERE username = :USERNAME", Word.class)
                        .setParameter("USERNAME", username).list();
                transaction.commit();
            }
            catch (RuntimeException exception) {
                if (transaction != null) transaction.rollback();
            }
        }
        return result.get(0);
    }
}
