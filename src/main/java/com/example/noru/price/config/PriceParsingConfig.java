package com.example.noru.price.config;

import com.example.noru.price.dto.PriceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PriceParsingConfig {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static PriceDto parsePrice(String code, String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode output = root.get("output");

            if (output == null || output.get("stck_prpr") == null) {
                return new PriceDto(code, -1);
            }

            long price = output.get("stck_prpr").asLong();
            return new PriceDto(code, price);

        } catch (Exception e) {
            return new PriceDto(code, -1); // 오류 시 -1
        }
    }
}
