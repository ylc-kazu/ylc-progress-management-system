package com.ylc.progress_management_system.entity;

import com.ylc.progress_management_system.entity.Student;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_contacts")
@Data
public class StudentContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String relation;
    private String name;
    private String phone;
    private String email;
    private Integer priority = 0;
}