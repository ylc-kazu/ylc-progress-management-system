package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "mentors")
@Data
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    // Mentor.java
    @DateTimeFormat(pattern = "yyyy-MM-dd") // これを追加
    private LocalDate birthday;

    // 給与・交通費
    private String nearestStation;
    private Integer transportationFee;

    // もし将来同姓同名が増える対策をするなら、ここに追加しておくと完璧です！
    @Column(name = "mentor_code", unique = true)
    private String mentorCode;

    // ★DBに合わせて一旦これを含めます
    @Column(name = "hourly_rate")
    private Integer hourlyRate;

    // 状態・研修
    private String completedCurriculumId;
    private boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String note;

    // ランク
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rank_id")
    private MentorRank rank;

    // 🎯 追加：メンターが所属（ヘルプ含む）している教室コードのリスト
    // @Transient をつけておくことで、インポート時などに一時的にメモリ上で教室一覧を保持・操作しやすくなります
    @Transient
    private java.util.List<String> assignedClassroomCodes = new java.util.ArrayList<>();
}