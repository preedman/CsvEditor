/*
 * Copyright 2021 paul.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reedmanit.csveditor.app;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author paul
 */
public class Main extends Application {

    private static Scene scene;

    private static Stage theStage;

   
    
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {

        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/com/reedmanit/csveditor/view/file.png")));

        stage.setTitle("My CVS Editor");

        theStage = stage;

        scene = new Scene(loadFXML("cvsui"));
        
    //   scene.getStylesheets().add(getClass().getResource("/com/reedmanit/csveditor/style/csv.css").toExternalForm());
        
        stage.setScene(scene);

       

        stage.show();

    }

    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("/com/reedmanit/csveditor/view/" + fxml + ".fxml"));

        return fxmlLoader.load();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
