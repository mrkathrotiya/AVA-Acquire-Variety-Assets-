package com.server.AVA.Services.PropertyTypeServices;

import com.server.AVA.Models.Insights;

public interface InsightsService {
    Insights getInsights(Long insightsId);
    Insights saveInsights(Insights insights);
    void deleteInsights(Insights insights);
    void addInterestedCount(Long insightsId);
    void subtractInterestedCount(Long insightsId);
    void addCallCount(Long insightsId);
    void addViewCount(Long insightsId);

}
