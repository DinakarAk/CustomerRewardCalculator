package com.infosys.rewards.dto;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class CustomerRewardSummary {

    private String name;
    private Map<Month, Integer> monthlyRewards = new HashMap<>();
    private int totalRewards;

    public CustomerRewardSummary(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Month, Integer> getMonthlyRewards() {
        return monthlyRewards;
    }
    public int getTotalRewards() {
        return totalRewards;
    }

    public void addMonthlyRewards(Month month, int points) {
        monthlyRewards.put(month, monthlyRewards.getOrDefault(month, 0) + points);
    }

    public void calculateTotalRewards() {
        totalRewards = monthlyRewards.values().stream().mapToInt(Integer::intValue).sum();
    }
}
