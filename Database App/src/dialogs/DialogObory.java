package dialogs;

import data.Katedra;
import data.PridaniAkce;
import data.ViewObor;
import gui.Controller;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author st52550
 */
public class DialogObory {
    private Alerts alerts = new Alerts();
    
    private Label ozn = new Label("Označení:");
    private TextField oznTxt = new TextField();
    private Label nazO = new Label("Název:");
    private TextField nazOTxt = new TextField();
    
    private Label katedraO = new Label("Katedra");   
    private ObservableList<String> katedryList = FXCollections.observableArrayList();
    private ComboBox<String> vyberKatedru = new ComboBox<>(katedryList);        
    
    private Button pridej = new Button("Přidej obor");
    private Button edituj = new Button("Edituj obor");

//Dialog pro přidání studijního oboru    
    public void add(Controller c, String fakulta){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat obor");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(450);
            window.setHeight(300);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGrid(grid);
            GridPane.setConstraints(pridej, 1, 7);
            GridPane.setHalignment(pridej, HPos.RIGHT);
            grid.getChildren().add(pridej);
        root.getChildren().add(grid); 
        
        getKatedryList(c, fakulta);

//Tlačítko pro přidání studijního oboru        
        pridej.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularObor(oznTxt.getText(), nazOTxt.getText());
                
                if(vyberKatedru.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozObor(oznTxt.getText(), nazOTxt.getText(), vyberKatedru.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    }  
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také katedru");
                }     
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání oboru do databáze nastala chyba.");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearData();
    }

//Dialog pro editaci studijního obrou    
    public void edit(Controller c, ViewObor obor, String fakulta){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat obor");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(300);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGrid(grid);
            GridPane.setConstraints(edituj, 1, 7);
            GridPane.setHalignment(edituj, HPos.RIGHT);
            grid.getChildren().add(edituj);
        root.getChildren().add(grid); 
        
        getKatedryList(c, fakulta);

//Vyplňení formulářů daty         
        oznTxt.setText(obor.getOznaceni());
        nazOTxt.setText(obor.getNazev());       
        vyberKatedru.getSelectionModel().select(obor.getKatedra());

//Tlačítko pro editaci studijního oboru        
        edituj.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularObor(oznTxt.getText(), nazOTxt.getText());
                
                if(vyberKatedru.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.editObor(obor.getId(), oznTxt.getText(), nazOTxt.getText(), vyberKatedru.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    }  
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také katedru");
                }      
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci oboru nastala chyba.");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearData();
    }

//Tvorba GUI     
    private void setGrid(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(ozn, 0, 2);
        GridPane.setConstraints(oznTxt, 1, 2);
        GridPane.setConstraints(nazO, 0, 3);
        GridPane.setConstraints(nazOTxt, 1, 3);       
        GridPane.setConstraints(katedraO, 0, 4);
        GridPane.setConstraints(vyberKatedru, 1, 4);
        
        grid.getChildren().clear();
        grid.getChildren().addAll(ozn, oznTxt, nazO, nazOTxt, katedraO, vyberKatedru);
    }

//Aktualizace sezanmu kateder z databáze     
    private void getKatedryList(Controller c, String fakulta){
        try {
            katedryList.clear();
            for(Katedra kat:c.getKatedry(fakulta)){katedryList.add(kat.getNazevK());}
            vyberKatedru.setPromptText("Vyberte katedru"); 
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Nepodařil se zobrazit seznam kateder.");
        }
    }

//Vyčištění formulářů    
    private void clearData(){
        oznTxt.clear();
        nazOTxt.clear();
    }
}
