package com.falcon.shipping;

import com.falcon.shipping.discount.DiscountCalculator;
import com.falcon.shipping.discount.DiscountTracker;
import com.falcon.shipping.rule.SmallPackageDiscountRule;
import com.falcon.shipping.rule.ThirdLargePackageLPDiscountRule;
import com.falcon.shipping.shipment.ShipmentOptionFileParser;
import com.falcon.shipping.shipment.ShipmentTransactionFileParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ShipmentCalculatorApplication
		implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentCalculatorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		var shipmentTransactionsFile = "src/test/resources/shipment_transactions.txt";
		var shipmentOptionsFile = "src/test/resources/shipment_options.txt";

		//var shipmentTransactionsFile = "src/test/resources/shipment_transactions_with_errors.txt";
		//var shipmentOptionsFile = "src/test/resources/shipment_options_with_errors.txt";

		var shipmentTransactionRepository = new ShipmentTransactionFileParser();
		var shipmentTransactions = shipmentTransactionRepository.loadTransactionsFromFile(shipmentTransactionsFile);

		var shipmentOptionRepository = new ShipmentOptionFileParser();
		var shipmentOptions = shipmentOptionRepository.loadOptionsFromFile(shipmentOptionsFile);

		var discountTracker = new DiscountTracker();

		var discountRules = List.of(
				new SmallPackageDiscountRule(shipmentOptions, discountTracker),
				new ThirdLargePackageLPDiscountRule(shipmentOptions, discountTracker)
		);

		var discountManager = new DiscountCalculator(shipmentOptions, discountRules);
		var output = discountManager.processTransactions(shipmentTransactions);
		for (String line : output) {
			System.out.println(line);
		}
	}
}
