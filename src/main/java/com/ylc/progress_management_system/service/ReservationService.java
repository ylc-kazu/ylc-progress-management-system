package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Reservation;
import com.ylc.progress_management_system.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // 💡 修正：第2引数に「String loginClassroomCode」を追加しました
    @Transactional
    public void importCsv(MultipartFile file, String loginClassroomCode) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String targetDate = null;

        // 1. ファイルの中身を解析してリスト化
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "MS932"))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",", -1);
                if (data.length < 13) continue;

                Reservation r = new Reservation();
                r.setCampus(cleanValue(data[0]));
                r.setClassroom(cleanValue(data[1]));
                r.setClassCode(cleanValue(data[2]));
                r.setClassName(cleanValue(data[3]));

                String lessonDate = cleanValue(data[4]);
                r.setLessonDate(lessonDate);

                // 削除対象とする開講日を1つ保持
                if (targetDate == null && !lessonDate.isEmpty()) {
                    targetDate = lessonDate;
                }

                r.setStartTime(cleanValue(data[5]));
                r.setEndTime(cleanValue(data[6]));
                r.setCourseCode(cleanValue(data[7]));
                r.setCourseName(cleanValue(data[8]));
                r.setLesson(cleanValue(data[9]));
                r.setTeacher(cleanValue(data[10]));
                r.setStudentCode(cleanValue(data[11]));
                r.setStudentName(cleanValue(data[12]));

                // 💡 修正：CSVの日本語ではなく、ログインユーザーの教室コード（'OHASHI'など）を確実にセット！
                r.setClassroomCode(loginClassroomCode);

                reservations.add(r);
            }
        }

        // 💡 2. 【安全ガード付】今回取り込む「開講日」かつ「自分の教室」の古いデータのみを削除
        if (targetDate != null) {
            List<Reservation> oldReservations = reservationRepository.findAll();
            String finalTargetDate = targetDate;
            oldReservations.stream()
                    // 💡 条件追加：開講日が同じ、かつ教室コードが一致するものだけをピンポイントで消す
                    .filter(res -> finalTargetDate.equals(res.getLessonDate()) && loginClassroomCode.equals(res.getClassroomCode()))
                    .forEach(res -> reservationRepository.delete(res));
            reservationRepository.flush(); // 即座に削除を確定
        }

        // 3. まっさらになったところに、最新のCSVデータを一発で挿入
        if (!reservations.isEmpty()) {
            reservationRepository.saveAll(reservations);
        }
    }

    private String cleanValue(String val) {
        if (val == null) return "";
        String trimmed = val.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}