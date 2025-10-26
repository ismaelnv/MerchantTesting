package com.example.demo.service;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.entity.Merchant;
import com.example.demo.mappers.MerchantMapper;
import com.example.demo.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    @Mock
    private MerchantRepository _merchantRepository;

    @InjectMocks
    private MerchantService _merchantService;

    @Mock
    private MerchantMapper _merchantMapper;

    @Test
    public void shouldNotAllowDuplicateMerchantName() {

        this.mockIfEmailExists(false);
        this.mockIfNameExists(true);

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.save(createTestMerchant());
        });
    }

    @Test
    public void shouldNotAllowDuplicateMerchantEmail() {

        this.mockIfEmailExists(true);

        assertThrows(IllegalArgumentException.class, () -> {
            _merchantService.save((createTestMerchant()));
        });
    }

    @Test
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
