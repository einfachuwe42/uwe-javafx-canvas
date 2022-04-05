package de.uwe;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SettingsView {

    private final TextField testTextField = new TextField();
    private final Button testButton = new Button("test");

    private final VBox layout = new VBox(testTextField, testButton);



    @PostConstruct
    public void construct(){


    }

    public Node getNode(){
        return layout;
    }
}
