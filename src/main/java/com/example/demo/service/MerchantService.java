package com.example.demo.service;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.dto.in.MerchantUpdateDto;
import com.example.demo.entity.Merchant;
import com.example.demo.mappers.MerchantMapper;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.service.interfaz.MerchantServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.util.AppConstants.*;
import static com.example.demo.util.UUIDUtils.isValidUUID;

@Service
@RequiredArgsConstructor
public class MerchantService implements MerchantServiceI {

    private final MerchantRepository _merchantRepository;
    private final MerchantMapper _merchantMapper;

    @Override
    public Merchant save( final MerchantCreateDTO merchantCreateDTO) {

        this.validateEmailNotExists(merchantCreateDTO.getEmail());
        this.validateNameNotExists(merchantCreateDTO.getName());

        Merchant merchant = _merchantMapper.merchant(merchantCreateDTO);
        merchant.setMerchantId(UUID.randomUUID().toString());
        merchant.setStatus(true);

        return this._merchantRepository.save(merchant);
    }

    @Override
    public List<Merchant> findAll() {

        return this._merchantRepository.findAll();
    }

    @Override
    public Merchant findById(final String id) {

        if (isValidUUID(id)) {
            return this._merchantRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(MERCHANT_NOT_FOUND));
        }
        throw new IllegalArgumentException(INVALID_UUID);
    }

    @Override
    public String activate(final String id) {

        if (isValidUUID(id)) {
            this.validateIsActive(id);

            int active = this._merchantRepository.activeMerchant(id);
            if (active == 0){
                throw new NoSuchElementException(MERCHANT_NOT_FOUND);
            }

            return ACTIVATE_SUCCESS;
        }

        throw new IllegalArgumentException(INVALID_UUID);
    }

    @Override
    public String disabled(final String id) {

        if (isValidUUID(id)){
            this.validateIsInactive(id);

            int disabled = this._merchantRepository.disabledMerchant(id);
            if (disabled == 0){
                throw new NoSuchElementException(MERCHANT_NOT_FOUND);
            }

            return DISABLED_SUCCESS;
        }

        throw new IllegalArgumentException(INVALID_UUID);
    }

    @Override
    public Merchant update(final String id, final MerchantUpdateDto merchantUpdateDto) {

        Merchant merchant = this.findById(id);
        this.validateIsInactive(merchant);

        Merchant updated = Merchant.builder()
                .merchantId(merchant.getMerchantId())
                .email(Optional.ofNullable(merchantUpdateDto.getEmail())
                        .filter(s -> !s.isBlank())
                        .orElse(merchant.getEmail()))
                .name(Optional.ofNullable(merchantUpdateDto.getName())
                        .filter(s -> !s.isBlank())
                        .orElse(merchant.getName()))
                .address(Optional.ofNullable(merchantUpdateDto.getAddress())
                        .filter(s -> !s.isBlank())
                        .orElse(merchant.getAddress()))
                .website(Optional.ofNullable(merchantUpdateDto.getWebsite())
                        .filter(s -> !s.isBlank())
                        .orElse(merchant.getWebsite()))
                .description(Optional.ofNullable(merchantUpdateDto.getDescription())
                        .filter(s -> !s.isBlank())
                        .orElse(merchant.getDescription()))
                .cardNumber(Optional.ofNullable(merchantUpdateDto.getCardNumber())
                        .filter(s -> !s.isBlank())
                        .orElse(merchant.getCardNumber()))
                .build();

        return this._merchantRepository.save(updated);
    }

    private void validateEmailNotExists(final String email){

        if (this._merchantRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateNameNotExists(final String name){

        if (this._merchantRepository.existsByName(name)) {
            throw new IllegalArgumentException(NAME_ALREADY_EXISTS);
        }
    }

    private void validateIsActive(final String id){

        Merchant merchant = this.findById(id);
        if (merchant.getStatus()){
            throw new IllegalStateException(MERCHANT_ALREADY_ACTIVATED);
        }
    }

    private void validateIsInactive(final String id){

        Merchant merchant = this.findById(id);
        if (!merchant.getStatus()){
            throw new IllegalStateException(MERCHANT_ALREADY_DEACTIVATED);
        }
    }

    private void validateIsInactive(final Merchant merchant){
        if (!merchant.getStatus()) {
            throw new IllegalStateException(MERCHANT_ALREADY_DEACTIVATED);
        }
    }
}
