package de.uwe;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class SettingsStage {

    private final Stage stage = new Stage();

    @Inject
    public SettingsView settingsView;


    @PostConstruct
    public void construct(){
        stage.initModality(Modality.NONE);

        Scene scene = new Scene((Parent) settingsView.getNode(), 300, 400);
        scene.getStylesheets().addAll(MainView.darkStyle);

        stage.setScene(scene);
        stage.setTitle("SettingsStage");
        stage.setMinWidth(300);
        stage.setMinHeight(400);
        stage.centerOnScreen();
    }

    public void owner(@Observes Stage owner){
        stage.initOwner(owner);
    }

    public void show(){
        stage.show();
    }
}
