package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 生徒ID

    @Column(name = "name", nullable = false)
    private String name;  // 生徒名

    @Column(name = "status")
    private String status;  // 在籍状況（active / inactive など）

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 更新日時

    // ★ StudentProfile との 1対1（双方向）
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentProfile profile;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentContact> contacts;

    // ★ StudentContact との 1対多
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();

    }
}