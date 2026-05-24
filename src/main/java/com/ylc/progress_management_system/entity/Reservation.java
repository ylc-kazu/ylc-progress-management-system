package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
        name = "reservations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_student_reservation_slot",
                        columnNames = {"classroom_code", "student_code", "lesson_date", "start_time"}
                )
        }
)
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // 1. 自動採番の主キー（PK）

    @Column(name = "classroom_code", nullable = false)
    private String classroomCode;     // 2. 教室コード（閲覧制限・ユニーク制約用）

    @Column(name = "student_code", nullable = false)
    private String studentCode;       // 3. 生徒コード（ユニーク制約用）

    @Column(name = "lesson_date", nullable = false)
    private String lessonDate;        // 4. レッスン日（ユニーク制約用）

    @Column(name = "start_time", nullable = false)
    private String startTime;         // 5. 開始時間（ユニーク制約用）

    // --- 以下、業務データ項目（キー項目の下に配置） ---
    private String campus;
    private String classroom;
    private String classCode;
    private String className;
    private String endTime;
    private String courseCode;
    private String courseName;
    private String lesson;
    private String teacher;
    private String studentName;
}