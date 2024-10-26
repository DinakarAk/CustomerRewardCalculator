package com.infosys.rewards.controller;

import com.infosys.rewards.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RewardController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService;

    @Test
    public void testGetCustomerRewards() throws Exception {
        Map<String, Integer> rewards = new HashMap<>();
        rewards.put("OCTOBER", 90);
        rewards.put("SEPTEMBER", 45);

        when(rewardsService.calculateMonthlyRewards(1L)).thenReturn(rewards);

        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.OCTOBER").value(90))
                .andExpect(jsonPath("$.SEPTEMBER").value(45));
    }
}
