package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentCode; // 追加：CSVとの照合キー
    private String name;
    private String furigana;
    private String status;
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentProfile studentProfile; // HTMLでは s.studentProfile で呼べるようになります

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentContact> contacts;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}