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
                .description("Comercio de prueba para ")
                .status(true)
                .cardNumber("1234-5678-9012-3456")
                .build();

        Merchant merchant2 = Merchant.builder()
                .merchantId("3fa85f64-5717-4562-b3fc-2c963f66afa2")
                .name("Calle secundaria")
                .email("calle2@gmail.com")
                .address("123 Calle Principal")
                .website("https://wwwcalle.com")
                .description("Comercio de prueba para ")
                .status(false)
                .cardNumber("1234-5678-9012-3456")
                .build();

        _merchantRepository.saveAll(List.of(merchant1, merchant2));
    }

    @Test
    @DisplayName("existsByEmail devuelve true si el email existe")
    public void existsByEmailShouldReturnTrue(){

        boolean emailExists  = _merchantRepository.existsByEmail("calle@gmail.com");
        assertTrue(emailExists );
    }

    @Test
    @DisplayName("existsByEmail devuelve false si el email no existe")
    public void existsByEmailShouldReturnFalse(){

        boolean emailExists  = _merchantRepository.existsByEmail("calle@l.com");
        assertFalse(emailExists);
    }

    @Test
    @DisplayName("xistsByName devuelve true si el nombre existe")
    public void existsByNameShouldReturnTrue(){

        boolean nameExists = _merchantRepository.existsByName("Calle principal");
        assertTrue(nameExists);
    }

    @ParameterizedTest
    @ValueSource(strings = {"depay", "saga"})
    @DisplayName("existsByName devuelve false para nombres que no existen")
    public void existsByNameShouldReturnFalseForUnknownNames(String name){

        boolean nameExists  = _merchantRepository.existsByName(name);
        assertFalse(nameExists);
    }

    @Test
    @DisplayName("Deshabilita correctamente un merchant existente")
    public void disableMerchantShouldReturnOneForExistingMerchant(){

        _merchantRepository.disabledMerchant("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        em.flush();
        em.clear();

        Merchant merchant = _merchantRepository.findById("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .orElseThrow(() -> new AssertionError("Merchant no encontrado"));
        assertFalse(merchant.getStatus());
    }

    @Test
    @DisplayName("disableMerchant devuelve 0 cuando el merchant no existe")
    public void disableMerchantShouldReturnZeroForNonExistingMerchant(){

        int count = _merchantRepository.disabledMerchant("3fa85f24-5717-4562-b3fc-2c963f66afa6");
        assertEquals(0, count);
    }

    @Test
    @DisplayName("activateMerchant devuelve 0 cuando el merchant no existe")
    public void activateMerchantShouldReturnZeroForNonExistingMerchant(){

        int count = _merchantRepository.activeMerchant("3fa85f24-5717-4562-b3fc-2c963f66afa6");
        assertEquals(0, count);
    }

    @Test
    @DisplayName("activar correctamente un merchant existente")
    public void activateMerchantShouldReturnOneForExistingMerchant(){

        _merchantRepository.activeMerchant("3fa85f64-5717-4562-b3fc-2c963f66afa2");

        em.flush();
        em.clear();

        Merchant merchant = _merchantRepository.findById("3fa85f64-5717-4562-b3fc-2c963f66afa2")
                .orElseThrow(() -> new AssertionError("Merchant no encontrado"));
        assertTrue(merchant.getStatus());
    }
}
