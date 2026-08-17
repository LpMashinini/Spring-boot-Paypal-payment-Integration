package com.payment.PayPal_integration.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final APIContext apiContext;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
            ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f", total)); // formats total to float


        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>(); // Handles multiple payment
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method); // a method to decide whether we wanna pay with paypal balance, credit card

        Payment payment = new Payment();

        payment.setIntent(intent);
        payment.setPayer(payer); // set the type of payments user chose
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl); // handles payment cancellation
        redirectUrls.setReturnUrl(successUrl); // handles success payment
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }
}
