package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private String classroomCode;     // 2. 教室コード

    @Column(name = "student_code", nullable = false)
    private String studentCode;       // 3. 生徒コード

    // 💡 修正：Studentエンティティとのリレーションを追加
    // insertable=false, updatable=false にすることで、studentCodeカラムで紐付けのみを行います
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_code", referencedColumnName = "student_code", insertable = false, updatable = false)
    private Student student;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;     // 4. レッスン日

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;      // 5. 開始時間

    // --- 以下、業務データ項目 ---
    private String campus;
    private String classroom;
    private String classCode;
    private String className;

    @Column(name = "end_time")
    private LocalTime endTime;

    private String courseCode;
    private String courseName;
    private String lesson;
    private String teacher;
    private String studentName;

    // --- 💡 拡張フィールド（手入力・割当用） ---

    @Column(name = "mentor_code")
    private String mentorCode;               // 割り当てられたメンターコード

    // 💡 Mentorエンティティとのリレーションを追加
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_code", referencedColumnName = "mentor_code", insertable = false, updatable = false)
    private Mentor mentor;

    @Column(name = "attendance_status")
    private String attendanceStatus;         // 出欠状況 (例: 出席, 欠席, 遅刻, 早退)

    @Column(name = "is_late")
    private Boolean isLate = false;          // 遅刻フラグ

    @Column(name = "is_early_leave")
    private Boolean isEarlyLeave = false;    // 早退フラグ

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;                  // 備考

    @Column(name = "progress_memo", columnDefinition = "TEXT")
    private String progressMemo;             // 今日の進捗メモ

    @Column(name = "homework", columnDefinition = "TEXT")
    private String homework;                 // 宿題

    @Column(name = "next_lesson_memo", columnDefinition = "TEXT")
    private String nextLessonMemo;           // 次回やること

    @Column(name = "is_completed")
    private Boolean isCompleted = false;     // 授業終了フラグ
}
