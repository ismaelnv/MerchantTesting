package com.example.demo.mappers;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.entity.Merchant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    Merchant merchant(MerchantCreateDTO merchantCreateDTO);
}
