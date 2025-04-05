package it.andrea.start.controller.types;

import java.io.Serializable;

public class ResetPasswordContent implements Serializable {

    private static final long serialVersionUID = -1106588086535126442L;

    private String uuid;
    private String newPassword;
    private String repeatPassword;

    public String getUuid() {
	return uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    public String getNewPassword() {
	return newPassword;
    }

    public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
	return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
	this.repeatPassword = repeatPassword;
    }

}
