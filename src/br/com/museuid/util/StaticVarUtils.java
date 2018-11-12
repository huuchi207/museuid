package br.com.museuid.util;

import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.SessionUserInfo;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.screen.app.AppController;

public class StaticVarUtils {
    private static UserDTO currentUser;
    private static SessionDeviceInfo sessionDeviceInfo;
    private static SessionUserInfo sessionUserInfo;
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
    public static SessionDeviceInfo getSessionDeviceInfo(){
        return  sessionDeviceInfo;
    }
    public static void clearSessionDeviceInfo(){
        sessionDeviceInfo = null;
    }

    public static void setSessionUserInfo(SessionUserInfo userInfo){
        sessionUserInfo = userInfo;
    }
    public static SessionUserInfo getSessionUserInfo(){
        if (sessionUserInfo == null){
            AppController.getInstance().startLogin();
        }
        return  sessionUserInfo;
    }
    public static void clearSessionUserInfo(){
        sessionUserInfo = null;
    }
}
