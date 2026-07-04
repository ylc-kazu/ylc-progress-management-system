package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; // Listをインポート
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 💡 データベースの DATE / TIME 型への変更に合わせて、引数も LocalDate / LocalTime にします
    Optional<Reservation> findByClassroomCodeAndStudentCodeAndLessonDateAndStartTime(
            String classroomCode, String studentCode, LocalDate lessonDate, LocalTime startTime);

    // 特定の教室のすべての予約を取得するメソッドを追加
    List<Reservation> findByClassroomCode(String classroomCode);
}
