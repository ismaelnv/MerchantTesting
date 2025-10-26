package com.example.demo.repository;

import com.example.demo.entity.Merchant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MerchantRepository extends JpaRepository<Merchant, String> {

    @Modifying
    @Transactional
    @Query("UPDATE Merchant m SET m.status = false WHERE m.merchantId = :merchantId")
    int disabledMerchant(@Param("merchantId") String merchantId);

    @Modifying
    @Transactional
    @Query("UPDATE Merchant m SET m.status = true WHERE m.merchantId = :merchantId")
    int activeMerchant(@Param("merchantId") String merchantId);

    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
