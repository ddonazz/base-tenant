package it.andrea.start.dto.user;

import java.time.LocalDateTime;

public class JWTokenUserDTO extends UserDTO {

    private static final long serialVersionUID = -7835078325958780326L;

    private LocalDateTime fireToken;

    public LocalDateTime getFireToken() {
	return fireToken;
    }

    public void setFireToken(LocalDateTime fireToken) {
	this.fireToken = fireToken;
    }

}
