package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "classrooms")
@Data
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classroomId;

    @Column(unique = true, nullable = false)
    private String classroomCode;

    @Column(nullable = false)
    private String classroomName;
}