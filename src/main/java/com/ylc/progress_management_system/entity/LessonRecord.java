package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "lesson_records")
@Data
public class LessonRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code", nullable = false)
    private String studentCode;

    @ManyToOne
    @JoinColumn(name = "student_code", referencedColumnName = "student_code", insertable = false, updatable = false)
    private Student student;

    @Column(name = "mentor_code", nullable = false)
    private String mentorCode;

    @ManyToOne
    @JoinColumn(name = "mentor_code", referencedColumnName = "mentor_code", insertable = false, updatable = false)
    private Mentor mentor;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "typing_level")
    private String typingLevel;

    @Column(name = "typing_score")
    private Integer typingScore;

    @Column(name = "curriculum", nullable = false)
    private String curriculum; // MESH, Scratch, Micro:bit, Python, Unity/C#, HTML.SCSS, JavaScript

    @Column(name = "curriculum_detail", columnDefinition = "TEXT")
    private String curriculumDetail; // 例: Scratchの「ネコを動かそう」プロジェクト

    @Column(name = "mentor_comment", columnDefinition = "TEXT")
    private String mentorComment; // メンターが入力する感想

    @Column(name = "ai_comment1", columnDefinition = "TEXT")
    private String aiComment1; // AI生成感想1

    @Column(name = "ai_comment2", columnDefinition = "TEXT")
    private String aiComment2; // AI生成感想2

    @Column(name = "ai_comment3", columnDefinition = "TEXT")
    private String aiComment3; // AI生成感想3

    @Column(name = "final_comment_for_parent", columnDefinition = "TEXT")
    private String finalCommentForParent; // 保護者へ共有する最終感想

    @Column(name = "shared_at")
    private LocalDate sharedAt; // 保護者共有日時
}
