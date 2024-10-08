package com.falcon.shipping.rule;

import com.falcon.shipping.shipment.ShipmentTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThirdLargePackageLPDiscountRuleTest {

  private ThirdLargePackageLPDiscountRule rule;
  private DiscountContext context;
  private Map<String, Map<String, Double>> priceMap;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    priceMap = Map.of(
        "LP", Map.of("L", 6.90)
    );
    rule = new ThirdLargePackageLPDiscountRule(priceMap);
    context = new DiscountContext();
  }

  @Test
  public void testApply_appliesThirdLargeLPDiscount() {
    var transaction1 = new ShipmentTransaction(LocalDate.of(2023, 2, 1), "L", "LP");
    var transaction2 = new ShipmentTransaction(LocalDate.of(2023, 2, 2), "L", "LP");
    var transaction3 = new ShipmentTransaction(LocalDate.of(2023, 2, 3), "L", "LP");

    rule.applyRule(transaction1, context);
    rule.applyRule(transaction2, context);

    // Third one should trigger the discount
    boolean result = rule.applyRule(transaction3, context);

    assertTrue(result);
    assertEquals(0.00, transaction3.getPrice());
    assertEquals(6.90, transaction3.getDiscount());
  }
}
