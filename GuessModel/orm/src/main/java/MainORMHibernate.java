import domain.Model;
import domain.validators.ModelValidator;
import repository.hbm.WordHbmRepository;

public class MainORMHibernate {

    public static void main(String[] args) {

        ModelValidator wordValidator = new ModelValidator();
        WordHbmRepository wordHbmRepository = new WordHbmRepository(wordValidator);

        try {
            Model entity1 = new Model("-----~", "dimitrie");
            entity1.setId(10);

            Model entity2 = new Model("~~~~~~", "dimitrie");
            entity2.setId(11);

            Model entity3 = new Model("^^^^^^", "teo25");
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
