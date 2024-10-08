package com.falcon.shipping.rule;

import com.falcon.shipping.discount.DiscountTracker;
import com.falcon.shipping.shipment.ShipmentTransaction;

import java.util.Map;

public class SmallPackageDiscountRule extends DiscountRuleBase {

  public SmallPackageDiscountRule(Map<String, Map<String, Double>> shipmentOptions, DiscountTracker discountTracker) {
    super(shipmentOptions, discountTracker);
  }

  @Override
  public boolean applyRule(ShipmentTransaction transaction) {
    if (transaction.getPackageSize().equals("S")) {
      double lowestPrice = Double.MAX_VALUE;

      for (String provider : shipmentOptions.keySet()) {
        double price = getPrice(provider, "S");
        lowestPrice = Math.min(lowestPrice, price);
      }

      double currentPrice = getPrice(transaction.getProvider(), "S");

      return applyDiscount(transaction, currentPrice, lowestPrice);
    }
    return false;
  }
}
