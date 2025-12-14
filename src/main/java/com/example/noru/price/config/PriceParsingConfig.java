package com.example.noru.price.config;

import com.example.noru.price.dto.PriceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PriceParsingConfig {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static PriceDto parsePrice(String stockCode, String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode output = root.get("output");

            if (output == null) {
                return empty(stockCode);
            }

            long price = output.path("stck_prpr").asLong(-1);
            long diffPrice = output.path("prdy_vrss").asLong(0);
            double diffRate = output.path("prdy_ctrt").asDouble(0.0);

            return new PriceDto(
                    stockCode,
                    price,
                    diffPrice,
                    diffRate
            );

        } catch (Exception e) {
            return empty(stockCode);
        }
    }

    private static PriceDto empty(String stockCode) {
        return new PriceDto(stockCode, -1, 0, 0.0);
    }
}
