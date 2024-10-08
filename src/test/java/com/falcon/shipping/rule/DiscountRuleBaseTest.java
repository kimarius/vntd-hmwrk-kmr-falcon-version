package com.falcon.shipping.rule;

import com.falcon.shipping.discount.DiscountTracker;
import com.falcon.shipping.shipment.ShipmentTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountRuleBaseTest {

  private DiscountRuleBase discountRule;
  private Map<String, Map<String, Double>> shipmentOptions;
  private DiscountTracker discountTracker;

  @BeforeEach
  void setUp() {
    shipmentOptions = new HashMap<>();
    discountTracker = new DiscountTracker();

    // Creating a concrete implementation of DiscountRuleBase for testing
    discountRule = new DiscountRuleBase(shipmentOptions, discountTracker) {
      @Override
      public boolean applyRule(ShipmentTransaction transaction) {
        // Example logic for testing
        return false;
      }
    };
  }

  @Test
  void testApplyDiscountSuccess() {
    // Edge Case 1: Discount is successfully applied
    shipmentOptions.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderA");
    transaction.setPrice(10.0); // Current price

    boolean result = discountRule.applyDiscount(transaction, transaction.getPrice(), 5.0); // New price is 5.0
    assertTrue(result, "Discount should be applied");
    assertEquals(5.0, transaction.getPrice(), "Price should be updated after discount");
    assertEquals(5.0, transaction.getDiscount(), "Discount amount should be recorded");
  }

  @Test
  void testApplyDiscountNoDiscount() {
    // Edge Case 2: No discount applied
    shipmentOptions.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderA");
    transaction.setPrice(10.0); // Current price

    boolean result = discountRule.applyDiscount(transaction, transaction.getPrice(), 12.0); // New price is more than current
    assertFalse(result, "Discount should not be applied when the new price is higher");
    assertEquals(10.0, transaction.getPrice(), "Price should remain unchanged");
    assertEquals(0.0, transaction.getDiscount(), "No discount should be recorded");
  }

  @Test
  void testGetPriceReturnsCorrectValue() {
    // Edge Case 3: Correct price retrieval
    shipmentOptions.put("ProviderA", Map.of("S", 10.0));
    double price = discountRule.getPrice("ProviderA", "S");
    assertEquals(10.0, price, "Price should match the expected value");
  }

  @Test
  void testGetPriceReturnsZeroForNonExistentOption() {
    // Edge Case 4: Price retrieval for non-existent provider/package
    double price = discountRule.getPrice("NonExistentProvider", "S");
    assertEquals(0.0, price, "Price should be zero for non-existent provider/package");
  }

  @Test
  void testDiscountTracking() {
    // Edge Case 5: Validate discount tracking
    shipmentOptions.put("ProviderA", Map.of("S", 10.0));
    ShipmentTransaction transaction = new ShipmentTransaction(LocalDate.now(), "S", "ProviderA");
    transaction.setPrice(10.0); // Current price

    discountRule.applyDiscount(transaction, transaction.getPrice(), 5.0); // Apply a discount

    YearMonth yearMonth = YearMonth.from(transaction.getDate());
    assertEquals(5.0, discountTracker.getRemainingDiscount(yearMonth), "Remaining discount should be tracked correctly");
  }
}
