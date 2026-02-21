package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.ylc.progress_management_system.entity.Student;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 生徒プロフィールID

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;  // 生徒ID（students.id）

    @Column(name = "birth_date")
    private LocalDate birthDate;  // 生年月日

    @Column(name = "school_name")
    private String schoolName;  // 学校名

    @Column(name = "grade")
    private Integer grade;  // 学年

    @Column(name = "google_drive_url")
    private String googleDriveUrl;  // Googleドライブ共有URL

    @Column(name = "notes")
    private String notes;  // 注意事項・メモ

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
