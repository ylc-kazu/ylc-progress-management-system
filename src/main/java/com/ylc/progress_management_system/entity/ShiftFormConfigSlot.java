package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "shift_form_config_slots")
@Data
public class ShiftFormConfigSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_form_config_date_id", nullable = false)
    private ShiftFormConfigDate shiftFormConfigDate;

    @Column(name = "slot_name", nullable = false)
    private String slotName; // コマの表示名 (例: "1限(10:00〜11:00)")

    @Column(name = "start_time")
    private LocalTime startTime; // コマの開始時刻 (例: 10:00)

    @Column(name = "end_time")
    private LocalTime endTime; // コマの終了時刻 (例: 11:00)

    @Column(name = "is_available_for_request", nullable = false)
    private Boolean isAvailableForRequest = true; // メンターが希望を提出できるか (ON/OFF)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
