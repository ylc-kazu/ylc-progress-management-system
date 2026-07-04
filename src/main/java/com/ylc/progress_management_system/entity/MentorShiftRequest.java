package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mentor_shift_requests")
@Data
public class MentorShiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentor_code", nullable = false)
    private String mentorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_code", referencedColumnName = "mentor_code", insertable = false, updatable = false)
    private Mentor mentor;

    @Column(name = "request_month", nullable = false)
    private LocalDate requestMonth; // シフト希望の対象月 (例: 2026-06-01)

    @Column(name = "shift_form_config_id", nullable = false) // 追加
    private Long shiftFormConfigId; // どのシフトフォーム設定に対する希望か

    @ManyToOne(fetch = FetchType.LAZY) // 追加
    @JoinColumn(name = "shift_form_config_id", referencedColumnName = "id", insertable = false, updatable = false) // 追加
    private ShiftFormConfig shiftFormConfig; // 追加

    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submissionDate; // シフト希望の提出日時

    @OneToMany(mappedBy = "shiftRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MentorShiftDetail> shiftDetails;
}
