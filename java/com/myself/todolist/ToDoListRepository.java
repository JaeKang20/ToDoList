package com.myself.todolist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoListRepository extends JpaRepository<ToDoListEntity, Long> {
}

