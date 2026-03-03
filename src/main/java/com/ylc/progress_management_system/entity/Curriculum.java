package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data; // これが必要

@Entity
@Table(name = "curriculums")
@Data // これを付けると、getItemName() 等が自動で作られます

public class Curriculum {
    @Id
    @Pattern(regexp = "^[A-Z0-9]+-[A-Z0-9-]+$", message = "管理IDには必ずハイフンを含めてください")
    private String id;
    private String textbookName;
    private String chapter;
    private String itemName;
    @Min(value = 1, message = "ページ数は1以上で入力してください")
    private Integer pageNumber;

    @Column(columnDefinition = "TEXT")
    private String aiDescription;

}