/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoorganizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class PhotoOrganizer extends Application {

    private File currentLocation;
    private File locationTree;
    private Stage stage;
    private VBox rightFrame;
    private double size;
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, IOException {
        currentLocation = new File (Paths.get("./src/pictures").toAbsolutePath().normalize().toString());
        locationTree = new File (Paths.get("./src").toAbsolutePath().normalize().toString());
        stage = primaryStage;
        restart(stage);
    }
    
    public void resolution (Stage primaryStage, BorderPane borderPane) {
        
        //The resolution of the App (width, height)
        final Scene scene = new Scene(borderPane);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle(currentLocation.getName());
        
        primaryStage.setMinHeight(689);
        primaryStage.setMinWidth(903);

        primaryStage.show();
        
    }
    
    public HBox header() throws FileNotFoundException {
        //Button with icons
        Image image;
        IconGraphic icon;
        
        //HBox lays out its children in horizontal row
        HBox hBoxNav = new HBox();
        
        hBoxNav.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(hBoxNav, Priority.ALWAYS);
        
         //Undo Button
        image = new Image(new FileInputStream("./src/photoorganizer/undo.png"));
        icon = new IconGraphic(image);
        hBoxNav.getChildren().add(icon);

        icon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(currentLocation.getParentFile() != null){
                    currentLocation = locationTree;
                    locationTree = new File(locationTree.getParentFile().getAbsolutePath());
                    System.out.println("Back");
                    try {
                        restart(stage);
                    }
                    catch (IOException ex) {
                        Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
            }

        });
        
        //Redo Button
        image = new Image(new FileInputStream("./src/photoorganizer/redo.png"));
        icon = new IconGraphic(image);
        hBoxNav.getChildren().add(icon);
        
        
        
        //HBox Search Bar
        HBox hBoxSearch = new HBox();
        
        //Search bar
        TextField textField = new TextField ();
        //Search bar's placeholder
        textField.setPromptText("Search...");
        
        hBoxSearch.setPrefWidth(800);
        textField.setPrefWidth(300);
        hBoxSearch.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hBoxSearch, Priority.ALWAYS);
        hBoxSearch.getChildren().addAll(textField);
        
        //Read enter key when pressed (search bar)
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
     
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode().equals(KeyCode.ENTER)) {
                System.out.println("EE");
            }
        }
        });
        
        HBox hBoxTop = new HBox(hBoxNav, hBoxSearch);
        hBoxTop.setPadding(new Insets(10));
        
        return hBoxTop;
    };
    
    public ScrollPane treeView() throws FileNotFoundException {
        
        ScrollPane spane = new ScrollPane();
        //VBox lays out its children in vertical row
        VBox vBoxTree = new VBox();
       
        
        //Tree
        Image imageRoot = new Image(new FileInputStream("./src/photoorganizer/star.png"));
        ImageView imageViewRoot = new ImageView(imageRoot);
        imageViewRoot.setFitHeight(20);
        imageViewRoot.setFitWidth(20);
        
        for( int i = 0; i < locationTree.listFiles().length; i++ ){
            //HBox for folder alignment
            HBox hBoxTree = new HBox();
 
            if(locationTree.listFiles()[i].isDirectory()){
                Image imageFolder = new Image(new FileInputStream("./src/photoorganizer/dir.png"));
                PathFinder imageView = new PathFinder(imageFolder, locationTree.listFiles()[i].getAbsolutePath());
                Text text = new Text(locationTree.listFiles()[i].getName());
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if(event.getButton().equals(MouseButton.PRIMARY)){
                        if(event.getClickCount() == 1){
                            PathFinder path = (PathFinder) event.getSource();
                            System.out.println(path.getDirectoryPath());
                            
                            File newFile = new File(path.getDirectoryPath());
                            
                            locationTree = newFile.getParentFile();
                            currentLocation = newFile;
                            
                            try {
                                restart(stage);
                            } catch (IOException ex) {
                                Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                }
           });
                hBoxTree.setPrefWidth(200);
                
                hBoxTree.getChildren().addAll(imageView, text);
                
                vBoxTree.getChildren().add(hBoxTree);
                vBoxTree.setPrefHeight(600);
                
            }
        };
            
        spane.setContent(vBoxTree);
        return spane;
    }
    
    public void restart(Stage primaryStage) throws FileNotFoundException, IOException {
        //BorderPane lays out children in top, left, right, bottom, and center positions
        BorderPane borderPane = new BorderPane();

        //VBox for folder alignment
        VBox vBoxFolder = new VBox();

        //To put the items inside of hBox at the top side
        borderPane.setTop(header());
        
        //To put the items inside of hBox at the left side
        borderPane.setLeft(treeView());
        
        
        //Scroll Pane
        ScrollPane spane = new ScrollPane();
        
        for( int i = 0; i < currentLocation.listFiles().length; i++ ){
            //HBox for folder alignment
            HBox hBoxFolder = new HBox();
            if(currentLocation.listFiles()[i].isDirectory()){
                Image imageFolder = new Image(new FileInputStream("./src/photoorganizer/dir.png"));
                PathFinder imageView = new PathFinder(imageFolder, currentLocation.listFiles()[i].getAbsolutePath());
                Text text = new Text(currentLocation.listFiles()[i].getName());
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if(event.getButton().equals(MouseButton.PRIMARY)){
                            if(event.getClickCount() == 1){
                                PathFinder path = (PathFinder) event.getSource();
                                System.out.println(path.getDirectoryPath());

                                File newFile = new File(path.getDirectoryPath());

                                locationTree = newFile.getParentFile();
                                currentLocation = newFile;

                                try {
                                    restart(stage);
                                } catch (IOException ex) {
                                    Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                });
                hBoxFolder.getChildren().addAll(imageView, text);
                vBoxFolder.getChildren().add(hBoxFolder);
                
            }
            else if(currentLocation.listFiles()[i].getName().endsWith(".jpg") ||
                    currentLocation.listFiles()[i].getName().endsWith(".png")){
                Image imageFolder = new Image(new FileInputStream(currentLocation.listFiles()[i].getAbsolutePath()));
                PathFinder imageView = new PathFinder(imageFolder, currentLocation.listFiles()[i].getAbsolutePath());
                Text text = new Text(currentLocation.listFiles()[i].getName());
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if(event.getButton().equals(MouseButton.PRIMARY)){
                            if(event.getClickCount() == 1){
//                                System.out.println("TESTP");
                                PathFinder path = (PathFinder) event.getSource();
                                System.out.println(path.getDirectoryPath());

                                rightFrame = new VBox();
                                try {
                                    File fileRight = new File(path.getDirectoryPath());
                                    Text textRight = new Text(fileRight.getName());
                                    
                                    Image imageRight = new Image(new FileInputStream(path.getDirectoryPath()));
                                    ImageView imageViewRight = new ImageView(imageRight);
                                    imageViewRight.setFitHeight(300);
                                    imageViewRight.setFitWidth(300);
                                    
                                    Slider slider = new Slider();
                                    slider.setMin(1);
                                    slider.setMax(2);
                                    slider.setOnMouseReleased(events -> {
                                        size = slider.getValue();
                                        System.out.println("slider: " + slider.getValue());
                                        imageViewRight.setFitHeight(300 * size);
                                        imageViewRight.setFitWidth(300 * size);
                                        try {
                                            restart(stage);
                                        } catch (IOException ex) {
                                            Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                                    
                                    ScrollPane scrollZoom = new ScrollPane();
                                    scrollZoom.setFitToHeight(true);
                                    scrollZoom.setFitToWidth(true);
                                    scrollZoom.setPrefViewportHeight(300);
                                    scrollZoom.setPrefViewportWidth(300);
                                    scrollZoom.setContent(imageViewRight);
                                    
                                    HBox hBoxName = new HBox();
                                    Text name = new Text("name: ");
                                    name.setStyle("-fx-font-weight: bold;");
                                    
                                    HBox hBoxDate = new HBox();
                                    Text date = new Text("last modified: ");
                                    date.setStyle("-fx-font-weight: bold;");
                                    
                                    HBox hBoxType = new HBox();
                                    
                                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                    File fileName = new File(currentLocation.getAbsolutePath());
                                    Text imageLastMod = new Text(df.format(fileName.lastModified()));
                                    
                                    hBoxName.getChildren().addAll(name, textRight);
                                    hBoxName.setAlignment(Pos.CENTER);
                                    hBoxDate.getChildren().addAll(date, imageLastMod);
                                    hBoxDate.setAlignment(Pos.CENTER);
                                    
                                    if (fileRight.getName().endsWith(".jpg")) {
                                        Text type = new Text("type: ");
                                        type.setStyle("-fx-font-weight: bold;");
                                        Text detail = new Text (".jpg");
                                        hBoxType.getChildren().addAll(type, detail);
                                    }
                                    else if (fileRight.getName().endsWith(".png")) {
                                        Text type = new Text("type: ");
                                        type.setStyle("-fx-font-weight: bold;");
                                        Text detail = new Text (".png");
                                        
                                        hBoxType.getChildren().addAll(type, detail);
                                    }
                                    hBoxType.setAlignment(Pos.CENTER);
                                    
                                    rightFrame.getChildren().addAll(scrollZoom, slider, hBoxName, hBoxDate, hBoxType);
                                    
                                    restart(stage);
                                    
                                    
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                        }

                    }
                });
                hBoxFolder.getChildren().addAll(imageView, text);
                vBoxFolder.getChildren().add(hBoxFolder);
                
            }
        }
        spane.setContent(vBoxFolder);
 
        borderPane.setCenter(spane);
        borderPane.setRight(rightFrame);
        
        resolution(primaryStage, borderPane);
    }
    
//    public ScrollPane favourite () throws FileNotFoundException {
//        Image imageFolder = new Image(new FileInputStream("./src/photoorganizer/dir.png"));
//        PathFinder imageView = new PathFinder(imageFolder, currentLocation.listFiles()[i].getAbsolutePath());
//        Text text = new Text(currentLocation.listFiles()[i].getName());
//        imageView.setFitHeight(20);
//        imageView.setFitWidth(20);
//                
//        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//
//                if(event.getButton().equals(MouseButton.PRIMARY)){
//                    if(event.getClickCount() == 1){
//                        PathFinder path = (PathFinder) event.getSource();
//                        System.out.println(path.getDirectoryPath());
//
//                        File newFile = new File(path.getDirectoryPath());
//
//                        locationTree = newFile.getParentFile();
//                        currentLocation = newFile;
//
//                        try {
//                            restart(stage);
//                        } catch (IOException ex) {
//                            Logger.getLogger(PhotoOrganizer.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//
//            }
//        });
//        hBoxFolder.getChildren().addAll(imageView, text);
//        vBoxFolder.getChildren().add(hBoxFolder);
//    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
