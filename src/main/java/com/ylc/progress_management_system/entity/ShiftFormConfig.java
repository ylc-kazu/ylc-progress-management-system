package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat; // 追加

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shift_form_configs")
@Data
public class ShiftFormConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_name", nullable = false)
    private String formName; // フォーム名 (例: "Swimmy大橋校2026年06月シフト希望フォーム")

    @DateTimeFormat(pattern = "yyyy-MM") // 追加
    @Column(name = "target_month", nullable = false)
    private LocalDate targetMonth; // 対象月 (例: 2026-06-01)

    @Column(name = "publish_flag", nullable = false)
    private Boolean publishFlag = false; // 公開フラグ (true/false)

    @Column(name = "public_url")
    private String publicUrl; // 公開URL (自動生成または導出)

    @Column(name = "remarks", columnDefinition = "TEXT") // 備考欄を追加
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "shiftFormConfig", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ShiftFormConfigDate> configDates;
}
