package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Mentor;
import com.ylc.progress_management_system.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Optionalをインポート

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    /**
     * 指定された校舎コードに紐づく（本所属 or ヘルプ）稼働中のメンター一覧を取得
     */
    public List<Mentor> getMentorsByClassroom(String classroomCode) {
        // 💡 データベースの中間テーブル(mentor_classroom_relations)を経由して、
        // 該当の校舎コードを持ち、かつ active が true のメンターをリポジトリから取得します。
        return mentorRepository.findActiveMentorsByClassroomCode(classroomCode);
    }

    // メンターコードでメンターを取得するメソッドを追加
    public Optional<Mentor> findByMentorCode(String mentorCode) {
        return mentorRepository.findByMentorCode(mentorCode);
    }
}
