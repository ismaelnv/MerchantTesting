package com.example.demo.service;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.entity.Merchant;
import com.example.demo.mappers.MerchantMapper;
import com.example.demo.repository.MerchantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    @Mock
    private MerchantRepository _merchantRepository;

    @InjectMocks
    private MerchantService _merchantService;

    @Mock
    private MerchantMapper _merchantMapper;

    private final static String INVALIDID = "123-invalido";

    @Test
    @DisplayName("No debe permitir guardar un merchant con un nombre duplicado")
    public void shouldNotAllowDuplicateMerchantName() {

        this.mockIfEmailExists(false);
        this.mockIfNameExists(true);

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.save(createTestMerchant());
        });
    }

    @Test
    @DisplayName("No debe permitir guardar un merchant con un email duplicado")
    public void shouldNotAllowDuplicateMerchantEmail() {

        this.mockIfEmailExists(true);

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.save((createTestMerchant()));
        });
    }

    @Test
    @DisplayName("Debe guardar un merchant correctamente cuando no existen duplicados")
    public void shouldSaveMerchantSuccessfully() {
        this.mockIfEmailExists(false);
        this.mockIfNameExists(false);

        Merchant merchantMock = new Merchant();
        merchantMock.setMerchantId(UUID.randomUUID().toString());
        merchantMock.setStatus(true);

        when(_merchantMapper.merchant(createTestMerchant())).thenReturn(merchantMock);
        when(_merchantRepository.save(merchantMock)).thenReturn(merchantMock);

        Merchant merchant = _merchantService.save(createTestMerchant());
        assertNotNull(merchant);
    }


    @Test
    @DisplayName("Debe retornar todos los merchants correctamente")
    public void shouldReturnAllMerchants(){
        when(_merchantRepository.findAll())
                .thenReturn(findAllMerchant());

        List<Merchant> merchants = _merchantService.findAll();
        assertNotNull(merchants);
        assertEquals(2, merchants.size());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando el repositorio devuelve null")
    public void shouldReturnEmptyListWhenRepositoryReturnsNull(){
        when(_merchantRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Merchant> merchants = _merchantService.findAll();
        assertNotNull(merchants);
        assertTrue(merchants.isEmpty());
    }

    @Test
    @DisplayName("Debe retornar un merchant válido cuando se busca por ID existente")
    public void shouldReturnMerchantWhenIdExists(){
        when(_merchantRepository.findById("b56d4b9b-32b3-4a97-9e53-74a8b6f2db24"))
                .thenReturn(Optional.of(
                        createOjectMerchant(
                                "test@gmail.com",
                                "test2",
                                "la casa de test2",
                                "test2@gmail.com",
                                "comercio de test2",
                                "1234567891234567854",
                                "b56d4b9b-32b3-4a97-9e53-74a8b6f2db24"
                        )
                ));

        Merchant merchant = _merchantService.findById("b56d4b9b-32b3-4a97-9e53-74a8b6f2db24");
        assertNotNull(merchant);
        assertEquals("b56d4b9b-32b3-4a97-9e53-74a8b6f2db24", merchant.getMerchantId());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando se busca un ID inexistente")
    public void shouldNotFindMerchantWhenIdDoesNotExist() {
        when(_merchantRepository.findById("b56d4b9b-32b3-4a97-9e53-74a8b6f2db28"))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            _merchantService.findById("b56d4b9b-32b3-4a97-9e53-74a8b6f2db28");
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el UUID es inválido")
    public void shouldThrowExceptionForInvalidUUID() {

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.findById(INVALIDID);
        });
    }

    private List<Merchant> findAllMerchant(){
        List<Merchant> merchants = new ArrayList<Merchant>();

        merchants.add(
                createOjectMerchant(
                        "test@gmail.com",
                        "test",
                        "la casa de test",
                        "test@gmail.com",
                        "comercio de test",
                        "1234567891234567852",
                        "b56d4b9b-32b3-4a97-9e53-74a8b6f2db23"
                )
        );

        merchants.add(
                createOjectMerchant(
                        "test1@gmail.com",
                        "test1",
                        "la casa de test1",
                        "test1@gmail.com",
                        "comercio de test1",
                        "1234567891234567854",
                        "b56d4b9b-32b3-4a97-9e53-74a8b6f2db21"
                )
        );

        return merchants;
    }

    private Merchant createOjectMerchant(String email, String name,
                                         String address, String website,
                                         String description, String cardNumber,
                                         String merchatId){
        return Merchant.builder()
                .merchantId(merchatId)
                .email(email)
                .name(name)
                .address(address)
                .website(website)
                .description(description)
                .status(true)
                .cardNumber(cardNumber)
                .build();
    }

    private MerchantCreateDTO createTestMerchant(){

        return MerchantCreateDTO.builder()
                .name("depay")
                .email("depay@gmail.com")
                .address("123 Main Street")
                .website("https://www.depay.com")
                .description("Tienda de tecnología")
                .cardNumber("1234-5678-9012-3456")
                .build();
    }

    private void mockIfNameExists(boolean exists) {
        when(_merchantRepository.existsByName("depay"))
                .thenReturn(exists);
    }

    private void mockIfEmailExists(boolean exists) {
        when(_merchantRepository.existsByEmail("depay@gmail.com"))
                .thenReturn(exists);
    }

}
