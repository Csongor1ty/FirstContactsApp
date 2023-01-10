package hu.alkfejl;

import hu.alkfejl.dao.ContactDAO;
import hu.alkfejl.dao.ContactDAOImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;


public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) {
        App.stage = stage;
        App.loadFXML("/fxml/main_window.fxml");

        stage.show();
    }

    public static FXMLLoader loadFXML(String fxml){
        return loadFXML(fxml, stage, o -> {});
    }

    public static <T> FXMLLoader loadFXML(String fxml, Stage stage, Consumer<T> controllerOps){
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = null;
        try {
            Parent root = loader.load();
            controllerOps.accept(loader.getController());
            scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader;

    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }

}