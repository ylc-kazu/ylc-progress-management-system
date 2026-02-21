package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 生徒連絡先ID

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;  // 生徒ID（students.id）

    @Column(name = "relation")
    private String relation;  // 続柄（父・母・祖父母・保護者など）

    @Column(name = "name")
    private String name;  // 保護者氏名

    @Column(name = "phone")
    private String phone;  // 電話番号

    @Column(name = "email")
    private String email;  // メールアドレス

    @Column(name = "notes")
    private String notes;  // メモ

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 更新日時

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