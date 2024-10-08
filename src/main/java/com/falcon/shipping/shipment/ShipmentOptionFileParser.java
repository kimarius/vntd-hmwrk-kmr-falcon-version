package com.falcon.shipping.shipment;

import com.falcon.shipping.exception.ShipmentProcessingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShipmentOptionFileParser {

  public Map<String, Map<String, Double>> loadOptionsFromFile(String fileName) {
    Map<String, Map<String, Double>> shipmentOptionMap = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(" ");
        String provider = parts[0];
        String packageSize = parts[1];

        double price;
        try {
          price = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
          throw new ShipmentProcessingException("Invalid price format in line: " + line + " - " + e.getMessage());
        }

        // Add data to the map
        shipmentOptionMap.computeIfAbsent(provider, k -> new HashMap<>());
        shipmentOptionMap.get(provider).put(packageSize, price);
      }
    } catch (IOException e) {
      throw new ShipmentProcessingException("Error reading shipment options file", e);
    }
    return shipmentOptionMap;
  }
}
