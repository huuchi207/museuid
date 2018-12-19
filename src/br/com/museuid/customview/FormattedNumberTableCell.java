package br.com.museuid.customview;

import br.com.museuid.util.NumberUtils;
import javafx.scene.control.TableCell;

public class FormattedNumberTableCell extends TableCell<Object, Integer> {
  @Override
  protected void updateItem(Integer item, boolean empty) {
    super.updateItem(item, empty);
    if (!empty && item != null) {
      setText(NumberUtils.convertVNDMoneyToDecimaFormatString(item));
    } else {
      setText(null);
    }
  }

}
