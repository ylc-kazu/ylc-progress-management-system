package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.MentorShiftDetail;
import com.ylc.progress_management_system.entity.MentorShiftRequest;
import com.ylc.progress_management_system.repository.MentorShiftRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentorShiftService {

    private final MentorShiftRequestRepository shiftRequestRepository;

    @Transactional
    public MentorShiftRequest saveShiftRequest(MentorShiftRequest shiftRequest) {
        shiftRequest.setSubmissionDate(LocalDateTime.now()); // 提出日時をセット
        if (shiftRequest.getShiftDetails() != null) {
            shiftRequest.getShiftDetails().forEach(detail -> detail.setShiftRequest(shiftRequest));
        }
        return shiftRequestRepository.save(shiftRequest);
    }

    // シフトフォーム設定IDでシフト希望を取得するメソッドを追加
    public Optional<MentorShiftRequest> getShiftRequestByMentorCodeAndConfigId(String mentorCode, Long shiftFormConfigId) {
        return shiftRequestRepository.findByMentorCodeAndShiftFormConfigId(mentorCode, shiftFormConfigId);
    }

    // 既存のメソッドは残しておくか、必要に応じて削除
    public Optional<MentorShiftRequest> getShiftRequest(String mentorCode, LocalDate requestMonth) {
        return shiftRequestRepository.findByMentorCodeAndRequestMonth(mentorCode, requestMonth);
    }

    public List<MentorShiftRequest> getAllShiftRequests() {
        return shiftRequestRepository.findAll();
    }
}
