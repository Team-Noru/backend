package com.example.noru.price.config;

import com.example.noru.price.dto.PriceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PriceParsingConfig {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static PriceDto parsePrice(String stockCode, String json) {

        try {
            JsonNode root = mapper.readTree(json);

            // =========================
            // 1️⃣ 해외 (output.t_xprc 존재)
            // =========================
            JsonNode output = root.get("output");
            if (output != null && output.has("t_xprc")) {

                int price = output.get("t_xprc").asInt();
                int diffPrice = output.get("t_xdif").asInt();

                String rateStr = output.get("t_xrat").asText(); // "+0.58"
                double diffRate = Double.parseDouble(rateStr.replace("+", ""));

                return new PriceDto(
                        stockCode,
                        price,
                        diffPrice,
                        diffRate
                );
            }

            // =========================
            // 2️⃣ 국내 (기존 로직 유지)
            // =========================
            JsonNode outputNode = root.path("output");

            int price = outputNode.path("stck_prpr").asInt();
            int diffPrice = outputNode.path("prdy_vrss").asInt();
            double diffRate = outputNode.path("prdy_ctrt").asDouble();

            return new PriceDto(
                    stockCode,
                    price,
                    diffPrice,
                    diffRate
            );

        } catch (Exception e) {
            throw new RuntimeException("Price JSON parsing error", e);
        }
    }
}
