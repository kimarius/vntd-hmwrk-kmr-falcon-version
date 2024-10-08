package com.falcon.shipping.shipment;

import com.falcon.shipping.utl.DateFormatterUtil;
import com.falcon.shipping.utl.LocaleUtil;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
public class ShipmentTransaction {

  private final LocalDate date;
  private final String packageSize;
  private final String provider;
  private double price;
  private double discount;

  public ShipmentTransaction(LocalDate date, String packageSize, String provider) {
    this.date = date;
    this.packageSize = packageSize;
    this.provider = provider;
  }

  @Override
  public String toString() {
    return String.format(
        LocaleUtil.DEFAULT_LOCALE,
        "%s %s %s %.2f %s",
        DateFormatterUtil.formatDate(date),
        packageSize,
        provider,
        price,
        (discount > 0 ? String.format(LocaleUtil.DEFAULT_LOCALE,"%.2f", discount) : "-")
    );
  }
}
