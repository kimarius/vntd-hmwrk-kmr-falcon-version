package com.falcon.shipping.shipment;

import com.falcon.shipping.utl.DateFormatterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import com.falcon.shipping.exception.ShipmentProcessingException;

public class ShipmentTransactionFileParser {
  private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentTransactionFileParser.class);

  public List<ShipmentTransaction> loadTransactionsFromFile(String fileName) {
    List<ShipmentTransaction> shipmentTransactions = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        try {
          shipmentTransactions.add(parseInputLineAndCreateShipmentTransaction(line));
        } catch (ShipmentProcessingException e) {
          LOGGER.error("Skipping invalid transaction entry: {}", line, e);
        }
      }
    } catch (IOException e) {
      throw new ShipmentProcessingException("Error reading shipment transactions file", e);
    }
    return shipmentTransactions;
  }

  private ShipmentTransaction parseInputLineAndCreateShipmentTransaction(String inputLine) {
    String[] parts = inputLine.split(" ");
    if (parts.length != 3) {
      throw new ShipmentProcessingException("Invalid transaction format: " + inputLine);
    }

    LocalDate date;
    try {
      date = LocalDate.parse(parts[0], DateFormatterUtil.DEFAULT_DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new ShipmentProcessingException("Invalid date format: " + parts[0], e);
    }

    String packageSize = parts[1];
    String provider = parts[2];
    return new ShipmentTransaction(date, packageSize, provider);
  }
}
