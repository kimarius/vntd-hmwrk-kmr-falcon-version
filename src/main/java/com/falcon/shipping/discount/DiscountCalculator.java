package com.falcon.shipping.discount;

import com.falcon.shipping.exception.ShipmentProcessingException;
import com.falcon.shipping.shipment.ShipmentTransaction;
import com.falcon.shipping.rule.DiscountRuleBase;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Getter
public class DiscountCalculator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DiscountCalculator.class);
  private final Map<String, Map<String, Double>> shipmentOptionMap;
  private final List<DiscountRuleBase> DiscountRules;

  public DiscountCalculator(Map<String, Map<String, Double>> shipmentOptionMap, List<DiscountRuleBase> DiscountRules) {
    this.shipmentOptionMap = shipmentOptionMap;
    this.DiscountRules = DiscountRules;
  }

  public List<String> processTransactions(List<ShipmentTransaction> transactions) {
    for (ShipmentTransaction transaction : transactions) {
      try {
        boolean ruleApplied = false;

        for (DiscountRuleBase rule : DiscountRules) {
          if (rule.applyRule(transaction)) {
            ruleApplied = true;
          }
        }

        if (!ruleApplied) {
          setDefaultPrice(transaction);
        }
      } catch (Exception e) {
        throw new ShipmentProcessingException("Error processing shipment transaction.", e);
      }
    }

    // Return the formatted output
    return transactions.stream().map(ShipmentTransaction::toString).toList();
  }

  private void setDefaultPrice(ShipmentTransaction transaction) {
    String provider = transaction.getProvider();
    String packageSize = transaction.getPackageSize();
    Double price = shipmentOptionMap.getOrDefault(provider, Map.of()).get(packageSize);

    if (price != null) {
      transaction.setPrice(price);
      transaction.setDiscount(0.0);  // No discount applied yet
    } else {
      LOGGER.error("Error: No price found for provider: {}, package size: {}", provider, packageSize);
    }
  }
}
