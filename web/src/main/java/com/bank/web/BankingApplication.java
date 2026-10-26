
package com.bank.web;

import com.bank.service.BankService;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class BankingApplication {

    public static void main(String[] args) throws Exception {

        BankService bank = new BankService();

        HttpServer server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 8080), 0);

        server.createContext("/", exchange -> {

            // Enable CORS for the HTML frontend
            var headers = exchange.getResponseHeaders();

            headers.set("Access-Control-Allow-Origin", "*");
            headers.set("Access-Control-Allow-Methods",
                    "GET, POST, OPTIONS");
            headers.set("Access-Control-Allow-Headers",
                    "Content-Type");
            headers.set("Content-Type",
                    "application/json; charset=UTF-8");

            // Handle browser preflight requests
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            int status = 200;
            String response;

            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();

                // Check balance
                if (path.equals("/balance") &&
                        method.equals("GET")) {

                    response = "{\"balance\":" +
                            bank.balance() + "}";
                }

                // Deposit or withdraw money
                else if ((path.equals("/deposit") ||
                        path.equals("/withdraw")) &&
                        method.equals("POST")) {

                    String query = exchange.getRequestURI().getRawQuery();

                    if (query == null ||
                            !query.startsWith("amount=")) {

                        throw new IllegalArgumentException(
                                "Use ?amount=VALUE");
                    }

                    double amount = Double.parseDouble(
                            URLDecoder.decode(
                                    query.substring(7),
                                    StandardCharsets.UTF_8));

                    double balance = path.equals("/deposit")
                            ? bank.deposit(amount)
                            : bank.withdraw(amount);

                    response = "{\"balance\":" +
                            balance + "}";
                }

                else {
                    status = 404;
                    response = "{\"error\":\"Endpoint not found\"}";
                }

            } catch (IllegalArgumentException e) {

                status = 400;
                response = "{\"error\":\"Invalid amount or insufficient funds\"}";
            }

            byte[] bytes = response.getBytes(
                    StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(
                    status, bytes.length);

            try (var out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        });

        server.start();

        System.out.println(
                "Banking API running at http://localhost:8080/balance");
    }
}
