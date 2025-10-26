package com.example.demo.service.interfaz;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.dto.in.MerchantUpdateDto;
import com.example.demo.entity.Merchant;

import java.util.List;

public interface MerchantServiceI {

    Merchant save(MerchantCreateDTO merchantCreateDTO);
    List<Merchant> findAll();
    Merchant findById(String id);
    String activate(String id);
    String disabled(String id);
    Merchant update(String id, MerchantUpdateDto merchantUpdateDto);
}
