package br.com.museuid.util;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.SessionUserInfo;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.screen.app.AppController;

public class StaticVarUtils {
    private static SessionDeviceInfo sessionDeviceInfo;
    private static SessionUserInfo sessionUserInfo;
    private static String macAddress = NetworkUtils.getAddress("mac");
    private static String token;
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
        token = sessionUserInfo.getToken();
    }
    public static SessionUserInfo getSessionUserInfo(){
        if(ConstantConfig.FAKE){
            SessionUserInfo sessionUserInfo = new SessionUserInfo();
            UserDTO userDTO = new UserDTO();
            sessionUserInfo.setInfo(userDTO);
            return sessionUserInfo;
        }
        if (sessionUserInfo == null ){
            AppController.getInstance().startLogin();
        }
        return  sessionUserInfo;
    }
    public static void clearSessionUserInfo(){
        sessionUserInfo = null;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        StaticVarUtils.token = token;
    }
}
