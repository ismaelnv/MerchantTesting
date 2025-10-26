package com.example.demo.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class MerchantCreateDTO {

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El name es obligatorio")
    private String name;

    @NotBlank(message = "La address es obligatoria")
    private String address;

    @NotBlank(message = "El website es obligatorio")
    @URL(message = "La URL del sitio web no es válida")
    private String website;

    private String description;

    @NotBlank(message = "El cardNumber es obligatorio")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "El número de tarjeta debe contener solo números y tener entre 13 y 19 dígitos")
    private String cardNumber;
}
