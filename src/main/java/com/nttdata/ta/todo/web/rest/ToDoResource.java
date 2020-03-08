package com.nttdata.ta.todo.web.rest;

import com.nttdata.ta.todo.domain.ToDo;
import com.nttdata.ta.todo.repository.ToDoRepository;
import com.nttdata.ta.todo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.nttdata.ta.todo.domain.ToDo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ToDoResource {

    private final Logger log = LoggerFactory.getLogger(ToDoResource.class);

    private static final String ENTITY_NAME = "toDo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToDoRepository toDoRepository;

    public ToDoResource(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    /**
     * {@code POST  /to-dos} : Create a new toDo.
     *
     * @param toDo the toDo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toDo, or with status {@code 400 (Bad Request)} if the toDo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-dos")
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDo toDo) throws URISyntaxException {
        log.debug("REST request to save ToDo : {}", toDo);
        if (toDo.getId() != null) {
            throw new BadRequestAlertException("A new toDo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToDo result = toDoRepository.save(toDo);
        return ResponseEntity.created(new URI("/api/to-dos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /to-dos} : Updates an existing toDo.
     *
     * @param toDo the toDo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDo,
     * or with status {@code 400 (Bad Request)} if the toDo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toDo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-dos")
    public ResponseEntity<ToDo> updateToDo(@RequestBody ToDo toDo) throws URISyntaxException {
        log.debug("REST request to update ToDo : {}", toDo);
        if (toDo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToDo result = toDoRepository.save(toDo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toDo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /to-dos} : get all the toDos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toDos in body.
     */
    @GetMapping("/to-dos")
    public List<ToDo> getAllToDos() {
        log.debug("REST request to get all ToDos");
        return toDoRepository.findAll();
    }

    /**
     * {@code GET  /to-dos/:id} : get the "id" toDo.
     *
     * @param id the id of the toDo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toDo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-dos/{id}")
    public ResponseEntity<ToDo> getToDo(@PathVariable Long id) {
        log.debug("REST request to get ToDo : {}", id);
        Optional<ToDo> toDo = toDoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(toDo);
    }

    /**
     * {@code DELETE  /to-dos/:id} : delete the "id" toDo.
     *
     * @param id the id of the toDo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-dos/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        log.debug("REST request to delete ToDo : {}", id);
        toDoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
