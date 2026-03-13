package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_profiles")
@Data
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String schoolName;      // 学校名
    private String gradeText;       // 学年
    private String birthDateText;   // 生年月日
    private String genderText;      // 性別（追加しました）
    private String googleDriveUrl;  // GoogleドライブURL（エラーの原因）
    private String notes;           // 注意事項・メモ（エラーの原因）
}