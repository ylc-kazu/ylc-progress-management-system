package com.ylc.progress_management_system.dto;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFullForm {

    // 生徒基本情報
    private Student student = new Student();

    // プロフィール
    private StudentProfile profile = new StudentProfile();

    // 連絡先（複数）
    @Builder.Default
    private List<StudentContact> contacts = new ArrayList<>();
}