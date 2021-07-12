package rest.services;

import domain.Round;
import domain.validators.RoundValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.jdbc.rest.RoundJdbcRestRepository;


import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/rounds")
public class RoundController {

    private final RoundValidator roundValidator = new RoundValidator();
    private final RoundJdbcRestRepository roundRepository = new RoundJdbcRestRepository(roundValidator);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@PathVariable Integer id) {
        Round entity = roundRepository.findOne(id);
        if (entity == null)
            return new ResponseEntity<String>("Entity not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Round>(entity, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Round[] findAll() {
        int size = (int) StreamSupport.stream(roundRepository.findAll().spliterator(), false).count();
        Round[] entities = new Round[size];
        entities = ((List<Round>) roundRepository.findAll()).toArray(entities);
        return entities;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Round save(@RequestBody Round entity) {
        roundRepository.save(entity);
        return entity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            roundRepository.delete(id);
            return new ResponseEntity<Round>(HttpStatus.OK);
        }
        catch(Exception repositoryException){
            return new ResponseEntity<String>(repositoryException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Round update(@RequestBody Round entity) {
        roundRepository.update(entity);
        return entity;
    }

    @RequestMapping(value = "/{id}/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> findOneByUsername(@PathVariable Integer id, @PathVariable String username) {
        Round entity = roundRepository.findOneByUsername(id, username);
        if (entity == null)
            return new ResponseEntity<String>("Entity not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Round>(entity, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(Exception e) {
        return e.getMessage();
    }
}

