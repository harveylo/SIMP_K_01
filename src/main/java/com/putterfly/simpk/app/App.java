package com.putterfly.simpk.app;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Application")
public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("SIMP to Kripke tool @Putterfly");
        stage.initStyle(StageStyle.UNIFIED);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        BorderPane root = new BorderPane();
        Button b1 = new Button();
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                log.info("yes");
            }
        });
        Scene s1 = new Scene(root,500,500);
        root.setCenter(b1);
        stage.setScene(s1);
        stage.show();
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                System.out.println(t1);
            }
        });
    }
}
