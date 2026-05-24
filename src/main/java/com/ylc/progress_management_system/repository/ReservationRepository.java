package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 💡 3つのキーの組み合わせでデータベースから1件を特定するメソッド
    Optional<Reservation> findByLessonDateAndStartTimeAndStudentCode(String lessonDate, String startTime, String studentCode);
}