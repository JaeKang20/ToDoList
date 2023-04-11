package com.myself.todolist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "todolist") // 테이블 이름
public class ToDoListEntity {

    @Id
    @GeneratedValue
    private long id; // id 값 부여

    @NotNull // 빈값 X
    private LocalDate date;

    @NotNull // memo size 지정
    @Size(min = 1, max = 50)
    private String memo;

    public ToDoListEntity() {
        // 기본 생성자
    }

    public ToDoListEntity(LocalDate date, String memo) {
        this.date = date;
        this.memo = memo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
