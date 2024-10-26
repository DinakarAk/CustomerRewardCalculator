package com.infosys.rewards.service;


import java.util.concurrent.CompletableFuture;

public class ApiService {

    public CompletableFuture<String> fetchDataSync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Fetched data";
        });
    }
}
