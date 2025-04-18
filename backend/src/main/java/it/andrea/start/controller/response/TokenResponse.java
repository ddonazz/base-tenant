package it.andrea.start.controller.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1252821933964715766L;

    private String token;

}
