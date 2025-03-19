package com.server.AVA.Implementations;

import com.server.AVA.Models.Insights;
import com.server.AVA.Repos.InsightsRepository;
import com.server.AVA.Services.PropertyTypeServices.InsightsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class InsightsServiceImpl implements InsightsService {
    private final InsightsRepository insightsRepository;
    
    @Override
    @Cacheable(value = "INSIGHT", key = "#insightsId")
    public Insights getInsights(Long insightsId) {
        return insightsRepository.findById(insightsId).orElseThrow(
                () -> new EntityNotFoundException("Insights not found with Id: "+insightsId)
        );
    }

    @Transactional
    @Override
    public Insights saveInsights(Insights insights) {
        return insightsRepository.save(Objects.requireNonNull(insights));
    }

    @Transactional
    @Override
    @CacheEvict(value = "INSIGHT", key = "#insightsId")
    public void deleteInsights(Insights insights) {
        insightsRepository.delete(Objects.requireNonNull(insights));
    }

    @Transactional
    @Override
    public void addInterestedCount(Long insightsId) {
        Insights insights = getInsights(insightsId);
        Optional.ofNullable(insights)
                .ifPresent(insight -> insight.setInterested(insight.getInterested() + 1));
        saveInsights(insights);
    }

    @Transactional
    @Override
    public void subtractInterestedCount(Long insightsId) {
        Insights insights = getInsights(insightsId);
        Optional.ofNullable(insights)
                .ifPresent(insight -> insight.setInterested(Math.max(0, insight.getInterested() - 1)));
        saveInsights(insights);
    }

    @Override
    public void addCallCount(Long insightsId) {
        Insights insights = getInsights(insightsId);
        Optional.ofNullable(insights)
                .ifPresent(insights1 -> insights1.setCallCount(insights1.getCallCount()+1));
        saveInsights(insights);
    }

    @Override
    public void addViewCount(Long insightsId) {
        Insights insights = getInsights(insightsId);
        Optional.ofNullable(insights)
                .ifPresent(insights1 -> insights1.setViews(insights1.getViews()+1));
        saveInsights(insights);
    }
}
