package br.com.museuid.service.remote.requestbody;

public class ChangePasswordRequest {
    private String userid;
    private String oldpassword;
    private String newpassword;

    public ChangePasswordRequest(String userid, String oldpassword, String newpassword) {
        this.userid = userid;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
}
