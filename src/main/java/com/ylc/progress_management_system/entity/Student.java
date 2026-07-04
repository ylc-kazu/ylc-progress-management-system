package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @Column(name = "student_code", nullable = false)
    private String studentCode;       // 1. 生徒コード（主キー：PK）

    @Column(name = "classroom_code", nullable = false)
    private String classroomCode;     // 2. 所属教室コード（★FK連携項目）

    @Column(name = "name")
    private String name;              // 3. 氏名

    @Column(name = "furigana")
    private String furigana;          // 4. フリガナ

    @Column(name = "status")
    private String status;            // 5. ステータス（在籍、体験など）

    @Column(name = "registration_source")
    private String registrationSource;// 6. 登録元（CSV、手動など）

    // --- 他のテーブルとのリレーション設定 ---

    // 💡 生徒プロフィール（1対1）
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StudentProfile profile;

    // 💡 連絡先マスタ（1対多）
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentContact> contacts;

    // 💡 授業記録（1対多）
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LessonRecord> lessonRecords;
}
