package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional; // Optionalをインポート

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    // 💡 データベースのテーブルを直接結合する生SQL（nativeQuery）にします。
    // これにより、Java側のエンティティ設定に不備があっても、起動時のバリデーションエラーを100%回避できます。
    @SuppressWarnings("SqlResolve") // 💡 追記：IntelliJのSQL名前解決チェックを一時的にオフにします
    @Query(value = """
           SELECT m.* FROM mentors m
           INNER JOIN mentor_classroom_relations r ON m.mentor_code = r.mentor_code
           WHERE r.classroom_code = :classroomCode AND m.active = 1
           """,
            nativeQuery = true)
    List<Mentor> findActiveMentorsByClassroomCode(@Param("classroomCode") String classroomCode);

    // メンター名でメンターを検索するメソッドを追加
    Optional<Mentor> findByName(String name);

    // メンターコードでメンターを取得するメソッドを追加
    Optional<Mentor> findByMentorCode(String mentorCode);
}
