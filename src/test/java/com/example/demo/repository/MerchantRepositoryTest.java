package com.example.demo.repository;

import com.example.demo.entity.Merchant;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MerchantRepositoryTest {

    @Autowired
    private MerchantRepository _merchantRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setup() {
        Merchant merchant1 = Merchant.builder()
                .merchantId("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .name("Calle principal")
                .email("calle@gmail.com")
                .address("123 Calle Principal")
                .website("https://wwwcalle.com")
                .description("Test merchant")
                .status(true)
                .cardNumber("1234-5678-9012-3456")
                .build();

        Merchant merchant2 = Merchant.builder()
                .merchantId("3fa85f64-5717-4562-b3fc-2c963f66afa2")
                .name("Calle secundaria")
                .email("calle2@gmail.com")
                .address("123 Calle Principal")
                .website("https://wwwcalle.com")
                .description("Test merchant")
                .status(false)
                .cardNumber("1234-5678-9012-3456")
                .build();

        _merchantRepository.saveAll(List.of(merchant1, merchant2));
    }

    @Test
    @DisplayName("existsByEmail returns true if the email exists")
    public void existsByEmailShouldReturnTrue() {
        boolean emailExists = _merchantRepository.existsByEmail("calle@gmail.com");
        assertTrue(emailExists);
    }

    @Test
    @DisplayName("existsByEmail returns false if the email does not exist")
    public void existsByEmailShouldReturnFalse() {
        boolean emailExists = _merchantRepository.existsByEmail("calle@l.com");
        assertFalse(emailExists);
    }

    @Test
    @DisplayName("existsByName returns true if the name exists")
    public void existsByNameShouldReturnTrue() {
        boolean nameExists = _merchantRepository.existsByName("Calle principal");
        assertTrue(nameExists);
    }

    @ParameterizedTest
    @ValueSource(strings = {"depay", "saga"})
    @DisplayName("existsByName returns false for unknown names")
    public void existsByNameShouldReturnFalseForUnknownNames(String name) {
        boolean nameExists = _merchantRepository.existsByName(name);
        assertFalse(nameExists);
    }

    @Test
    @DisplayName("disableMerchant correctly disables an existing merchant")
    public void disableMerchantShouldReturnOneForExistingMerchant() {
        _merchantRepository.disabledMerchant("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        em.flush();
        em.clear();

        Merchant merchant = _merchantRepository.findById("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .orElseThrow(() -> new AssertionError("Merchant not found"));
        assertFalse(merchant.getStatus());
    }

    @Test
    @DisplayName("disableMerchant returns 0 when the merchant does not exist")
    public void disableMerchantShouldReturnZeroForNonExistingMerchant() {
        int count = _merchantRepository.disabledMerchant("3fa85f24-5717-4562-b3fc-2c963f66afa6");
        assertEquals(0, count);
    }

    @Test
    @DisplayName("activateMerchant returns 0 when the merchant does not exist")
    public void activateMerchantShouldReturnZeroForNonExistingMerchant() {
        int count = _merchantRepository.activeMerchant("3fa85f24-5717-4562-b3fc-2c963f66afa6");
        assertEquals(0, count);
    }

    @Test
    @DisplayName("activateMerchant correctly activates an existing merchant")
    public void activateMerchantShouldReturnOneForExistingMerchant() {
        _merchantRepository.activeMerchant("3fa85f64-5717-4562-b3fc-2c963f66afa2");

        em.flush();
        em.clear();

        Merchant merchant = _merchantRepository.findById("3fa85f64-5717-4562-b3fc-2c963f66afa2")
                .orElseThrow(() -> new AssertionError("Merchant not found"));
        assertTrue(merchant.getStatus());
    }
}