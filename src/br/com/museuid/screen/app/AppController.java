package br.com.museuid.screen.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.museuid.Constants;
import br.com.museuid.app.App;
import br.com.museuid.app.Login;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Link;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.StaticVarUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppController {
  private static AppController instance;
  @FXML
  public ToggleButton btEmployeeManagement;
  public VBox boxManagement;
  public ToggleButton btAppManagementSection;
  public ToggleGroup gAppManagementSection;
  public VBox boxOrderSection;
  public ToggleButton btCreateOrder;
  public ToggleButton btMyHandlingOrder;
  public VBox boxStatistic;
  public ToggleButton btOrderSection;
  public ToggleButton btOrderCreated;
  public Label lbUser;
  public ToggleButton btUserInfoSection;
  public VBox boxUserInfo;
  public ToggleButton btUpdateUserInfo;
  public ToggleButton btChangePassword;
  public ToggleButton btOrderInQueue;
  public ToggleButton btProductManagement;
  public ToggleButton btDeviceManagement;
  public ToggleButton btRevenueStatistic;
  public ToggleButton btStockImporting;
  public VBox boxMenu;
  public ToggleButton btProductStatistic;
  public ToggleButton btStockImportingManagement;
  public ToggleButton btStatisticSection;
  public ToggleButton btStockImportingHistory;

  @FXML
  private AnchorPane boxContainer;
  @FXML
  private VBox boxNotas;
  private ToggleButton currentScreen;
  @FXML
  private ProgressIndicator progressIndicator;

  private List<Node> userListBoxMenu;
  private List<Node> userListFeatureView;

  public static AppController getInstance() {
    return instance;
  }

  @FXML
  void logOut(ActionEvent event) {
    showProgressDialog();
    ServiceBuilder.getApiService().logOut().enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        App.getmStage().close();
        new Login().start(new Stage());
      }
    });
  }

  @FXML
  void initialize() {
    instance = this;

//    if (!EnumUtils.isValidEnum(UserDTO.UserRole.class, StaticVarUtils.getSessionUserInfo().getInfo().getRole()) && !ConstantConfig.FAKE) {
//      startLogin();
//    }
    UserDTO.UserRole role;
//    if (ConstantConfig.FAKE) {
//      role = UserDTO.UserRole.ADMIN;
//    } else {
    role = UserDTO.UserRole.valueOf(StaticVarUtils.getSessionUserInfo().getInfo().getRole());
//    }
    switch (role) {
      case ADMIN:
        userListBoxMenu = new ArrayList<Node>(
          Arrays.asList(btOrderSection, boxOrderSection, btAppManagementSection, boxManagement, btStatisticSection, boxStatistic, btUserInfoSection, boxUserInfo));
        userListFeatureView = new ArrayList<Node>(
          Arrays.asList(btCreateOrder, btOrderInQueue, btMyHandlingOrder, btEmployeeManagement, btProductManagement,
            btDeviceManagement, btStockImportingManagement, btRevenueStatistic,
            btProductStatistic,
            btUpdateUserInfo, btChangePassword));
        openOrderInQueueScreen(new ActionEvent(btOrderInQueue, null));
        break;
      case MANAGER:
        userListBoxMenu = new ArrayList<Node>(
          Arrays.asList(btStatisticSection, boxStatistic, btUserInfoSection, boxUserInfo));
        userListFeatureView = new ArrayList<Node>(
          Arrays.asList(btRevenueStatistic, btUpdateUserInfo, btChangePassword));
        openRevenueStatistic(new ActionEvent(btRevenueStatistic, null));
        break;
      case STOCKER:
        userListBoxMenu = new ArrayList<Node>(
          Arrays.asList(btAppManagementSection, boxManagement, btUserInfoSection, boxUserInfo));
        userListFeatureView = new ArrayList<Node>(
          Arrays.asList(btStockImporting, btStockImportingHistory, btUpdateUserInfo, btChangePassword));
        openStockImporting(new ActionEvent(btStockImporting, null));
        break;
      case EMPLOYEE:
        userListBoxMenu = new ArrayList<Node>(
          Arrays.asList(btOrderSection, boxOrderSection, btUserInfoSection, boxUserInfo));
        userListFeatureView = new ArrayList<Node>(
          Arrays.asList(btCreateOrder, btOrderInQueue, btMyHandlingOrder, btUpdateUserInfo, btChangePassword));
        openOrderInQueueScreen(new ActionEvent(btOrderInQueue, null));
        break;
    }
    //remove views
    List<Node> removingParentViews = new ArrayList<>();
    List<Node> removingChildViews = new ArrayList<>();
    for (Node child : boxMenu.getChildren()) {
      if (userListBoxMenu.contains(child)) {
        //check children of this child
        removingChildViews.clear();
        if (child instanceof VBox) {
          for (Node childOfChild : ((VBox) child).getChildren()) {
            if (!userListFeatureView.contains(childOfChild)) {
              removingChildViews.add(childOfChild);
            }
          }
          ((VBox) child).getChildren().removeAll(removingChildViews);
        }
      } else {
        removingParentViews.add(child);
      }
    }
    boxMenu.getChildren().removeAll(removingParentViews);
    lbUser.setText("Xin chào, " + StaticVarUtils.getSessionUserInfo().getInfo().getUsername());
  }

  /**
   * Obter componente para exbição das notas
   */
  public VBox boxNotas() {
    return boxNotas;
  }


  public void openEmployeeManagement(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openCreateOrderScreen(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openMyHandlingOrderScreen(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openOrderCreatedScreen(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openRevenueStatistic(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void setStyleSubMenuThenHideProgressIndicator(ToggleButton currentSelectedSubMenu) {
    for (Node boxMenu : userListBoxMenu) {
      if (boxMenu instanceof VBox) {
        List<Node> children = ((VBox) boxMenu).getChildren();
        if (children != null && !children.isEmpty()) {
          for (Node child : children) {
            if (child instanceof ToggleButton) {
              if (child != currentSelectedSubMenu) {
                ((ToggleButton) child).setSelected(false);
              } else {
                ((ToggleButton) child).setSelected(true);
              }
            }
          }
        }
      }
    }
    hideProgressDialog();
  }

  public void showProgressDialog() {
    progressIndicator.setVisible(true);
  }

  public void hideProgressDialog() {
    progressIndicator.setVisible(false);
  }

  public void openUpdateUserInfoScreen(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openChangePasswordScreen(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void updateUserNameTextView(String userName) {
    lbUser.setText(BundleUtils.getResourceBundle().getString("txt_hello" + ", " + userName));
  }


  public void openOrderInQueueScreen(ActionEvent event) {
    openScreen((ToggleButton) event.getSource());
  }

  public void startLogin() {
    new Login().start(new Stage());
    App.getmStage().close();
  }

  public void openProductManagement(ActionEvent event) {
    openScreen((ToggleButton) event.getSource());
  }

  public void openDeviceManagement(ActionEvent event) {
    openScreen((ToggleButton) event.getSource());
  }

  public void openMyHandingOrderScreen(ActionEvent event) {
    openScreen((ToggleButton) event.getSource());
  }

  public void openStockImporting(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void goSite(ActionEvent actionEvent) {
    //TODO: change link
    Link.address(Constants.WEB_LINK);
  }

  public void openStockImportingManagement(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openProductStatistic(ActionEvent actionEvent) {
    openScreen((ToggleButton) actionEvent.getSource());
  }

  public void openStockImportingHistory(ActionEvent event) {
    openScreen((ToggleButton) event.getSource());
  }

  private void openScreen(ToggleButton bt) {
    setStyleSubMenuThenHideProgressIndicator(bt);
    if (bt == currentScreen) {
      return;
    }
    if (bt == btCreateOrder){
      NavigationUtils.getCreateOrderScreen(boxContainer);
    } else if (bt == btOrderInQueue){
      NavigationUtils.getOrderInQueueScreen(boxContainer);
    } else if (bt == btMyHandlingOrder){
      NavigationUtils.getMyHandlingOrderScreen(boxContainer);
    } else if (bt == btEmployeeManagement){
      NavigationUtils.getEmployeeManagementScreen(boxContainer);
    } else if (bt == btProductManagement){
      NavigationUtils.getProductManagementScreen(boxContainer);
    } else if (bt == btDeviceManagement){
      NavigationUtils.getDeviceManagementScreen(boxContainer);
    } else if (bt == btStockImportingManagement){
      NavigationUtils.getStockImportingManagementControler(boxContainer);
    } else if (bt == btRevenueStatistic){
      NavigationUtils.getStatisticScreen(boxContainer);
    } else if (bt == btProductStatistic){
      NavigationUtils.getProductStatisticController(boxContainer);
    } else if (bt == btUpdateUserInfo){
      NavigationUtils.getUpdateUserInfoScreen(boxContainer);
    } else if (bt == btChangePassword){
      NavigationUtils.getChangePasswordScreen(boxContainer);
    } else if (bt == btStockImporting){
      NavigationUtils.getStockImportingControler(boxContainer);
    } else if (bt == btStockImportingHistory){
      NavigationUtils.getStockImportingHistoryControler(boxContainer);
    } else if (bt == btOrderCreated){
      NavigationUtils.getMyOrderCreatedScreen(boxContainer);
    }
    currentScreen = bt;
  }
}
