package com.putterfly.simpk.app.item;


import com.putterfly.simpk.drawer.KSDrawer;
import com.putterfly.simpk.imp.FirstOrderLogic;
import com.putterfly.simpk.imp.Imp;
import com.putterfly.simpk.imp.Statement;
import com.putterfly.simpk.ks.KripkeTransfer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;

@Slf4j(topic = "")
public class TopMenu extends MenuBar {
    private Menu fileMenu;
    private Menu startMenu;
    private FileChooser fileChooser;
    private Stage stage;
    private TextArea textArea;
    private TextArea label;
    private Canvas canvas;

    public TopMenu(Stage stage, TextArea textArea,TextArea label,Canvas canvas){
        super();
        fileMenu = new Menu("File");
        startMenu = new Menu("Start");
        this.getMenus().add(fileMenu);
        this.getMenus().add(startMenu);
        this.label = label;
        fileChooser = new FileChooser();
        this.stage = stage;
        this.textArea = textArea;
        this.canvas = canvas;
        MenuItem openFile = new MenuItem("Open");
        openFile.setOnAction((ActionEvent e)->{openFile();});
        MenuItem saveFile = new MenuItem("SaveText");
        saveFile.setOnAction((ActionEvent e)->{saveFile();});
        MenuItem startTransfer = new MenuItem("BeginTransfer");
        startTransfer.setOnAction((ActionEvent e)->{transfer();});
        MenuItem saveGraph = new MenuItem("SaveGraph");
        saveGraph.setOnAction((ActionEvent e)->{saveImage();});
        fileMenu.getItems().add(openFile);
        fileMenu.getItems().add(saveFile);
        fileMenu.getItems().add(saveGraph);
        startMenu.getItems().add(startTransfer);
//        log.info("initialized complete");
    }

    private void transfer(){
        List<List<Statement>> sts = Imp.parseImp(textArea.getText());
        String labelled = Imp.makeLabel(sts);
        Pair<String,List<List<FirstOrderLogic>>> firstOrder = Imp.makeFirstOrderLogic(sts);
        StringBuilder sb = new StringBuilder();
        sb.append("The labelled program is:").append('\n').append('\n').append(labelled)
                .append('\n').append('\n').append("The first order logic is:").append('\n').append('\n')
                .append(firstOrder.getKey()).append("\n\nThe Kripke structure is:\n");
        Pair<String,Pair<List<String>,List<Pair<String,String>>>> pair =KripkeTransfer.beginTransfer(firstOrder.getValue());
        String ks = pair.getKey();
        sb.append(ks);
        label.setText(sb.toString());
        Pair<List<String>,List<Pair<String,String>>> lnr = pair.getValue();
        KSDrawer.draw(canvas,lnr.getKey(),lnr.getValue());
    }
    private void saveImage(){
        String s = label.getText();
        fileChooser.setTitle("Save to");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG File","*.png"));
        File output = fileChooser.showSaveDialog(stage);
        if(output==null) return;
        try {
            WritableImage image = canvas.snapshot(new SnapshotParameters(),null);
            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",output);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("Something wrong when saving png file");
        }
    }
    private void saveFile(){
        String s = label.getText();
        fileChooser.setTitle("Save to");
        fileChooser.getExtensionFilters().clear();
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
        fileChooser.getExtensionFilters().clear();
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
