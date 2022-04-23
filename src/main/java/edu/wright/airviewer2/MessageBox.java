package edu.wright.airviewer2;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MessageBox {
    /**
     * This method shows a simple message box with a title and a message using
     * the Text control
     * This message box is a modal hence all user interactions with the main stage
     * are restricted
     * 
     * @param message
     * @param title
     */
    public static void show(String message, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Text txt = new Text();
        txt.setText(message);
        txt.setWrappingWidth(400);
        txt.setTextAlignment(TextAlignment.CENTER);

        VBox pane = new VBox(20);
        Insets padding = new Insets(20);
        pane.setPadding(padding);
        pane.getChildren().addAll(txt);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

    }
}