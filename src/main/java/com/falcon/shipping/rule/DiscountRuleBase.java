package com.falcon.shipping.rule;

import com.falcon.shipping.shipment.ShipmentTransaction;
import com.falcon.shipping.discount.DiscountTracker;

import java.time.YearMonth;
import java.util.Map;

public abstract class DiscountRuleBase {

  protected final Map<String, Map<String, Double>> shipmentOptions;
  private final DiscountTracker discountTracker;

  public DiscountRuleBase(Map<String, Map<String, Double>> shipmentOptions, DiscountTracker discountTracker) {
    this.shipmentOptions = shipmentOptions;
    this.discountTracker = discountTracker;
  }

  // This method calculates the discount based on the context and applies it
  protected boolean applyDiscount(ShipmentTransaction transaction, double currentPrice,
                                  double newPrice) {
    if (currentPrice < newPrice) {
      return false;
    }

    double discount = currentPrice - newPrice;

    YearMonth yearMonth = YearMonth.from(transaction.getDate());
    discount = discountTracker.getMaxAvailableDiscount(yearMonth, discount);

    if (transaction.getDiscount() > 0) { //if discount was already applied for same transaction
      discount = transaction.getDiscount() + discount;
    }

    newPrice = currentPrice - discount;

    if (discount > 0) {
      transaction.setPrice(newPrice);
      transaction.setDiscount(discount);
      discountTracker.addDiscount(yearMonth, discount);
      return true;
    }
    return false;
  }

  protected double getPrice(String provider, String packageSize) {
    // Return the price if found, otherwise return 0.0
    return shipmentOptions.getOrDefault(provider, Map.of()).getOrDefault(packageSize, 0.0);
  }

  // Abstract method to be implemented by specific rules to define how they apply
  public abstract boolean applyRule(ShipmentTransaction transaction);
}
