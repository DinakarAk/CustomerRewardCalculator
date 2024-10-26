package com.infosys.rewards.service;

import com.infosys.rewards.dto.CustomerRewardSummary;
import com.infosys.rewards.error.CustomerNotFoundException;
import com.infosys.rewards.model.Customer;
import com.infosys.rewards.model.Transaction;
import com.infosys.rewards.repository.CustomerRepository;
import com.infosys.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /***
     * Calculating the points
     * $120 > 100 so +20 * 2
     * $120 > 50 and also > 100 then +50 * 1
     * @param amounts
     * @return
     */
    public int calculatePoints(double amounts) {
        int points = 0;
        if (amounts > 100) {
            points += (int) (2 * (amounts - 100));
            amounts = 100;
        }
        if (amounts > 50) {
            points += (int) (1 * (amounts - 50));
        }
        return points;
    }

    public Map<String, Integer> calculateMonthlyRewards(Long customerId) {

        List<Transaction> transactionList = transactionRepository.findByCustomerId(customerId);
        Map<String, Integer> monthlyPoints = new HashMap<>();

        for (Transaction transaction : transactionList) {
            String month = transaction.getTransactionDate().getMonth().toString();
            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + calculatePoints(transaction.getAmount()));
        }
        return monthlyPoints;
    }


    public Map<Long, CustomerRewardSummary> calculateRewardsForAllCustomers(YearMonth startMonth) {
        List<Customer> customers = customerRepository.findAll();
        Map<Long, CustomerRewardSummary> customerRewards = new HashMap<>();

        YearMonth oneMonthAgo = startMonth.minusMonths(1);
        YearMonth twoMonthAgo = startMonth.minusMonths(2);

        for (Customer customer : customers) {
            List<Transaction> transactionList = transactionRepository.findByCustomerId(customer.getId());

//            CustomerRewardSummary summary = new CustomerRewardSummary(customer.getName());
//            for (Transaction transaction : transactionList) {
//                LocalDate date = transaction.getTransactionDate();
//                Month month = date.getMonth();
//                int points = calculatePoints(transaction.getAmount());
//
//                summary.addMonthlyRewards(month, points);
//            }

            List<Transaction> periodTransactions = transactionList.stream()
                            .filter(transaction -> {
                                YearMonth transactionMonth = YearMonth.from(transaction.getTransactionDate());
                                return transactionMonth.equals(startMonth) ||
                                        transactionMonth.equals(oneMonthAgo) ||
                                        transactionMonth.equals(twoMonthAgo);
                            }).collect(Collectors.toList());

            CustomerRewardSummary summary = new CustomerRewardSummary(customer.getName());

            for (Transaction transaction : periodTransactions) {
                LocalDate date = transaction.getTransactionDate();
                Month month = date.getMonth();
                int points = calculatePoints(transaction.getAmount());

                summary.addMonthlyRewards(month, points);
            }

            summary.calculateTotalRewards();
            customerRewards.put(customer.getId(), summary);
        }
        return customerRewards;
    }
}
