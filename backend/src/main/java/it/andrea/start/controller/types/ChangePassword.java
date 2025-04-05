package it.andrea.start.controller.types;

import java.io.Serializable;

public class ChangePassword implements Serializable {

    private static final long serialVersionUID = -1641230016264874514L;

    private String newPassword;
    private String repeatPassword;

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
