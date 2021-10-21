package com.putterfly.simpk.app.item;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j(topic = "")
public class TopMenu extends MenuBar {
    private Menu fileMenu;
    private Menu startMenu;
    private FileChooser fileChooser;
    private Stage stage;
    private TextArea textArea;
    private Label label;

    public TopMenu(Stage stage, TextArea textArea,Label label){
        super();
        fileMenu = new Menu("File");
        startMenu = new Menu("Start");
        this.getMenus().add(fileMenu);
        this.getMenus().add(startMenu);
        this.label = label;
        fileChooser = new FileChooser();
        this.stage = stage;
        this.textArea = textArea;
        MenuItem openFile = new MenuItem("Open");
        openFile.setOnAction((ActionEvent e)->{openFile();});
        MenuItem saveFile = new MenuItem("Save");
        saveFile.setOnAction((ActionEvent e)->{saveFile();});
        fileMenu.getItems().add(openFile);
        fileMenu.getItems().add(saveFile);
//        log.info("initialized complete");
    }

    private void saveFile(){
        String s = label.getText();
        fileChooser.setTitle("Save to");
        fileChooser.getExtensionFilters().removeAll();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        File output = fileChooser.showSaveDialog(stage);
        PrintStream o = System.out;
        if(output==null) return;
        try {
            PrintStream os = new PrintStream(output);
            System.setOut(os);
            System.out.println(s);
            System.setOut(o);
        } catch (IOException e) {
            e.printStackTrace();
            System.setOut(o);
            log.info("Something wrong when saving txt file");
        }
    }
    private void openFile(){
        fileChooser.setTitle("Open TXT file");
        fileChooser.getExtensionFilters().removeAll();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        File inputFile = fileChooser.showOpenDialog(stage);
        if(inputFile==null) return;
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(inputFile);
            BufferedReader br = new BufferedReader(fr);
            String s = null;
            while((s=br.readLine())!=null){
                sb.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Something wrong with the file reading");
        }
        textArea.setText(sb.toString());
    }
}
