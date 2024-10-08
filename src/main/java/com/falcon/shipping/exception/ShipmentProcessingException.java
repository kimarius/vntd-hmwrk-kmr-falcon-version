package com.falcon.shipping.exception;

public class ShipmentProcessingException extends RuntimeException {
  public ShipmentProcessingException(String message) {
    super(message);
  }

  public ShipmentProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}