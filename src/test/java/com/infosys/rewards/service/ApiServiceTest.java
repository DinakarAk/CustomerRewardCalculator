package com.infosys.rewards.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiServiceTest {


    private final ApiService apiService = new ApiService();

    @Test
    public void testFetchAsync() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> future = apiService.fetchDataSync();

        String result = future.get(2, TimeUnit.SECONDS);
        assertEquals("Fetched data", result, "The asynchronous fetched data");

        assertTrue(future.isDone());
    }
}
