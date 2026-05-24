package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_contacts")
@Data
public class StudentContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // 1. このテーブルの主キー

    @ManyToOne
    @JoinColumn(name = "student_code", referencedColumnName = "student_code")
    private Student student;          // 2. 親と繋がる外部連携キー

    @Column(name = "classroom_code", nullable = false)
    private String classroomCode;     // 3. 教室コード（★新規追加）

    private Integer priority = 0;     // 4. 業務データ...
    private String relation;
    private String name;
    private String phone;
    private String email;
}