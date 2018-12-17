package br.com.museuid.model.data;


import br.com.museuid.dto.ProductWithImage;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

public class ProductImporting extends ProductWithImage {
  public ProductImporting(String id, String productName, String description, Integer price, Integer inStock, String imageid, String type) {
    super(id, productName, description, price, inStock, imageid, type);
    configView();
  }

  private void configView(){
    numberToImport = 0;
    tfNumberToImport = new TextField();
    tfNumberToImport.setMaxSize(50, 30);
    tfNumberToImport.setText("0");
    tfNumberToImport.textProperty().addListener((observable, oldValue, newValue) -> {
      int newNumber;
      if (StringUtils.isEmpty(newValue)){
        newValue = "0";
      }
      try {
        newNumber = Integer.valueOf(newValue);
        if (onContentChange!= null)
          onContentChange.onNumberChange(null, newNumber);
      } catch (NumberFormatException e) {
//        Messenger.erro("Yêu cầu là số!");
        tfNumberToImport.setText(oldValue);
      }

    });
  }
  public ProductImporting(String productName, String productid, int numberToImport) {
    super(productName, productid);
    configView();
    this.numberToImport = numberToImport;
    tfNumberToImport.setText(numberToImport+"");
  }

  private int numberToImport;
  private TextField tfNumberToImport;
  private OnContentChange onContentChange;
  private int number;
  public OnContentChange getOnContentChange() {
    return onContentChange;
  }

  public void setOnContentChange(OnContentChange onContentChange) {
    this.onContentChange = onContentChange;
  }

  public int getNumberToImport() {
    return numberToImport;
  }

  public void setNumberToImport(int numberToImport) {
    this.numberToImport = numberToImport;
  }

  public TextField getTfNumberToImport() {
    return tfNumberToImport;
  }

  public void setTfNumberToImport(TextField tfNumberToImport) {
    this.tfNumberToImport = tfNumberToImport;
  }

  public interface OnContentChange {
    void onNumberChange(Integer oldNumber, Integer newNumber);
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
