package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // これが必要
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, String> {

    // 以前追加した：範囲検索用
    List<Curriculum> findByIdBetweenOrderByIdAsc(String startId, String endId);

    // ★今回追加する：教材名の重複を除いたリストを取得（プルダウン用）
    // @Queryアノテーションを使うことで、独自のSQL（JPQL）を実行できます
    @Query("SELECT DISTINCT c.textbookName FROM Curriculum c ORDER BY c.textbookName")
    List<String> findDistinctTextbookNames();
}