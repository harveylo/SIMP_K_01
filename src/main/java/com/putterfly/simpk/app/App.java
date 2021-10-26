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
import javafx.scene.control.ScrollPane;
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
        stage.setTitle("IMP to Kripke tool @Putterfly");
//        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        BorderPane root = new BorderPane();
        TextArea textArea = new TextArea();
        textArea.setText("Please input imp program here \nor open a text file that contains a simp program.");
        textArea.setPrefSize(400,800);
        TextArea label = new TextArea("The Text result will displayed here");
        label.setPrefSize(400,800);
        label.editableProperty().set(false);
        root.setLeft(textArea);
        root.setCenter(label);
        ScrollPane sp = new ScrollPane();
        sp.setPrefSize(600,800);
        Canvas canvas = new Canvas(1200,1200);
        sp.setContent(canvas);
        sp.pannableProperty().set(true);
        root.setRight(sp);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.fillText("The graph will be displayed here",40,40);
        root.setTop(new TopMenu(stage,textArea,label,canvas));
        Scene scene = new Scene(root,1400,800);
        stage.setScene(scene);
        stage.show();
    }
}
