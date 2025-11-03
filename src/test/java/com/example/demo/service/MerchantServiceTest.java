package com.example.demo.service;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.dto.in.MerchantUpdateDto;
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

    private final static String INVALID_ID = "123-invalido";
    private final static String VALID_ID = "b56d4b9b-32b3-4a97-9e53-74a8b6f2db24";

    @Test
    @DisplayName("No permite guardar un merchant si el nombre ya existe")
    public void shouldNotAllowDuplicateMerchantName() {

        this.mockIfEmailExists(false);
        this.mockIfNameExists(true);

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.save(createTestMerchant());
        });
    }

    @Test
    @DisplayName("No permite guardar un merchant si el email ya existe")
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
        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.of(
                        createOjectMerchant(
                                true
                        )
                ));

        Merchant merchant = _merchantService.findById(VALID_ID);
        assertNotNull(merchant);
        assertEquals(VALID_ID, merchant.getMerchantId());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando se busca un ID inexistente")
    public void shouldNotFindMerchantWhenIdDoesNotExist() {
        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            _merchantService.findById(VALID_ID);
        });
    }

    @Test
    @DisplayName("Lanza IllegalArgumentException al buscar merchant con UUID inválido")
    public void shouldThrowExceptionForInvalidUUID() {

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.findById(INVALID_ID);
        });
    }

    //test
    @Test
    @DisplayName("Lanza IllegalArgumentException al activar merchant con UUID inválido")
    public void shouldThrowExceptionWhenUUIDIsInvalid() {

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.activate(INVALID_ID);
        });
    }

    @Test
    @DisplayName("Lanza IllegalStateException al intentar activar un merchant ya activo")
    public void shouldThrowExceptionWhenMerchantIsAlreadyActive() {

        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(createOjectMerchant(
                        true
                )));

        assertThrows(IllegalStateException.class, () -> {
            _merchantService.activate(VALID_ID);
        });
    }

    @Test
    @DisplayName("Debe lanzar NoSuchElementException si no se activa ningún merchant")
    public void shouldThrowNoSuchElementExceptionWhenNoMerchantIsActivated() {

        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(createOjectMerchant(
                        false
                )));

        when(_merchantRepository.activeMerchant(VALID_ID)).
                thenReturn(0);

        assertThrows(NoSuchElementException.class, () -> {
            _merchantService.activate(VALID_ID);
        });
    }

    @Test
    @DisplayName("Retorna mensaje de éxito al activar un merchant existente correctamente")
    public void shouldReturnActivateSuccessWhenMerchantIsActivated() {

        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(createOjectMerchant(
                        false
                )));

        when(_merchantRepository.activeMerchant(VALID_ID))
                .thenReturn(1);

        String message = this._merchantService.activate(VALID_ID);
        assertNotNull(message);
        assertFalse(message.isEmpty());
    }

    @Test
    @DisplayName("Lanza IllegalArgumentException al desactivar un merchant con UUID inválido")
    public void shouldThrowExceptionWhenDisableMerchantWithInvalidUUID() {

        assertThrows(IllegalArgumentException.class, () -> {
            this._merchantService.disabled(INVALID_ID);
        });
    }

    @Test
    @DisplayName("Lanza IllegalStateException si el merchant ya está desactivado")
    public void shouldThrowExceptionWhenDisableAlreadyDisabledMerchant() {

        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(createOjectMerchant(
                        false
                )));

        assertThrows(IllegalStateException.class, () -> {
            this._merchantService.disabled(VALID_ID);
        });
    }

    @Test
    @DisplayName("Lanza NoSuchElementException si no se logra desactivar el merchant")
    public void shouldThrowNoSuchElementExceptionWhenMerchantCannotBeDisabled() {

        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(createOjectMerchant(
                        true
                )));

        when(_merchantRepository.disabledMerchant(VALID_ID))
                .thenReturn(0);

        assertThrows(NoSuchElementException.class, () -> {
            this._merchantService.disabled(VALID_ID);
        });
    }

    @Test
    @DisplayName("Retorna mensaje de éxito al desactivar un merchant activo correctamente")
    public void shouldReturnSuccessMessageWhenMerchantIsDisabled() {

        when(_merchantRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(createOjectMerchant(
                        true
                )));

        when(_merchantRepository.disabledMerchant(VALID_ID))
                .thenReturn(1);

        String message = this._merchantService.disabled(VALID_ID);

        assertNotNull(message);
        assertFalse(message.isEmpty());
        assertEquals("Deactivation successful", message);
    }

    @Test
    @DisplayName("Lanza IllegalArgumentException si el ID del merchant es inválido en update")
    public void shouldThrowExceptionWhenUpdatingWithInvalidUUID() {
        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.update(INVALID_ID, createMerchantUpdateDto());
        });
    }

    @Test
    @DisplayName("Lanza NoSuchElementException si el merchant no existe al intentar actualizar")
    public void shouldThrowExceptionWhenUpdatingNonExistingMerchant() {
        when(_merchantRepository.findById(VALID_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            _merchantService.update(VALID_ID, createMerchantUpdateDto());
        });
    }

    @Test
    @DisplayName("Lanza IllegalStateException si el merchant está activo y se intenta actualizar")
    public void shouldThrowExceptionWhenUpdatingActiveMerchant() {
        Merchant activeMerchant = createOjectMerchant(false);
        when(_merchantRepository.findById(VALID_ID)).thenReturn(Optional.of(activeMerchant));

        assertThrows(IllegalStateException.class, () -> {
            _merchantService.update(VALID_ID, createMerchantUpdateDto());
        });
    }

    @Test
    @DisplayName("Lanza IllegalStateException si el merchant está inactivo y se intenta actualizar")
    public void shouldThrowExceptionWhenUpdatingInactiveMerchant() {
        Merchant inactiveMerchant = createOjectMerchant(false);
        when(_merchantRepository.findById(VALID_ID)).thenReturn(Optional.of(inactiveMerchant));

        assertThrows(IllegalStateException.class, () -> {
            _merchantService.update(VALID_ID, createMerchantUpdateDto());
        });
    }

    private MerchantUpdateDto createMerchantUpdateDto() {
        return MerchantUpdateDto.builder()
                .name("Updated Name")
                .email("updated@mail.com")
                .address("New Address 123")
                .website("https://updated.com")
                .description("Updated description")
                .cardNumber("1111-2222-3333-4444")
                .build();
    }

    private List<Merchant> findAllMerchant(){
        List<Merchant> merchants = new ArrayList<Merchant>();

        merchants.add(
                createOjectMerchant(
                        true
                )
        );

        merchants.add(
                createOjectMerchant(
                        true
                )
        );

        return merchants;
    }

    private Merchant createOjectMerchant(boolean status){
        return Merchant.builder()
                .merchantId("b56d4b9b-32b3-4a97-9e53-74a8b6f2db24")
                .email("test1@gmail.com")
                .name("test1")
                .address("la casa de test1")
                .website("test1@gmail.com")
                .description("comercio de test1")
                .status(status)
                .cardNumber("1234567891234567854")
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
