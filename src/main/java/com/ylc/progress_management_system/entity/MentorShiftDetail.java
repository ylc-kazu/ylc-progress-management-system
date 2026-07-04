package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "mentor_shift_details")
@Data
public class MentorShiftDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_request_id", nullable = false)
    private MentorShiftRequest shiftRequest;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Column(name = "shift_slot_name", nullable = false)
    private String shiftSlotName; // 例: "1限(10:00〜11:00)", "終日NG"

    @Column(name = "slot_start_time")
    private LocalTime slotStartTime; // 終日NGの場合はnull

    @Column(name = "slot_end_time")
    private LocalTime slotEndTime; // 終日NGの場合はnull

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable; // true: 出勤可能, false: 終日NG
}
