package com.falcon.shipping.discount;

import com.falcon.shipping.shipment.ShipmentOptionFileParser;
import com.falcon.shipping.shipment.ShipmentTransaction;
import com.falcon.shipping.rule.SmallPackageDiscountRule;
import com.falcon.shipping.rule.ThirdLargePackageLPDiscountRule;
import com.falcon.shipping.shipment.ShipmentTransactionFileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountCalculatorEndToEndTest {

  private DiscountCalculator discountCalculator;
  private List<ShipmentTransaction> shipmentTransactions;
  private String expectedOutputFilePath;

  @BeforeEach
  public void setUp() throws Exception {
    // Set up file paths for test resources
    String shipmentTransactionsFile = "src/test/resources/shipment_transactions.txt";
    String shipmentOptionsFile = "src/test/resources/shipment_options.txt";
    expectedOutputFilePath = "src/test/resources/expected_output.txt";

    // Load transactions and price map
    var shipmentTransactionRepository = new ShipmentTransactionFileParser();
    shipmentTransactions = shipmentTransactionRepository.loadTransactionsFromFile(shipmentTransactionsFile);

    var shipmentOptionRepository = new ShipmentOptionFileParser();
    var shipmentOptions = shipmentOptionRepository.loadOptionsFromFile(shipmentOptionsFile);

    var discountTracker = new DiscountTracker();

    // Set up discount rules
    var discountRules = List.of(
        new SmallPackageDiscountRule(shipmentOptions, discountTracker),
        new ThirdLargePackageLPDiscountRule(shipmentOptions, discountTracker)
    );

    // Initialize DiscountManager
    discountCalculator = new DiscountCalculator(shipmentOptions, discountRules);

  }

  @Test
  public void testProcessTransactions_withExpectedOutput() throws Exception {
    // Process transactions
    List<String> actualOutput = discountCalculator.processTransactions(shipmentTransactions);

    // Read the expected output file
    List<String> expectedOutput = Files.readAllLines(Paths.get(expectedOutputFilePath));

    // Compare the actual output to the expected output
    assertEquals(expectedOutput, actualOutput, "The output should match the expected results.");
  }
}
