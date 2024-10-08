package com.falcon.shipping.discount;

import lombok.Getter;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DiscountTracker {
  private final Map<YearMonth, Double> monthlyDiscounts = new HashMap<>();
  private static final double MAX_MONTHLY_DISCOUNT = 10.00;

  public void addDiscount(YearMonth yearMonth, double discount) {
    monthlyDiscounts.put(yearMonth, monthlyDiscounts.getOrDefault(yearMonth, 0.0) + discount);
  }

  public double getRemainingDiscount(YearMonth yearMonth) {
    double currentDiscount = monthlyDiscounts.getOrDefault(yearMonth, 0.0);
    return Math.max(0, MAX_MONTHLY_DISCOUNT - currentDiscount);
  }

  public double getMaxAvailableDiscount(YearMonth yearMonth, double discount) {
    double remainingDiscount = getRemainingDiscount(yearMonth);
    if (discount - remainingDiscount > 0)  {
      return remainingDiscount;
    } else {
      return discount;
    }
  }
}
