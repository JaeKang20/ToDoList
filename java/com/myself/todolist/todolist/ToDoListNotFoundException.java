package com.myself.todolist;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ToDoListNotFoundException extends RuntimeException {
public ToDoListNotFoundException(String massage) {
	super(massage);
}
}
