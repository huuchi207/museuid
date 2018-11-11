package br.com.museuid.util;

import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.UserDTO;

public class StaticVarUtils {
    private static UserDTO currentUser;
    private static SessionDeviceInfo sessionDeviceInfo;
    private static String macAddress = NetworkUtils.getAddress("mac");

    public static void setCurrentUser(UserDTO user){
        currentUser = user;
    }
    public static void clearUser(){
        currentUser = null;
    }
    public static String getMacAddress(){
        return macAddress;
    }
    public static void setSessionDeviceInfo(SessionDeviceInfo deviceInfo){
        sessionDeviceInfo = deviceInfo;
    }
    public static void clearSession(){
        sessionDeviceInfo = null;
    }
}
