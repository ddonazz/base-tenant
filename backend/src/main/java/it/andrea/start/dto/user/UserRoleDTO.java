package it.andrea.start.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4014378396482783842L;

    private String role;

}
