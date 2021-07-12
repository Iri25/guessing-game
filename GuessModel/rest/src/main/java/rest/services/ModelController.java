package rest.services;

import domain.Model;
import domain.validators.ModelValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.jdbc.rest.ModelJdbcRestRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/models")
public class ModelController {

    private final ModelValidator wordValidator = new ModelValidator();
    private final ModelJdbcRestRepository wordRepository = new ModelJdbcRestRepository(wordValidator);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@PathVariable Integer id) {
        Model entity = wordRepository.findOne(id);
        if (entity == null)
            return new ResponseEntity<String>("Entity not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Model>(entity, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Model[] findAll() {
        int size = (int) StreamSupport.stream(wordRepository.findAll().spliterator(), false).count();
        Model[] entities = new Model[size];
        entities = ((List<Model>) wordRepository.findAll()).toArray(entities);
        return entities;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Model save(@RequestBody Model entity) {
        wordRepository.save(entity);
        return entity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            wordRepository.delete(id);
            return new ResponseEntity<Model>(HttpStatus.OK);
        }
        catch(Exception repositoryException){
            return new ResponseEntity<String>(repositoryException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Model update(@RequestBody Model entity) {
        wordRepository.update(entity);
        return entity;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(Exception e) {
        return e.getMessage();
    }
}
