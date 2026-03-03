package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Curriculum;
import com.ylc.progress_management_system.repository.CurriculumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurriculumService {
    @Autowired
    private CurriculumRepository curriculumRepository;

    public String getJoinedDescriptions(String startId, String endId) {
        List<Curriculum> items = curriculumRepository.findByIdBetweenOrderByIdAsc(startId, endId);

        // 取得した項目の「説明文」を一つの文字列に連結する
        return items.stream()
                .map(item -> item.getItemName() + ":" + item.getAiDescription())
                .collect(Collectors.joining("\n"));
    }
}