package br.com.museuid.util;

import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.screen.app.AppController;

public class StaticVarUtils {
    private static UserDTO currentUser;
    private static SessionDeviceInfo sessionDeviceInfo;
    private static String macAddress = NetworkUtils.getAddress("mac");
    private static String token;
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
        token = deviceInfo.getToken();
    }
    public static SessionDeviceInfo getSessionDeviceInfo(){
        if (sessionDeviceInfo == null){
            AppController.getInstance().startInitialize();
        }
        return  sessionDeviceInfo;
    }
    public static void clearSession(){
        sessionDeviceInfo = null;
    }

    public static String getToken() {
        return token;
    }
}
