package dialogs;

import data.FormaVyuky;
import data.Kategorie;
import data.Predmet;
import data.PridaniAkce;
import data.Role;
import data.Semestr;
import data.ViewPlan;
import data.Vyucujici;
import data.VyucujiciRole;
import data.Zakonceni;
import data.ZpusobVyuky;
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
public class DialogStudijniPlany {
    private Alerts alerts = new Alerts();
    
    private Label vyucRok1 = new Label("Vyberte vyučujícího");
    private ObservableList<Vyucujici> listV = FXCollections.observableArrayList();
    private ComboBox<Vyucujici> vyucujiciList = new ComboBox<>(listV);
    private Label roleV1 = new Label("Vyberte roli");    
    private ObservableList<Role> listRole = FXCollections.observableArrayList();
    private ComboBox<Role> vyberRoli = new ComboBox<>(listRole);
    
    private Button pridejR = new Button("Přiřadit");
    private Button editujR = new Button("Editovat");
    
    private Label akRokL = new Label("Akademický rok");
    private TextField akRokTxt = new TextField();
    private Label kategorieL = new Label("Kategorie");   
    private ObservableList<Kategorie> listKategorie = FXCollections.observableArrayList();
    private ComboBox<Kategorie> vyberKategorii = new ComboBox<>(listKategorie);                   
    private Label predmetL = new Label("Předmět");   
    private ObservableList<Predmet> listPredmet = FXCollections.observableArrayList();
    private ComboBox<Predmet> vyberPredmet = new ComboBox<>(listPredmet);
    private Label delkaL = new Label("Délka výuky v h");
    private TextField delkaTxt = new TextField();
    private Label odhadL = new Label("Odhad studentů");
    private TextField odhadTxt = new TextField();
    private Label vyukaL = new Label("Způsob výuky");           
    private ObservableList<ZpusobVyuky> listVyuky = FXCollections.observableArrayList();
    private ComboBox<ZpusobVyuky> vyberVyuku = new ComboBox<>(listVyuky);           
    private Label zakonceniL = new Label("Zakončení");           
    private ObservableList<Zakonceni> listZakonceni = FXCollections.observableArrayList();
    private ComboBox<Zakonceni> vyberZakonceni = new ComboBox<>(listZakonceni); 
    private Label formaL = new Label("Forma výuky");    
    private ObservableList<FormaVyuky> listFormy = FXCollections.observableArrayList();
    private ComboBox<FormaVyuky> vyberFormu = new ComboBox<>(listFormy);            
    private Label semestrL = new Label("Semestr");
    private ObservableList<Semestr> listSemestr = FXCollections.observableArrayList();
    private ComboBox<Semestr> vyberSemestr = new ComboBox<>();           
            
    private Button pridejP = new Button("Přidej");
    private Button editujP = new Button("Edituj");           
    
//Dialog pro přidání předmětu do studijního plánu
    public void addPredmet(Controller c, String nazevOboru){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat předmět");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(450);
            window.setHeight(550);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridPredmet(grid);
            GridPane.setConstraints(pridejP, 1, 13);
            GridPane.setHalignment(pridejP, HPos.RIGHT);
            grid.getChildren().add(pridejP);
        root.getChildren().add(grid); 
        
        getPredmetLists(c, nazevOboru);       

//Tlačítko pro přidání předmětu do studijního plánu         
        pridejP.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularPlan(akRokTxt.getText(), Integer.parseInt(delkaTxt.getText()), Integer.parseInt(odhadTxt.getText()));

                if(vyberKategorii.getSelectionModel().getSelectedItem() != null ||
                   vyberPredmet.getSelectionModel().getSelectedItem() != null ||
                   vyberVyuku.getSelectionModel().getSelectedItem() != null ||
                   vyberZakonceni.getSelectionModel().getSelectedItem() != null ||
                   vyberFormu.getSelectionModel().getSelectedItem() != null ||
                   vyberSemestr.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.vlozPredmetPlan(akRokTxt.getText(), vyberKategorii.getSelectionModel().getSelectedItem().getId(), 
                                vyberPredmet.getSelectionModel().getSelectedItem().getId(), delkaTxt.getText(), 
                                odhadTxt.getText(), vyberVyuku.getSelectionModel().getSelectedItem().getId(),
                                vyberZakonceni.getSelectionModel().getSelectedItem().getId(), vyberFormu.getSelectionModel().getSelectedItem().getId(),
                                vyberSemestr.getSelectionModel().getSelectedItem().getId());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    } 
                } else {
                    alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány");
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
        
        clearDataPredmet();
    }

//Dialog pro editaci předmětu do studijního plánu    
    public void editPredmet(Controller c, String nazevOboru, String rok, ViewPlan predmet){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat předmět");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(550);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridPredmet(grid);
            GridPane.setConstraints(editujP, 1, 13);
            GridPane.setHalignment(editujP, HPos.RIGHT);
            grid.getChildren().add(editujP);
        root.getChildren().add(grid); 
        
        getPredmetLists(c, nazevOboru);
        
        akRokTxt.setText(rok);
        vyberKategorii.getSelectionModel().select(predmet.getIdKategore()-1);
        
        Predmet p = null;
        for (Predmet list: listPredmet){
            if (list.getNazev().equals(predmet.getNazevP())) {
                p = list;
            }
        }

//Vyplňení formulářů daty        
        vyberPredmet.getSelectionModel().select(p);
        delkaTxt.setText(String.valueOf(predmet.getDelkaP()));
        odhadTxt.setText(String.valueOf(predmet.getOdhadS()));
        vyberVyuku.getSelectionModel().select(predmet.getIdZpusob()-1);
        vyberZakonceni.getSelectionModel().select(predmet.getIdZakonceni()-1);
        vyberFormu.getSelectionModel().select(predmet.getIdForma()-1);
        vyberSemestr.getSelectionModel().select(predmet.getIdSemestr()-1);

//Tlačítko pro editaci předmětu do studijního plánu        
        editujP.setOnAction(e -> {
            PridaniAkce validniData;
            try {
                validniData = c.formularPlan(akRokTxt.getText(), Integer.parseInt(delkaTxt.getText()), Integer.parseInt(odhadTxt.getText()));

                if(vyberKategorii.getSelectionModel().getSelectedItem() != null ||
                   vyberPredmet.getSelectionModel().getSelectedItem() != null ||
                   vyberVyuku.getSelectionModel().getSelectedItem() != null ||
                   vyberZakonceni.getSelectionModel().getSelectedItem() != null ||
                   vyberFormu.getSelectionModel().getSelectedItem() != null ||
                   vyberSemestr.getSelectionModel().getSelectedItem() != null){
                    if (validniData != null) {
                        if (validniData.getMuzePridat().equals("true")) {
                            c.editPredmetPlan(predmet.getId(), akRokTxt.getText(), vyberKategorii.getSelectionModel().getSelectedItem().getId(), 
                                vyberPredmet.getSelectionModel().getSelectedItem().getId(), delkaTxt.getText(), 
                                odhadTxt.getText(), vyberVyuku.getSelectionModel().getSelectedItem().getId(),
                                vyberZakonceni.getSelectionModel().getSelectedItem().getId(), vyberFormu.getSelectionModel().getSelectedItem().getId(),
                                vyberSemestr.getSelectionModel().getSelectedItem().getId());
                            window.close();
                        } else {
                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                        }
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány");
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
        
        clearDataPredmet();
    }    

//Aktualizace sezanmů z databáze     
    private void getPredmetLists(Controller c, String nazevOboru){
        try {
            listKategorie = FXCollections.observableArrayList(c.getKategorie());
            vyberKategorii.setItems(listKategorie);
            listPredmet = FXCollections.observableArrayList(c.getPredmetObor(nazevOboru));
            vyberPredmet.setItems(listPredmet);
            listVyuky = FXCollections.observableArrayList(c.getZpusobVyuky());
            vyberVyuku.setItems(listVyuky);
            listZakonceni = FXCollections.observableArrayList(c.getZakonceni());
            vyberZakonceni.setItems(listZakonceni);
            listFormy = FXCollections.observableArrayList(c.getFormaVyuky());
            vyberFormu.setItems(listFormy);
            listSemestr = FXCollections.observableArrayList(c.getSemestr());
            vyberSemestr.setItems(listSemestr);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Při načítaní seznamů nastala chyba.");
        }
    }

//Tvorba GUI     
    private void setGridPredmet(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(akRokL, 0, 2);
        GridPane.setConstraints(akRokTxt, 1, 2);
        GridPane.setConstraints(kategorieL, 0, 3);
        GridPane.setConstraints(vyberKategorii, 1, 3);       
        GridPane.setConstraints(predmetL, 0, 4);
        GridPane.setConstraints(vyberPredmet, 1, 4);
        GridPane.setConstraints(delkaL, 0, 5);
        GridPane.setConstraints(delkaTxt, 1, 5);
        GridPane.setConstraints(odhadL, 0, 6);
        GridPane.setConstraints(odhadTxt, 1, 6);
        GridPane.setConstraints(vyukaL, 0, 7);
        GridPane.setConstraints(vyberVyuku, 1, 7);
        GridPane.setConstraints(zakonceniL, 0, 8);
        GridPane.setConstraints(vyberZakonceni, 1, 8);
        GridPane.setConstraints(formaL, 0, 9);
        GridPane.setConstraints(vyberFormu, 1, 9);
        GridPane.setConstraints(semestrL, 0, 10);
        GridPane.setConstraints(vyberSemestr, 1, 10); 
        
        grid.getChildren().clear();
        grid.getChildren().addAll(akRokL, akRokTxt, kategorieL, vyberKategorii,
                predmetL, vyberPredmet, delkaL, delkaTxt, odhadL, odhadTxt, 
                vyukaL, vyberVyuku, zakonceniL, vyberZakonceni,
                formaL, vyberFormu, semestrL, vyberSemestr);
    }

//Vyčištění formulářů    
    private void clearDataPredmet(){
        akRokTxt.clear();
        delkaTxt.clear();
        odhadTxt.clear();
    }
}
