package com.systemwerx.common.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXOptionPane {

    static Insets insets = new Insets(20, 15, 20, 15);

    public enum Response {
        NO, YES, CANCEL
    };

    private static Response buttonSelected = Response.CANCEL;

    private static ImageView icon = new ImageView();

    static class Dialog extends Stage {
        public Dialog(String title, Stage owner, Scene scene, String iconFile) {
            setTitle(title);
            initStyle(StageStyle.UTILITY);
            initModality(Modality.APPLICATION_MODAL);
            initOwner(owner);
            setResizable(false);
            setScene(scene);
            icon.setImage(new Image(getClass().getResourceAsStream(iconFile)));
        }

        public void showDialog() {
            sizeToScene();
            centerOnScreen();
            showAndWait();
        }
    }

    static class Message extends Text {
        public Message(String msg) {
            super(msg);
            //setWrappingWidth(250);
        }
    }

    public static Response showConfirmDialog(Stage owner, String message, String title) {
        VBox vb = new VBox();
        Scene scene = new Scene(vb);
        final Dialog dial = new Dialog(title, owner, scene, "/images/confirm.png");
        vb.setPadding(insets);
        vb.setSpacing(10);
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
                buttonSelected = Response.YES;
            }
        });
        Button noButton = new Button("No");
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
                buttonSelected = Response.NO;
            }
        });
        BorderPane bp = new BorderPane();
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.getChildren().addAll(yesButton, noButton);
        bp.setCenter(buttons);
        HBox msg = new HBox();
        msg.setSpacing(5);
        msg.getChildren().addAll(icon, new Message(message));
        vb.getChildren().addAll(msg, bp);
        dial.showDialog();
        return buttonSelected;
    }

    public static void showMessageDialog(Stage owner, String message, String title) {
        VBox vb = new VBox();
        Scene scene = new Scene(vb);
        final Dialog dial = new Dialog(title, owner, scene, "/images/information.png");
        vb.setPadding(insets);
        vb.setSpacing(10);
        Button okButton = new Button("OK");
        okButton.setAlignment(Pos.CENTER);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
            }
        });
        Node messageNode = new Message("    "+message+"    ");
        BorderPane bp = new BorderPane();
        bp.setCenter(okButton);
        HBox msg = new HBox();
        msg.setSpacing(5);
        msg.getChildren().addAll(icon, messageNode);
        vb.getChildren().addAll(msg, bp);
        dial.showDialog();
    }

    public static void showErrorDialog(Stage owner, String message, String title) {
        VBox vb = new VBox();
        Scene scene = new Scene(vb);
        final Dialog dial = new Dialog(title, owner, scene, "/images/error.png");
        vb.setPadding(insets);
        vb.setSpacing(10);
        Button okButton = new Button("OK");
        okButton.setAlignment(Pos.CENTER);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
            }
        });
        Node messageNode = new Message("    "+message+"    ");
        BorderPane bp = new BorderPane();
        bp.setCenter(okButton);
        HBox msg = new HBox();
        msg.setSpacing(5);
        msg.getChildren().addAll(icon, messageNode);
        vb.getChildren().addAll(msg, bp);
        dial.showDialog();
    }

}