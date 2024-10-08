package com.falcon.shipping.rule;

import com.falcon.shipping.discount.DiscountTracker;
import com.falcon.shipping.shipment.ShipmentTransaction;

import java.util.Map;
import java.util.HashMap;
import java.time.YearMonth;

public class ThirdLargePackageLPDiscountRule extends DiscountRuleBase {

  private final Map<YearMonth, Integer> lpLargeCountPerMonth = new HashMap<>();

  public ThirdLargePackageLPDiscountRule(Map<String, Map<String, Double>> shipmentOptions, DiscountTracker discountTracker) {
    super(shipmentOptions, discountTracker);
  }

  @Override
  public boolean applyRule(ShipmentTransaction transaction) {
    if (transaction.getPackageSize().equals("L") && transaction.getProvider().equals("LP")) {
      YearMonth yearMonth = YearMonth.from(transaction.getDate());
      incrementLpLargeCount(yearMonth);
      int largeShipmentCount = getLpLargeCount(yearMonth);


      if (largeShipmentCount == 3) { // For the 3rd large package, full discount is applied
        double price = getPrice("LP", "L");

        // Use the applyDiscount method from the DiscountRuleBase
        return applyDiscount(transaction, price, 0.0);
      }
    }
    return false;
  }

  // Logic for incrementing LP large package count
  private void incrementLpLargeCount(YearMonth yearMonth) {
    lpLargeCountPerMonth.put(yearMonth, getLpLargeCount(yearMonth) + 1);
  }

  // Logic for retrieving the LP large package count
  private int getLpLargeCount(YearMonth yearMonth) {
    return lpLargeCountPerMonth.getOrDefault(yearMonth, 0);
  }
}
