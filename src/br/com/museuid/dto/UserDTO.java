package br.com.museuid.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import br.com.museuid.util.BundleUtils;

public class UserDTO {

    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("update")
    @Expose
    private String update;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("userid")
    @Expose
    private String userid;
    private String roleText;
    public UserDTO() {
    }
    public UserDTO(String userName, String name, String phoneNumber,String  address,String email, String role){
        this.username = userName;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email =email;
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UserDTO copy(){
        Gson gson = new Gson();
        String itemStr = gson.toJson(this);

        return gson.fromJson(itemStr, UserDTO.class);
    }

    public String getRoleText() {
        return roleText;
    }

    public void setRoleText(String roleText) {
        this.roleText = roleText;
    }

    public enum UserRole{
        MANAGER {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_qlkd");
            }
        },
        EMPLOYEE {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_employee");
            }
        },
        ADMIN {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_admin");
            }
        },
        STOCKER {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_stocker");
            }
        };
        //example
//        if (EnumUtils.isValidEnum(OrderStatus.class, "status")){
//            String statusName = OrderStatus.valueOf("status").toString();
//        }
        public static List<UserRole> getListUserRole(){
            return Arrays.asList(UserRole.values());
        }
    }

}
