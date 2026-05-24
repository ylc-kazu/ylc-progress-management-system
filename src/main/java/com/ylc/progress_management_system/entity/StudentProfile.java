package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "student_profiles")
@Data
@ToString(exclude = "student") // 無限ループ（StackOverflow）防止
public class StudentProfile {

    @Id // 💡 修正：student_codeをこのテーブルの主キー（PK）にします
    @Column(name = "student_code")
    private String studentCode;

    @OneToOne
    @MapsId // 💡 修正：Studentsテーブルの主キー（student_code）と完全に同期させます
    @JoinColumn(name = "student_code")
    private Student student;

    @Column(name = "management_id")
    private String managementId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "gender_text")
    private String genderText;

    @Column(name = "birth_date_text")
    private String birthDateText;

    @Column(name = "grade_text")
    private String gradeText;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "prefecture")
    private String prefecture;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "phone_home")
    private String phoneHome;

    @Column(name = "phone_mobile")
    private String phoneMobile;

    @Column(name = "email1")
    private String email1;

    @Column(name = "email2")
    private String email2;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "parent_name_kana")
    private String parentNameKana;

    @Column(name = "relation_text")
    private String relationText;

    @Column(name = "emergency_phone")
    private String emergencyPhone;

    @Column(name = "parent_email1")
    private String parentEmail1;

    @Column(name = "parent_email2")
    private String parentEmail2;

    @Column(name = "target_campus")
    private String targetCampus;

    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "remarks_general", columnDefinition = "TEXT")
    private String remarksGeneral;

    @Column(name = "typing_test")
    private String typingTest;

    @Column(name = "programming_test")
    private String programmingTest;

    @Column(name = "remarks_text", columnDefinition = "TEXT")
    private String remarksText;
}