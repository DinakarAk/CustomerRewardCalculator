package com.infosys.rewards.controller;

import com.infosys.rewards.dto.CustomerRewardSummary;
import com.infosys.rewards.error.ValidationException;
import com.infosys.rewards.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardsService rewardsService;

    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, Integer>> getRewards(@PathVariable Long customerId) {
        Map<String, Integer> rewards = rewardsService.calculateMonthlyRewards(customerId);
        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<Long, CustomerRewardSummary>> getRewardSummary(@RequestParam("year") int year, @RequestParam("month") int month) {
        YearMonth startMonth = YearMonth.of(year, month);
        Map<Long, CustomerRewardSummary> rewards = rewardsService.calculateRewardsForAllCustomers(startMonth);

        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/calculate/{amount}")
    public ResponseEntity<String> getReward(@PathVariable double amount) {
        if (amount < 0.0) {
            throw new ValidationException("Provide correct input");
        }
        int points = rewardsService.calculatePoints(amount);
        return ResponseEntity.ok("The calculated reward for amount $" + amount + " is: "  + points);
    }

}
