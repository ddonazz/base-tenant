package it.andrea.start.dto.user;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4014378396482783842L;

    private String role;

}
