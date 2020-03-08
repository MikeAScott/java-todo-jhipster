package com.nttdata.ta.todo.web.rest;

import com.nttdata.ta.todo.TodoApp;
import com.nttdata.ta.todo.domain.ToDo;
import com.nttdata.ta.todo.repository.ToDoRepository;
import com.nttdata.ta.todo.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.nttdata.ta.todo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ToDoResource} REST controller.
 */
@SpringBootTest(classes = TodoApp.class)
public class ToDoResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restToDoMockMvc;

    private ToDo toDo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ToDoResource toDoResource = new ToDoResource(toDoRepository);
        this.restToDoMockMvc = MockMvcBuilders.standaloneSetup(toDoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDo createEntity(EntityManager em) {
        ToDo toDo = new ToDo()
            .description(DEFAULT_DESCRIPTION)
            .byDate(DEFAULT_BY_DATE)
            .done(DEFAULT_DONE);
        return toDo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDo createUpdatedEntity(EntityManager em) {
        ToDo toDo = new ToDo()
            .description(UPDATED_DESCRIPTION)
            .byDate(UPDATED_BY_DATE)
            .done(UPDATED_DONE);
        return toDo;
    }

    @BeforeEach
    public void initTest() {
        toDo = createEntity(em);
    }

    @Test
    @Transactional
    public void createToDo() throws Exception {
        int databaseSizeBeforeCreate = toDoRepository.findAll().size();

        // Create the ToDo
        restToDoMockMvc.perform(post("/api/to-dos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDo)))
            .andExpect(status().isCreated());

        // Validate the ToDo in the database
        List<ToDo> toDoList = toDoRepository.findAll();
        assertThat(toDoList).hasSize(databaseSizeBeforeCreate + 1);
        ToDo testToDo = toDoList.get(toDoList.size() - 1);
        assertThat(testToDo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testToDo.getByDate()).isEqualTo(DEFAULT_BY_DATE);
        assertThat(testToDo.isDone()).isEqualTo(DEFAULT_DONE);
    }

    @Test
    @Transactional
    public void createToDoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toDoRepository.findAll().size();

        // Create the ToDo with an existing ID
        toDo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToDoMockMvc.perform(post("/api/to-dos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDo)))
            .andExpect(status().isBadRequest());

        // Validate the ToDo in the database
        List<ToDo> toDoList = toDoRepository.findAll();
        assertThat(toDoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllToDos() throws Exception {
        // Initialize the database
        toDoRepository.saveAndFlush(toDo);

        // Get all the toDoList
        restToDoMockMvc.perform(get("/api/to-dos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDo.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].byDate").value(hasItem(DEFAULT_BY_DATE.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getToDo() throws Exception {
        // Initialize the database
        toDoRepository.saveAndFlush(toDo);

        // Get the toDo
        restToDoMockMvc.perform(get("/api/to-dos/{id}", toDo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toDo.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.byDate").value(DEFAULT_BY_DATE.toString()))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingToDo() throws Exception {
        // Get the toDo
        restToDoMockMvc.perform(get("/api/to-dos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateToDo() throws Exception {
        // Initialize the database
        toDoRepository.saveAndFlush(toDo);

        int databaseSizeBeforeUpdate = toDoRepository.findAll().size();

        // Update the toDo
        ToDo updatedToDo = toDoRepository.findById(toDo.getId()).get();
        // Disconnect from session so that the updates on updatedToDo are not directly saved in db
        em.detach(updatedToDo);
        updatedToDo
            .description(UPDATED_DESCRIPTION)
            .byDate(UPDATED_BY_DATE)
            .done(UPDATED_DONE);

        restToDoMockMvc.perform(put("/api/to-dos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedToDo)))
            .andExpect(status().isOk());

        // Validate the ToDo in the database
        List<ToDo> toDoList = toDoRepository.findAll();
        assertThat(toDoList).hasSize(databaseSizeBeforeUpdate);
        ToDo testToDo = toDoList.get(toDoList.size() - 1);
        assertThat(testToDo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToDo.getByDate()).isEqualTo(UPDATED_BY_DATE);
        assertThat(testToDo.isDone()).isEqualTo(UPDATED_DONE);
    }

    @Test
    @Transactional
    public void updateNonExistingToDo() throws Exception {
        int databaseSizeBeforeUpdate = toDoRepository.findAll().size();

        // Create the ToDo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoMockMvc.perform(put("/api/to-dos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toDo)))
            .andExpect(status().isBadRequest());

        // Validate the ToDo in the database
        List<ToDo> toDoList = toDoRepository.findAll();
        assertThat(toDoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteToDo() throws Exception {
        // Initialize the database
        toDoRepository.saveAndFlush(toDo);

        int databaseSizeBeforeDelete = toDoRepository.findAll().size();

        // Delete the toDo
        restToDoMockMvc.perform(delete("/api/to-dos/{id}", toDo.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToDo> toDoList = toDoRepository.findAll();
        assertThat(toDoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
