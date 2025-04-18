package it.andrea.start.controller.types;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChangePassword implements Serializable {

    @Serial
    private static final long serialVersionUID = -1641230016264874514L;

    private String newPassword;
    private String repeatPassword;

}
