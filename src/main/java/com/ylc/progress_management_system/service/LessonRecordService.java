package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.LessonRecord;
import com.ylc.progress_management_system.repository.LessonRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; // Optionalをインポート

@Service
@RequiredArgsConstructor
public class LessonRecordService {

    private final LessonRecordRepository lessonRecordRepository;

    @Transactional
    public LessonRecord saveLessonRecord(LessonRecord lessonRecord) {
        return lessonRecordRepository.save(lessonRecord);
    }

    public Optional<LessonRecord> findById(Long id) { // findByIdメソッドを追加
        return lessonRecordRepository.findById(id);
    }

    public List<LessonRecord> getLessonRecordsByStudentCode(String studentCode) {
        return lessonRecordRepository.findByStudentCodeOrderByLessonDateDesc(studentCode);
    }

    // 将来的にAI連携のロジックを追加するメソッドのプレースホルダー
    public List<String> generateAiComments(String studentName, String curriculum, String mentorComment) {
        // ここにAIモデルを呼び出すロジックを実装
        // 例: OpenAI API, Google Gemini APIなど
        // 現時点ではダミーデータを返す
        return List.of(
                "AI生成コメント1: " + studentName + "さんは" + curriculum + "の学習に意欲的に取り組んでいました。",
                "AI生成コメント2: " + curriculum + "の概念をよく理解しており、素晴らしい進捗です。",
                "AI生成コメント3: メンターのコメントから、" + studentName + "さんの成長が感じられます。"
        );
    }
}
