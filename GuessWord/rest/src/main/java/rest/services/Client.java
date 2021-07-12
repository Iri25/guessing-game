package rest.services;

import domain.Round;
import domain.Word;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class Client {
    public static final String URL = "http://localhost:8080/words";
    public static final String URN = "http://localhost:8080/rounds";

    private final RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        }
        catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Word findOne(Integer id) throws Exception {
        return execute(() -> restTemplate.getForObject(URL + '/' + id.toString(), Word.class));
    }

    public Word[] findAll() throws Exception {
        return execute(() -> restTemplate.getForObject(URL, Word[].class));
    }

    public Word save(Word entity) throws Exception {
        return execute(() -> restTemplate.postForObject(URL, entity, Word.class));
    }

    public void delete(Integer id) throws Exception {
        execute(() -> {
            restTemplate.delete(URL + '/' + id.toString());
            return null;
        });
    }

    public Word update(Word entity) throws Exception {
        execute(() -> {
            restTemplate.put(URL, entity);
            return null;
        });
        return findOne(entity.getId());
    }

    public Round findOneByUsername(Integer id, String username) throws Exception {
        return execute(() -> restTemplate.getForObject(URN + '/' + id.toString() + '/' + username, Round.class));
    }
}


