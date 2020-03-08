package com.nttdata.ta.todo.repository;

import com.nttdata.ta.todo.domain.ToDo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ToDo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

}
