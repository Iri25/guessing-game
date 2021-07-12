import domain.Word;
import domain.validators.WordValidator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.hbm.WordHbmRepository;

import java.util.Date;
import java.util.List;

public class MainORMHibernate {

    public static void main(String[] args) {

        WordValidator wordValidator = new WordValidator();
        WordHbmRepository wordHbmRepository = new WordHbmRepository(wordValidator);

        try {
            Word entity1 = new Word("alleluia", "dimitrie");
            entity1.setId(10);

            Word entity2 = new Word("hello", "dimitrie");
            entity2.setId(11);

            Word entity3 = new Word("here", "teo25");
            entity3.setId(12);

            System.out.println();
            System.out.println("The entities are: ");
            wordHbmRepository.findAll();

            System.out.println();
            System.out.println("Add entities...");
            wordHbmRepository.save(entity1);
            wordHbmRepository.save(entity2);
            wordHbmRepository.save(entity3);
            System.out.println("Entities have been successfully added!");

            System.out.println();
            System.out.println("The entities are: ");
            wordHbmRepository.findAll();

            System.out.println();
            System.out.println("Update entities...");
            entity2.setUsername("teo25");
            wordHbmRepository.update(entity2);
            System.out.println("Entities have been successfully updated!");

            System.out.println();
            System.out.println("The entities are: ");
            wordHbmRepository.findAll();

            System.out.println();
            System.out.println("Delete entities...");
            wordHbmRepository.delete(entity3.getId());
            System.out.println("Entities have been successfully deleted!");

            System.out.println();
            System.out.println("The entities are: ");
            wordHbmRepository.findAll();

            System.out.println();
            System.out.println("Search the entity by id...");
            wordHbmRepository.findOne(10);
            System.out.println("Entities have been successfully found!");

            System.out.println();
            System.out.println("The entities are: ");
            wordHbmRepository.findAll();
        }
        catch (Exception e){
            System.err.println("Exception " + e);
            e.printStackTrace();
        }
        finally {
        WordHbmRepository.close();
        }
    }
}
