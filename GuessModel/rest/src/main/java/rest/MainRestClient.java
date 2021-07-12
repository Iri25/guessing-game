package rest;

import domain.Model;
import domain.Round;
import rest.services.Client;

public class MainRestClient {

    public static void main(String[] args) throws Exception {
        Client client = new Client();

        System.out.println();
        System.out.println("The entities are: ");
        for (Model entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Search the entity by id...");
        Model entityById = client.findOne(1);
        System.out.println("Entities have been successfully found!");
        System.out.println(entityById);

        System.out.println();
        System.out.println("The entities are: ");
        for (Model entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Add entities...");
        Model entityAdd = new Model("*^^^^*", "iri25");
        entityAdd.setId(5);
        client.save(entityAdd);
        System.out.println("Entities have been successfully added!");
        System.out.println(entityAdd);

        System.out.println();
        System.out.println("The entities are: ");
        for (Model entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Update entities...");
        Model entityUpdate = new Model("^^~~~~", "iri25");
        entityUpdate.setId(5);
        client.update(entityUpdate);
        System.out.println("Entities have been successfully updated!");
        System.out.println(entityUpdate);

        System.out.println();
        System.out.println("The entities are: ");
        for (Model entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Delete entities...");
        client.delete(5);
        System.out.println("Entities have been successfully deleted!");
        System.out.println();

        System.out.println();
        System.out.println("The entities are: ");
        for (Model entity : client.findAll()) {
            System.out.println(entity);
        }

        System.out.println();
        System.out.println("Search entities by round and username");
        client.findOneByUsername(1, "iri25");
        System.out.println("Entities have been successfully deleted!");
        System.out.println();

    }
}

