package rest;

import domain.Round;
import domain.Word;
import rest.services.Client;

public class MainRestClient {

    public static void main(String[] args) throws Exception {
        Client client = new Client();

        System.out.println();
        System.out.println("The entities are: ");
        for (Word entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Search the entity by id...");
        Word entityById = client.findOne(1);
        System.out.println("Entities have been successfully found!");
        System.out.println(entityById);

        System.out.println();
        System.out.println("The entities are: ");
        for (Word entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Add entities...");
        Word entityAdd = new Word("mere", "iri25");
        entityAdd.setId(5);
        client.save(entityAdd);
        System.out.println("Entities have been successfully added!");
        System.out.println(entityAdd);

        System.out.println();
        System.out.println("The entities are: ");
        for (Word entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Update entities...");
        Word entityUpdate = new Word("pere", "iri25");
        entityUpdate.setId(5);
        client.update(entityUpdate);
        System.out.println("Entities have been successfully updated!");
        System.out.println(entityUpdate);

        System.out.println();
        System.out.println("The entities are: ");
        for (Word entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Delete entities...");
        client.delete(5);
        System.out.println("Entities have been successfully deleted!");
        System.out.println();

        System.out.println();
        System.out.println("The entities are: ");
        for (Word entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Search the entity by round and username...");
        Round entityByIdAndUsername = client.findOneByUsername(1, "teo25");
        System.out.println("Entities have been successfully found!");
        System.out.println(entityByIdAndUsername);

    }
}

