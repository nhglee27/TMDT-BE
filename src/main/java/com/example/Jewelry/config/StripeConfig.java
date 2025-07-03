package com.example.Jewelry.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StripeConfig {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

    @PostConstruct
    public void setup() {
        Stripe.apiKey = secretKey;
    }

    public String getEndpointSecret() {
        return endpointSecret;
    }

}


