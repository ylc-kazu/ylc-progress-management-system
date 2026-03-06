package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mentor_ranks")
@Data
public class MentorRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rankName;
    private Integer trainingRate;
    private Integer singleStudentRate;
    private Integer doubleStudentRate;
    private Integer displayOrder;
}