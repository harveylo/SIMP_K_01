package com.putterfly.simpk.app;

import com.putterfly.simpk.app.item.TopMenu;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Application")
public class App extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("SIMP to Kripke tool @Putterfly");
//        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        BorderPane root = new BorderPane();
        TextArea textArea = new TextArea();
        textArea.setText("Please input simp program here or open a text file that contains a simp program.");
        Label label = new Label("The result will displayed here");
        root.setLeft(textArea);
        root.setCenter(label);
        Canvas canvas = new Canvas();
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.fillText("Test",95,95);
        graphicsContext.strokeRect(90,90,90,90);
        root.setTop(new TopMenu(stage,textArea,label));
        Scene scene = new Scene(root,1200,800);
        stage.setScene(scene);
        stage.show();
    }
}
