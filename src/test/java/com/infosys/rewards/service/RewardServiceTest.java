package com.infosys.rewards.service;

import com.infosys.rewards.dto.CustomerRewardSummary;
import com.infosys.rewards.model.Customer;
import com.infosys.rewards.model.Transaction;
import com.infosys.rewards.repository.CustomerRepository;
import com.infosys.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardsService rewardsService;

    public RewardServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /***
     * if 0
     */
    @Test
    public void testCalculatePoints() {
        assertEquals(90, rewardsService.calculatePoints(120));
        assertEquals(0, rewardsService.calculatePoints(45));
        assertEquals(50, rewardsService.calculatePoints(100));
        assertEquals(250, rewardsService.calculatePoints(200));
    }

    @Test
    public void testCalculateMonthlyRewards() {

        YearMonth startMonth = YearMonth.of(2024, 10);
        Transaction transactionOne = new Transaction(1L, 1L, 120.0, LocalDate.of(2024, 10, 25));
        Transaction transactionTwo = new Transaction(2L, 1L, 75.0, LocalDate.of(2024, 9, 25));

        when(transactionRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(transactionOne, transactionTwo));

        Map<String, Integer> rewards = rewardsService.calculateMonthlyRewards(1L);

        assertEquals(90, rewards.get("OCTOBER"));
        assertEquals(25, rewards.get("SEPTEMBER"));
    }

    @Test
    public void testCalculateRewardsForAllCustomers() {

        YearMonth startMonth = YearMonth.of(2024, 10);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Dinakar");

        Customer customer1 = new Customer();
        customer1.setId(2L);
        customer1.setName("Akku");

        List<Customer> customerList = Arrays.asList(customer, customer1);

        when(customerRepository.findAll()).thenReturn(customerList);


        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        transaction.setAmount(120.0);
        transaction.setTransactionDate(LocalDate.of(2024, 10, 25));


        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(1L);
        transaction1.setAmount(75.0);
        transaction1.setTransactionDate(LocalDate.of(2024, 9, 25));

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(1L);
        transaction2.setAmount(200.0);
        transaction2.setTransactionDate(LocalDate.of(2024, 8, 25));

        List<Transaction> customerTransactions = Arrays.asList(transaction, transaction1, transaction2);
        when(transactionRepository.findByCustomerId(1L)).thenReturn(customerTransactions);



        Transaction transaction3 = new Transaction();
        transaction3.setCustomerId(2L);
        transaction3.setAmount(95.0);
        transaction3.setTransactionDate(LocalDate.of(2024, 10, 13));


        Transaction transaction4 = new Transaction();
        transaction4.setCustomerId(2L);
        transaction4.setAmount(110.0);
        transaction4.setTransactionDate(LocalDate.of(2024, 9, 12));

        Transaction transaction5 = new Transaction();
        transaction5.setCustomerId(2L);
        transaction5.setAmount(60.0);
        transaction5.setTransactionDate(LocalDate.of(2024, 8, 17));

        List<Transaction> customerTransactions1 = Arrays.asList(transaction3, transaction4, transaction5);
        when(transactionRepository.findByCustomerId(2L)).thenReturn(customerTransactions1);

        Map<Long, CustomerRewardSummary> result = rewardsService.calculateRewardsForAllCustomers(startMonth);

        CustomerRewardSummary summary1 = result.get(1L);
        assertEquals("Dinakar", summary1.getName());
        assertEquals(90, summary1.getMonthlyRewards().get(Month.OCTOBER));
        assertEquals(250, summary1.getMonthlyRewards().get(Month.AUGUST));
        assertEquals(25, summary1.getMonthlyRewards().get(Month.SEPTEMBER));
        assertEquals(365, summary1.getTotalRewards());

        CustomerRewardSummary summary2 = result.get(2L);
        assertEquals("Akku", summary2.getName());
        assertEquals(45, summary2.getMonthlyRewards().get(Month.OCTOBER));
        assertEquals(10, summary2.getMonthlyRewards().get(Month.AUGUST));
        assertEquals(70, summary2.getMonthlyRewards().get(Month.SEPTEMBER));
        assertEquals(125, summary2.getTotalRewards());


    }
}
