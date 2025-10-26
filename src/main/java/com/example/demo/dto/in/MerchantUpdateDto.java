package com.example.demo.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class MerchantUpdateDto {

    @Email(message = "El formato del email no es válido")
    private String email;

    private String name;
    private String address;

    @URL(message = "La URL del sitio web no es válida")
    private String website;
    private String description;

    @Pattern(regexp = "^[0-9]{13,19}$", message = "El número de tarjeta debe contener solo números y tener entre 13 y 19 dígitos")
    private String cardNumber;
}
