package com.falcon.shipping.rule;

import com.falcon.shipping.shipment.ShipmentTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmallPackageDiscountRuleTest {

  private SmallPackageDiscountRule rule;
  private DiscountContext context;
  private Map<String, Map<String, Double>> priceMap;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    priceMap = Map.of(
        "MR", Map.of("S", 2.00),
        "LP", Map.of("S", 1.50)
    );
    rule = new SmallPackageDiscountRule(priceMap);
    context = new DiscountContext();
  }

  @Test
  public void testApply_appliesSmallPackageDiscount() {
    var transaction = new ShipmentTransaction(LocalDate.of(2023, 2, 1), "S", "MR", 2.00, 0);

    boolean result = rule.applyRule(transaction, context);

    assertTrue(result);
    assertEquals(1.50, transaction.getPrice());
    assertEquals(0.50, transaction.getDiscount());
  }
}
