package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code") // DBのカラム名と合わせる
    private String studentCode;
    private String name;
    private String furigana;
    private String status;

    @Column(name = "registration_source") // DBのカラム名と合わせる
    private String registrationSource;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentContact> contacts;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 手動Setter/Getter（Lombokが効かない場合用）
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }
    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setRegistrationSource(String registrationSource) { this.registrationSource = registrationSource; }
    public String getRegistrationSource() { return registrationSource; }
    public void setContacts(List<StudentContact> contacts) { this.contacts = contacts; }
    public List<StudentContact> getContacts() { return contacts; }
}