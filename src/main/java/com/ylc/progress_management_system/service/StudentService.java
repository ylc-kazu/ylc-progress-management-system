package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;
import com.ylc.progress_management_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // 💡 修正：第2引数に「String loginClassroomCode」をしっかり定義しました
    @Transactional
    public void importCsv(MultipartFile file, String loginClassroomCode) throws Exception {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "MS932"))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",", -1);
                if (data.length < 4) continue;

                String studentCode = cleanValue(getSafeValue(data, 2));
                if (studentCode.isEmpty()) continue;

                // 💡 修正：CSVの49列目からではなく、引数で渡されたログインユーザーの教室コードを強制セットします
                String classroomCode = loginClassroomCode;

                Optional<Student> existingStudentOpt = studentRepository.findById(studentCode);
                Student s;
                StudentProfile p;

                if (existingStudentOpt.isPresent()) {
                    s = existingStudentOpt.get();
                    p = s.getProfile();
                    if (p == null) {
                        p = new StudentProfile();
                        p.setStudent(s);
                    }
                    s.getContacts().clear();
                } else {
                    s = new Student();
                    s.setStudentCode(studentCode);
                    p = new StudentProfile();
                    p.setStudent(s);
                    s.setContacts(new ArrayList<>());
                }

                // 教室コードを一元セット
                s.setClassroomCode(classroomCode);

                String studentName = cleanValue(getSafeValue(data, 3));
                s.setName(studentName);
                s.setFurigana(cleanValue(getSafeValue(data, 4)));
                s.setStatus(cleanValue(getSafeValue(data, 1)));
                s.setRegistrationSource("本部CSV一括更新");

                p.setManagementId(cleanValue(getSafeValue(data, 0)));
                p.setNickname(cleanValue(getSafeValue(data, 5)));
                p.setGenderText(cleanValue(getSafeValue(data, 6)));
                p.setBirthDateText(cleanValue(getSafeValue(data, 7)));
                p.setGradeText(cleanValue(getSafeValue(data, 8)));
                p.setPostalCode(cleanValue(getSafeValue(data, 9)));
                p.setPrefecture(cleanValue(getSafeValue(data, 10)));
                p.setAddress1(cleanValue(getSafeValue(data, 11)));
                p.setAddress2(cleanValue(getSafeValue(data, 12)));
                p.setSchoolName(cleanValue(getSafeValue(data, 13)));
                p.setPhoneHome(cleanValue(getSafeValue(data, 14)));
                p.setPhoneMobile(cleanValue(getSafeValue(data, 15)));

                String email1 = cleanValue(getSafeValue(data, 22));
                String email2 = cleanValue(getSafeValue(data, 27));
                p.setEmail1(email1);
                p.setEmail2(email2);

                String parentName = cleanValue(getSafeValue(data, 32));
                p.setParentName(parentName);
                p.setParentNameKana(cleanValue(getSafeValue(data, 33)));

                String relation = cleanValue(getSafeValue(data, 34));
                p.setRelationText(relation);

                String emergencyPhone = cleanValue(getSafeValue(data, 40));
                p.setEmergencyPhone(emergencyPhone);

                String parentEmail1 = cleanValue(getSafeValue(data, 41));
                String parentEmail2 = cleanValue(getSafeValue(data, 45));
                p.setParentEmail1(parentEmail1);
                p.setParentEmail2(parentEmail2);

                p.setTargetCampus(classroomCode);
                p.setStaffCode(cleanValue(getSafeValue(data, 50)));
                p.setStaffName(cleanValue(getSafeValue(data, 51)));
                p.setRemarksGeneral(cleanValue(getSafeValue(data, 58)));
                p.setTypingTest(cleanValue(getSafeValue(data, 60)));
                p.setProgrammingTest(cleanValue(getSafeValue(data, 61)));
                p.setRemarksText(cleanValue(getSafeValue(data, 62)));
                s.setProfile(p);

                // 連絡先にも同じ教室コードをセット
                if (!email1.isEmpty()) {
                    s.getContacts().add(createContact(s, classroomCode, "本人", studentName, cleanValue(getSafeValue(data, 15)), email1, 1));
                }
                if (!email2.isEmpty()) {
                    s.getContacts().add(createContact(s, classroomCode, "本人(サブ)", studentName, cleanValue(getSafeValue(data, 14)), email2, 2));
                }
                if (!parentEmail1.isEmpty()) {
                    s.getContacts().add(createContact(s, classroomCode, relation.isEmpty() ? "保護者" : relation, parentName, emergencyPhone, parentEmail1, 3));
                }
                if (!parentEmail2.isEmpty()) {
                    s.getContacts().add(createContact(s, classroomCode, (relation.isEmpty() ? "保護者" : relation) + "(サブ)", parentName, emergencyPhone, parentEmail2, 4));
                }

                students.add(s);
            }
        }

        if (!students.isEmpty()) {
            studentRepository.saveAll(students);
        }
    }

    private StudentContact createContact(Student student, String classroomCode, String relation, String name, String phone, String email, int priority) {
        StudentContact c = new StudentContact();
        c.setStudent(student);
        c.setClassroomCode(classroomCode);
        c.setRelation(relation);
        c.setName(name);
        c.setPhone(phone);
        c.setEmail(email);
        c.setPriority(priority);
        return c;
    }

    private String cleanValue(String val) {
        if (val == null) return "";
        String trimmed = val.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private String getSafeValue(String[] data, int index) {
        if (data == null || index < 0 || index >= data.length) return "";
        return data[index];
    }

    public List<Student> getStudentsByClassroom(String classroomCode, String role) {
        if ("ADMIN".equals(role)) {
            return studentRepository.findAll();
        }
        return studentRepository.findAll();
    }
}