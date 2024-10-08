package com.falcon.shipping.shipment;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShipmentTransactionTest {

  @Test
  public void testParseInputLineAndCreateShipmentTransaction() {
    String inputLine = "2023-02-01 S MR";
    ShipmentTransaction transaction = ShipmentTransaction.parseInputLineAndCreateShipmentTransaction(inputLine);

    assertEquals(LocalDate.of(2023, 2, 1), transaction.date());
    assertEquals("S", transaction.packageSize());
    assertEquals("MR", transaction.provider());
  }

  @Test
  public void testToString_formatsCorrectly() {
    ShipmentTransaction transaction = new ShipmentTransaction(
        LocalDate.of(2023, 2, 1), "S", "MR", 1.50, 0.50);

    String result = transaction.toString();
    assertEquals("2023-02-01 S MR 1.50 0.50", result);
  }
}
