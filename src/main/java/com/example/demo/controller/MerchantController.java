package com.example.demo.controller;


import com.example.demo.dto.in.MerchantCreateDTO;
import com.example.demo.dto.in.MerchantUpdateDto;
import com.example.demo.entity.Merchant;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.ResponseStatus;
import com.example.demo.service.interfaz.MerchantServiceI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static com.example.demo.response.ApiResponse.createResponse;
import static com.example.demo.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantServiceI _merchantServiceI;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Merchant>> save(@Valid @RequestBody MerchantCreateDTO merchantCreateDTO){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    createResponse(ResponseStatus.SUCCESS,
                            SAVE_SUCCESS,
                            this._merchantServiceI.save(merchantCreateDTO)
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(ResponseStatus.ERROR,
                            INTERNAL_ERROR,
                            null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Merchant>>> findAll(){

        try{
            return ResponseEntity.ok(createResponse(ResponseStatus.SUCCESS,
                    FIND_ALL_SUCCESS,
                    this._merchantServiceI.findAll()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(ResponseStatus.ERROR,
                            INTERNAL_ERROR,
                            null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Merchant>> findById(@PathVariable String id){

        try{
            return ResponseEntity.ok(createResponse(ResponseStatus.SUCCESS,
                    FIND_BY_ID_SUCCESS,
                    this._merchantServiceI.findById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(ResponseStatus.ERROR,
                            INTERNAL_ERROR,
                            null));
        }
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable String id){

        try {
            return ResponseEntity.ok(createResponse(ResponseStatus.SUCCESS,
                    this._merchantServiceI.activate(id),
                    null));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(ResponseStatus.ERROR,
                            INTERNAL_ERROR,
                            null));
        }
    }

    @PostMapping("/disabled/{id}")
    public ResponseEntity<?> disabled(@PathVariable String id){

        try {
            return ResponseEntity.ok(createResponse(ResponseStatus.SUCCESS,
                    this._merchantServiceI.disabled(id),
                    null));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(ResponseStatus.ERROR,
                            INTERNAL_ERROR,
                            null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody MerchantUpdateDto merchantUpdateDto){

        try {
            return ResponseEntity.ok(createResponse(ResponseStatus.SUCCESS,
                    UPDATE_SUCCESS,
                    this._merchantServiceI.update(id,merchantUpdateDto
                    )));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createResponse(ResponseStatus.ERROR,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(ResponseStatus.ERROR,
                            INTERNAL_ERROR,
                            null));
        }
    }

}
