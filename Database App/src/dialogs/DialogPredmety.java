
package dialogs;

import data.PridaniAkce;
import data.StudijniObor;
import data.ViewPredmet;
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
public class DialogPredmety {
    private Alerts alerts = new Alerts();
    
    private Label zkrP = new Label("Zkratka:");
    private TextField zkrPTxt = new TextField();
    private Label nazP = new Label("Název:");
    private TextField nazPTxt = new TextField();    
    private Label krP = new Label("Počet kreditů:");
    private TextField krPTxt = new TextField();
    private Label pocPr = new Label("Počet přednášek:");    
    private TextField pocPrTxt = new TextField();
    private Label pocCv = new Label("Počet cvičení:");    
    private TextField pocCvTxt = new TextField();
    private Label pocSem = new Label("Počet seminářů:");    
    private TextField pocSemTxt = new TextField();
    private Label rocP = new Label("Doporučený ročník:");    
    private TextField rocPTxt = new TextField();
    private Label oborP = new Label("Studijní obor");   
    private ObservableList<String> listObor = FXCollections.observableArrayList();
    private ComboBox<String> vyberObor = new ComboBox<>(listObor);        
    
    private Button pridej = new Button("Přidej předmět");
    private Button edituj = new Button("Edituj předmět");
    
//Dialog pro přidání předmětu    
    public void add(Controller c, String fakulta){
//Tvorba GUI        
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat předmět");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(450);
            window.setHeight(500);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGrid(grid);
            GridPane.setConstraints(pridej, 1, 12);
            GridPane.setHalignment(pridej, HPos.RIGHT);
            grid.getChildren().add(pridej);
        root.getChildren().add(grid); 
        
        getOborList(c, fakulta);

//Tlačítko pro přidání předmětu        
        pridej.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularPredmet(zkrPTxt.getText(), nazPTxt.getText(), Integer.parseInt(krPTxt.getText()), Integer.parseInt(pocPrTxt.getText()),
                        Integer.parseInt(pocCvTxt.getText()), Integer.parseInt(pocSemTxt.getText()), Integer.parseInt(rocPTxt.getText()));

                if(vyberObor.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozPredmet(zkrPTxt.getText(), nazPTxt.getText(), krPTxt.getText(), pocPrTxt.getText(), pocCvTxt.getText(),
                                            pocSemTxt.getText(), rocPTxt.getText(), vyberObor.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také obor");
                }     
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při vkládání katedry do databáze nastala chyba.");
            } catch (NumberFormatException ex) {
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

//Dialog pro editaci předmětu    
    public void edit(Controller c, ViewPredmet predmet, String fakulta){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat předmět");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(500);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGrid(grid);
            GridPane.setConstraints(edituj, 1, 12);
            GridPane.setHalignment(edituj, HPos.RIGHT);
            grid.getChildren().add(edituj);
        root.getChildren().add(grid); 
        
        getOborList(c, fakulta);

//Vyplňení formulářů daty        
        zkrPTxt.setText(predmet.getZkratkaP());
        nazPTxt.setText(predmet.getNazevP());       
        krPTxt.setText(String.valueOf(predmet.getKredity()));
        pocPrTxt.setText(String.valueOf(predmet.getPocetPr()));
        pocCvTxt.setText(String.valueOf(predmet.getPocetCv()));
        pocSemTxt.setText(String.valueOf(predmet.getPocetSem()));
        rocPTxt.setText(String.valueOf(predmet.getRocnik()));
        vyberObor.getSelectionModel().select(predmet.getObor());
 
//Tlačítko pro editaci předmětu        
        edituj.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularPredmet(zkrPTxt.getText(), nazPTxt.getText(), Integer.parseInt(krPTxt.getText()), Integer.parseInt(pocPrTxt.getText()),
                        Integer.parseInt(pocCvTxt.getText()), Integer.parseInt(pocSemTxt.getText()), Integer.parseInt(rocPTxt.getText()));

                if(vyberObor.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.editPredmet(predmet.getId(), zkrPTxt.getText(), nazPTxt.getText(), krPTxt.getText(), pocPrTxt.getText(), pocCvTxt.getText(),
                                    pocSemTxt.getText(), rocPTxt.getText(), vyberObor.getSelectionModel().getSelectedItem());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také obor");
                }       
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při editaci předmětu nastala chyba.");
            } catch (NumberFormatException ex) {
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

//Aktualizace sezanmu studijních oborů z databáze    
    private void getOborList(Controller c, String fakulta){
         try {
            listObor.clear();
            for(StudijniObor obor:c.getObory(fakulta)){listObor.add(obor.getNazev());}
            vyberObor.setPromptText("Vyberte obor");
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Nepodařil se zobrazit seznam oborů.");
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
        GridPane.setConstraints(zkrP, 0, 2);
        GridPane.setConstraints(zkrPTxt, 1, 2);
        GridPane.setConstraints(nazP, 0, 3);
        GridPane.setConstraints(nazPTxt, 1, 3);       
        GridPane.setConstraints(krP, 0, 4);
        GridPane.setConstraints(krPTxt, 1, 4);
        GridPane.setConstraints(pocPr, 0, 5);
        GridPane.setConstraints(pocPrTxt, 1, 5);
        GridPane.setConstraints(pocCv, 0, 6);
        GridPane.setConstraints(pocCvTxt, 1, 6);
        GridPane.setConstraints(pocSem, 0, 7);
        GridPane.setConstraints(pocSemTxt, 1, 7);
        GridPane.setConstraints(rocP, 0, 8);
        GridPane.setConstraints(rocPTxt, 1, 8);
        GridPane.setConstraints(oborP, 0, 9);
        GridPane.setConstraints(vyberObor, 1, 9);
        
        grid.getChildren().clear();
        grid.getChildren().addAll(zkrP, zkrPTxt, nazP, nazPTxt, krP, krPTxt, pocPr, pocPrTxt, 
            pocCv, pocCvTxt, pocSem, pocSemTxt, rocP, rocPTxt, oborP, vyberObor);
    }

//Vyčištění formulářů     
    private void clearData(){
        zkrPTxt.clear();
        nazPTxt.clear();
        krPTxt.clear();
        pocPrTxt.clear();
        pocCvTxt.clear();
        pocSemTxt.clear();
        rocPTxt.clear();
    }
}
