package br.com.museuid.util;

import br.com.museuid.dto.UserDTO;

public class UserUtils {
    private static UserDTO currentUser;

    public static void setCurrentUser(UserDTO user){
        currentUser = user;
    }
    public static void clearUser(){
        currentUser = null;
    }
}
