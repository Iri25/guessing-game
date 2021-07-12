package rest.services;

import domain.Word;
import domain.validators.WordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.jdbc.rest.WordJdbcRestRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/words")
public class WordController {

    private final WordValidator wordValidator = new WordValidator();
    private final WordJdbcRestRepository wordRepository = new WordJdbcRestRepository(wordValidator);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@PathVariable Integer id) {
        Word entity = wordRepository.findOne(id);
        if (entity == null)
            return new ResponseEntity<String>("Entity not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Word>(entity, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Word[] findAll() {
        int size = (int) StreamSupport.stream(wordRepository.findAll().spliterator(), false).count();
        Word[] entities = new Word[size];
        entities = ((List<Word>) wordRepository.findAll()).toArray(entities);
        return entities;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Word save(@RequestBody Word entity) {
        wordRepository.save(entity);
        return entity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            wordRepository.delete(id);
            return new ResponseEntity<Word>(HttpStatus.OK);
        }
        catch(Exception repositoryException){
            return new ResponseEntity<String>(repositoryException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Word update(@RequestBody Word entity) {
        wordRepository.update(entity);
        return entity;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(Exception e) {
        return e.getMessage();
    }
}
