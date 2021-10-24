package com.putterfly.simpk.app;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Main")
public class Main {
    public static void main(String[] args) {
        log.info("Application successfully started");
        Application.launch(App.class);
    }
}
