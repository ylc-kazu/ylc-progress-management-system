package com.ylc.progress_management_system.dto;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFullForm {
    private Student student = new Student();
    private StudentProfile profile = new StudentProfile();
    private List<StudentContact> contacts = new ArrayList<>();
}