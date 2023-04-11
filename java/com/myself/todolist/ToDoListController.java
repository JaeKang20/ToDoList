package com.myself.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/todolist")
public class ToDoListController {
    @Autowired
    private ToDoListRepository toDoListRepository;
    
    //페이징 처리하여 모든 To Do List 목록 가져오기
    @GetMapping("")
    public ResponseEntity<List<ToDoListEntity>> getAllToDoList(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {
    	// 페이지 정보와 정렬 정보 설정
    	Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
    	// 페이지 결과 객체 반환
        Page<ToDoListEntity> pagedResult = toDoListRepository.findAll(pageable);
        // 페이지 내용을 List로 반환하여 응답
        List<ToDoListEntity> todos = pagedResult.getContent();
        return ResponseEntity.ok(todos);
}
        //http://localhost:8080/todolist?pageNo=1&pageSize=5
    

 // 특정 To Do List 조회
    @GetMapping("/{id}")
    public ResponseEntity<ToDoListEntity> getToDoListById(@PathVariable("id") Long id) {
        ToDoListEntity toDoList = toDoListRepository.findById(id).orElseThrow(() -> new ToDoListNotFoundException("ToDoList's ID not found"));
        return ResponseEntity.ok(toDoList);
    }
    
 // 새로운 To Do List 생성
    @PostMapping("")
    public ResponseEntity<ToDoListEntity> createToDoList(@RequestBody ToDoListEntity toDoList) {
        ToDoListEntity newToDoList = toDoListRepository.save(toDoList);
        return ResponseEntity.ok(newToDoList);
    }
 // To Do List 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<ToDoListEntity> updateToDoList(@PathVariable("id") Long id, @RequestBody ToDoListEntity toDoList) {
        Optional<ToDoListEntity> optionalToDoList = toDoListRepository.findById(id);
        ToDoListEntity existingToDoList = optionalToDoList.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "ToDoList not found"));
        existingToDoList.setDate(toDoList.getDate());
        existingToDoList.setMemo(toDoList.getMemo());
        ToDoListEntity updatedToDoList = toDoListRepository.save(existingToDoList);
        return ResponseEntity.ok(updatedToDoList);
    }
 // To Do List 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDoList(@PathVariable("id") Long id) {
        toDoListRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
 // 유효성 검사 예외 처리를 위한 어노테이션
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationErrors(ConstraintViolationException ex) {
        // 에러 메시지와 타임스탬프를 저장할 맵 생성
        Map<String, Object> errors = new HashMap<>();
        // 현재 시간을 타임스탬프로 저장
        errors.put("timestamp", LocalDateTime.now());
        // 검증 위반 메시지를 리스트로 저장
        errors.put("errors", ex.getConstraintViolations().stream().map(violation -> violation.getMessage()).collect(Collectors.toList()));
        // HTTP 응답 상태코드와 에러 맵을 반환
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
