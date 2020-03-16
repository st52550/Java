package dialogs;

import data.Katedra;
import data.Mistnost;
import data.PridaniAkce;
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
public class DialogMistnosti {
    private Alerts alerts = new Alerts();
    
    private Label oznaceniL = new Label("Označení:");
    private TextField oznaceniTxt = new TextField();
    private Label kapacitaL = new Label("Kapacita:");
    private TextField kapacitaTxt = new TextField(); 
    
    private Label katedra = new Label("Katedra");   
    private ObservableList<String> katedryList = FXCollections.observableArrayList();
    private ComboBox<String> vyberKatedru = new ComboBox<>(katedryList); 
    
    private Button pridej = new Button("Přidej předmět");
    private Button edituj = new Button("Edituj předmět");
 
//Dialog pro přidání místnosti    
    public void add(Controller c, String fakulta){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat místnost");
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
        
        getKatedraList(c, fakulta);
 
//Tlačítko pro přidání místnosti        
        pridej.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularMistnost(oznaceniTxt.getText(), Integer.parseInt(kapacitaTxt.getText()));
                
                if(vyberKatedru.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozMistnost(oznaceniTxt.getText(), Integer.parseInt(kapacitaTxt.getText()), vyberKatedru.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také katedru");
                }    
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání katedry do databáze nastala chyba.");
            } catch (NumberFormatException ex){
                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearData();
    }
 
//Dialog pro editaci místnosti    
    public void edit(Controller c, Mistnost mistnost, String fakulta){
//Tvorba GUI        
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat místnost");
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
        
        getKatedraList(c, fakulta);

//Vyplňení formulářů daty        
        oznaceniTxt.setText(mistnost.getOznaceni());
        kapacitaTxt.setText(String.valueOf(mistnost.getKapacita()));       
        vyberKatedru.getSelectionModel().select(mistnost.getKatedra());
 
//Tlačítko pro editaci místnosti        
        edituj.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularMistnost(oznaceniTxt.getText(), Integer.parseInt(kapacitaTxt.getText()));
                
                if(vyberKatedru.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.editMistnost(mistnost.getId(), oznaceniTxt.getText(), Integer.parseInt(kapacitaTxt.getText()), 
                                vyberKatedru.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také katedru");
                }     
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci předmětu nastala chyba.");
            } catch (NumberFormatException ex){
                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
            }
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearData();
    }

//Aktualizace sezanmu kateder z databáze    
    private void getKatedraList(Controller c, String fakulta){
         try {
            for(Katedra kat:c.getKatedry(fakulta)){katedryList.add(kat.getNazevK());}
            vyberKatedru.setPromptText("Vyberte katedru");
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Nepodařil se zobrazit seznam kateder.");
        } 
    }

//Tvorba GUI    
    private void setGrid(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(oznaceniL, 0, 2);
        GridPane.setConstraints(oznaceniTxt, 1, 2);
        GridPane.setConstraints(kapacitaL, 0, 3);
        GridPane.setConstraints(kapacitaTxt, 1, 3);       
        GridPane.setConstraints(katedra, 0, 4);
        GridPane.setConstraints(vyberKatedru, 1, 4);
        
        grid.getChildren().clear();
        grid.getChildren().addAll(oznaceniL, oznaceniTxt, kapacitaL, kapacitaTxt, 
                katedra, vyberKatedru);
    }
 
//Vyčištění formulářů    
    private void clearData(){
        oznaceniTxt.clear();
        kapacitaTxt.clear();
    }
}
