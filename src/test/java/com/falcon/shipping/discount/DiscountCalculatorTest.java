package com.falcon.shipping.discount;

import com.falcon.shipping.exception.ShipmentProcessingException;
import com.falcon.shipping.shipment.ShipmentTransaction;
import com.falcon.shipping.rule.DiscountRuleBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountCalculatorTest {

  private DiscountCalculator discountCalculator;
  private Map<String, Map<String, Double>> shipmentOptionMap;
  private List<DiscountRuleBase> discountRules;

  @BeforeEach
  void setUp() {
    shipmentOptionMap = new HashMap<>();
    discountRules = new ArrayList<>();
    discountCalculator = new DiscountCalculator(shipmentOptionMap, discountRules);
  }

  @Test
  void testNoDiscountApplied() {
    // Edge Case 1: No discount rules applicable
    shipmentOptionMap.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderA");

    List<ShipmentTransaction> transactions = List.of(transaction);
    List<String> output = discountCalculator.processTransactions(transactions);

    assertEquals("2024-10-08 S ProviderA 10.00 -", output.get(0));
  }

  @Test
  void testInvalidProvider() {
    // Edge Case 2: Transaction with a provider not in the shipment options
    shipmentOptionMap.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderB");

    List<ShipmentTransaction> transactions = List.of(transaction);
    List<String> output = discountCalculator.processTransactions(transactions);

    assertNotEquals("2024-10-08 S ProviderB 10.00 -", output.get(0));  // Should still set the default price
  }

  @Test
  void testMultipleDiscountRules() {
    // Edge Case 3: Transaction applies multiple discount rules
    shipmentOptionMap.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderA");

    DiscountRuleBase rule1 = Mockito.mock(DiscountRuleBase.class);
    when(rule1.applyRule(transaction)).thenReturn(true);
    discountRules.add(rule1);

    DiscountRuleBase rule2 = Mockito.mock(DiscountRuleBase.class);
    when(rule2.applyRule(transaction)).thenReturn(true);
    discountRules.add(rule2);

    List<ShipmentTransaction> transactions = List.of(transaction);
    discountCalculator.processTransactions(transactions);

    verify(rule1).applyRule(transaction);
    verify(rule2).applyRule(transaction);
  }

  @Test
  void testExceptionHandlingInTransactionProcessing() {
    // Edge Case 4: Exception thrown in rule application
    shipmentOptionMap.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderA");

    DiscountRuleBase rule = Mockito.mock(DiscountRuleBase.class);
    when(rule.applyRule(transaction)).thenThrow(new RuntimeException("Error applying rule"));
    discountRules.add(rule);

    assertThrows(ShipmentProcessingException.class, () -> {
      discountCalculator.processTransactions(List.of(transaction));
    });
  }

  @Test
  void testEmptyTransactionList() {
    // Edge Case 5: Processing an empty transaction list
    List<ShipmentTransaction> transactions = new ArrayList<>();
    List<String> output = discountCalculator.processTransactions(transactions);

    assertTrue(output.isEmpty(), "Output should be empty for an empty transaction list");
  }
}
