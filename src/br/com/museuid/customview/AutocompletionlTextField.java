package br.com.museuid.customview;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AutocompletionlTextField<O> extends TextField {
    //Local variables
    //entries to autocomplete
    public static int MAX_ENTRY = 5;
    private final SortedSet<O> entries;
    //popup GUI
    private ContextMenu entriesPopup;
    private O selectedItem;

    public AutocompletionlTextField() {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();

        setListner();
    }  


    /**
     * "Suggestion" specific listners
     */
    private void setListner() {     
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = getText();
            selectedItem = null;
            //always hide suggestion if nothing has been entered (only "spacebars" are dissalowed in TextFieldWithLengthLimit)
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //filter all possible suggestions depends on "Text", case insensitive
                List<O> filteredEntries = entries.stream()
                        .filter(e -> e.toString().toLowerCase().contains(enteredText.toLowerCase()))
                        .collect(Collectors.toList());
                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText);
                    if (!entriesPopup.isShowing()) { //optional
                        entriesPopup.show(AutocompletionlTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    }
                //no suggestions -> hide
                } else {
                    entriesPopup.hide();
                }
            }
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
            if (selectedItem == null){
                setText("");
            }
        });
    }             


    /**
    * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
    * 
    * @param searchResult The set of matching strings.
    */
    private void populatePopup(List<O> searchResult, String searchReauest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count

        int count = Math.min(searchResult.size(), MAX_ENTRY);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final O current = searchResult.get(i);
          final String result = current.toString();
          //label with graphic (text flow) to highlight founded subtext in suggestions
          Label entryLabel = new Label();
          entryLabel.setGraphic(buildTextFlow(result, searchReauest));
//          entryLabel.setStyle("-fx-text-fill: #808080;");
          entryLabel.setPrefHeight(12);  //don't sure why it's changed with "graphic"
          CustomMenuItem item = new CustomMenuItem(entryLabel, true);
          menuItems.add(item);

          //if any suggestion is select set it into text and close popup
          item.setOnAction(actionEvent -> {
              setText(result);
              selectedItem = current;
              positionCaret(result.length());
              entriesPopup.hide();
          });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }


    /**
    * Get the existing set of autocomplete entries.
    * 
    * @return The existing autocomplete entries.
    */
    public SortedSet<O> getEntries() { return entries; }

    public O getSelectedItem(){
        return selectedItem;
    }

    public void setMaxEntry(int entry){
        MAX_ENTRY = entry;
    }

    public static TextFlow buildTextFlow(String text, String filter) {
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.ORANGE);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        return new TextFlow(textBefore, textFilter, textAfter);
    }
}