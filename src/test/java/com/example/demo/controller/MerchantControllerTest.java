package com.example.demo.controller;

import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.dto.in.MerchantUpdateDto;
import com.example.demo.entity.Merchant;
import com.example.demo.service.interfaz.MerchantServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MerchantController.class)
public class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantServiceI _merchantServiceI;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String EXISTING_MERCHANT_ID = "8c9f4c3d-0c0d-47a1-996c-91cd4f23f8c2";

    @Test
    @DisplayName("Get list of merchants")
    void getListOfMerchants() throws Exception {
        given(_merchantServiceI.findAll())
                .willReturn(this.getTwoMerchants());

        mockMvc.perform(get("/api/v1/merchants/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Tienda Juan"))
                .andExpect(jsonPath("$.data[1].name").value("Tienda Ana"));
    }

    private List<Merchant> getTwoMerchants() {
        Merchant merchant1 = Merchant.builder()
                .merchantId("M001")
                .email("juan@correo.com")
                .name("Tienda Juan")
                .address("Calle Falsa 123")
                .website("https://tiendajuan.com")
                .description("Tienda de productos varios")
                .status(true)
                .cardNumber("1234-5678-9012-3456")
                .build();

        Merchant merchant2 = Merchant.builder()
                .merchantId("M002")
                .email("ana@correo.com")
                .name("Tienda Ana")
                .address("Avenida Siempre Viva 456")
                .website("https://tiendaana.com")
                .description("Tienda de ropa y accesorios")
                .status(false)
                .cardNumber("9876-5432-1098-7654")
                .build();

        return List.of(merchant1, merchant2);
    }

    @Test
    @DisplayName("Should create a merchant successfully and return 201 with success message")
    void shouldCreateMerchantSuccessfully() throws Exception {

        given(_merchantServiceI.save(buildMerchantCreateDTO()))
                .willReturn(buildMerchantEntity());

        mockMvc.perform(post("/api/v1/merchants/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildMerchantCreateDTO()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Save successful"));
    }

    private MerchantCreateDTO buildMerchantCreateDTO(){
        return MerchantCreateDTO.builder()
                .email("correo@example.com")
                .name("Nombre del Comercio")
                .address("Dirección del Comercio")
                .website("https://www.ejemplo.com")
                .description("Descripción opcional del comercio")
                .cardNumber("4111111111111111")
                .build();
    }

    private Merchant buildMerchantEntity(){
        return Merchant.builder()
                .merchantId(EXISTING_MERCHANT_ID)
                .status(false)
                .email("correo@example.com")
                .name("Nombre del Comercio")
                .address("Dirección del Comercio")
                .website("https://www.ejemplo.com")
                .description("Descripción opcional del comercio")
                .cardNumber("4111111111111111")
                .build();
    }

    @Test
    @DisplayName("Should return a merchant when searching by ID and respond with 200 and success message")
    void shouldReturnMerchantByIdSuccessfully() throws Exception {

        given(_merchantServiceI.findById(EXISTING_MERCHANT_ID))
                .willReturn(buildMerchantEntity());

        mockMvc.perform(get("/api/v1/merchants/{id}", EXISTING_MERCHANT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Record found successfully"));
    }

    @Test
    @DisplayName("Should activate a merchant and return 200 with success message")
    void shouldActivateMerchantSuccessfully() throws Exception {

        given(_merchantServiceI.activate(EXISTING_MERCHANT_ID))
                .willReturn("Activation successful");

        mockMvc.perform(post("/api/v1/merchants/activate/{id}", EXISTING_MERCHANT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Activation successful"));
    }

    @Test
    @DisplayName("Should deactivate a merchant and return 200 with success message")
    void shouldDeactivateMerchantSuccessfully() throws Exception {

        given(_merchantServiceI.disabled(EXISTING_MERCHANT_ID))
                .willReturn("Deactivation successful");

        mockMvc.perform(post("/api/v1/merchants/disabled/{id}", EXISTING_MERCHANT_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Deactivation successful"));
    }

    @Test
    @DisplayName("Should update a merchant and return 200 with success message")
    void shouldUpdateMerchantSuccessfully() throws Exception {

        given(_merchantServiceI.update(EXISTING_MERCHANT_ID, this.buildMerchantUpdateDto()))
                .willReturn(this.buildMerchantEntity());

        mockMvc.perform(put("/api/v1/merchants/update/{id}", EXISTING_MERCHANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.buildMerchantUpdateDto()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Update successful"));
    }

    private MerchantUpdateDto buildMerchantUpdateDto() {
        return MerchantUpdateDto.builder()
                .email("correo@example.com")
                .name("Nombre actualizado")
                .address("Dirección actualizada")
                .website("https://www.ejemploactualizado.com")
                .description("Descripción opcional actualizada")
                .cardNumber("4111111111111111")
                .build();
    }


}
