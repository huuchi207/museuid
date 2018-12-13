package br.com.museuid.util;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.dto.SessionUserInfo;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.screen.app.AppController;

import static br.com.museuid.config.ConstantConfig.APP_DATA_FOLDER_NAME;
import static br.com.museuid.config.ConstantConfig.APP_NAME;

public class StaticVarUtils {
  private static SessionDeviceInfo sessionDeviceInfo;
  private static SessionUserInfo sessionUserInfo;
  private static String macAddress = NetworkUtils.getAddress("mac");
  private static String token;
  private static String APP_DATA_PATH = System.getProperty("user.home") + "\\" + APP_NAME + "\\" + APP_DATA_FOLDER_NAME;

  public static String getMacAddress() {
    return macAddress;
  }

  public static void setSessionDeviceInfo(SessionDeviceInfo deviceInfo) {
    sessionDeviceInfo = deviceInfo;
  }

  public static SessionDeviceInfo getSessionDeviceInfo() {
    return sessionDeviceInfo;
  }

  public static void clearSessionDeviceInfo() {
    sessionDeviceInfo = null;
  }

  public static void setSessionUserInfo(SessionUserInfo userInfo) {
    sessionUserInfo = userInfo;
    token = sessionUserInfo.getToken();
  }

  public static SessionUserInfo getSessionUserInfo() {
    if (ConstantConfig.FAKE) {
      SessionUserInfo sessionUserInfo = new SessionUserInfo();
      UserDTO userDTO = new UserDTO();
      userDTO.setUserid("89989");
      userDTO.setUsername("chi");
      userDTO.setRole(UserDTO.UserRole.MANAGER.name());
      sessionUserInfo.setInfo(userDTO);
      return sessionUserInfo;
    }
    if (sessionUserInfo == null) {
      AppController.getInstance().startLogin();
    }
    return sessionUserInfo;
  }

  public static void clearSessionUserInfo() {
    sessionUserInfo = null;
  }

  public static String getToken() {
    return token;
  }

  public static void setToken(String token) {
    StaticVarUtils.token = token;
  }

  public static String getAppDataPath() {
    return APP_DATA_PATH;
  }

  public static void setAppDataPath(String appDataPath) {
    APP_DATA_PATH = appDataPath;
  }
}
