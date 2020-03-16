package gui;

import data.AkRok;
import data.Fakulta;
import data.Predmet;
import data.ViewObor;
import data.ViewPlan;
import dialogs.Alerts;
import dialogs.DialogStudijniPlany;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author st52550
 */
public class KartaStudijniPlany {
    private TableView<ViewPlan> tableR = new TableView();
    private TableColumn<ViewPlan, String> kategorie = new TableColumn<>("Kategorie");
    private TableColumn<ViewPlan, String> zkratkaPredmetu = new TableColumn<>("Zkratka");
    private TableColumn<ViewPlan, String> nazevPredmetu = new TableColumn<>("Název");      
    private TableColumn<ViewPlan, Integer> odhadS = new TableColumn<>("Odhad studentů");
    private TableColumn<ViewPlan, Integer> delkaP = new TableColumn<>("Délka výuky v h"); 
    private TableColumn<ViewPlan, Integer> rocnikV = new TableColumn<>("Ročník"); 
    private TableColumn<ViewPlan, String> zpusobVyuky = new TableColumn<>("Způsob výuky");
    private TableColumn<ViewPlan, String> zakonceniR = new TableColumn<>("Zakončení");
    private TableColumn<ViewPlan, String> formaVyuky = new TableColumn<>("Forma výuky");
    private TableColumn<ViewPlan, String> sem = new TableColumn<>("Semestr");
    
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private ComboBox<AkRok> vyberR = new ComboBox<>();
    private ComboBox<ViewObor> vyberObor = new ComboBox<>();  
    private ObservableList<ViewObor> listObor;
    private ComboBox<Predmet> vyberPredmet = new ComboBox<>();
    private ObservableList<Predmet> listPredmet;
    
    private DialogStudijniPlany dialogStudijniPlany;
    private Alerts alerts = new Alerts();
        
    public VBox getKartaStudijniPlany(Controller c, int opravneni) throws SQLException{
//Tvorba GUI           
        VBox plany = new VBox(10);
            plany.setId("layout");
            plany.setPadding(new Insets(10, 10, 10, 10));
      
        Label l7 = new Label("Předměty studijního plánu v daném ak. roce");
            l7.setStyle("-fx-font-size: 20px");           
              
        HBox rokH = new HBox(10);
            rokH.setAlignment(Pos.CENTER_LEFT);
            Label rokL = new Label("Vyberte ak. rok");
                rokL.setMinSize(125, Region.USE_PREF_SIZE);
            
            updateListLet(c);
            vyberR.getSelectionModel().selectFirst();

            Label fakultaL = new Label("Vyberte fakultu"); 
                fakultaL.setMinSize(125, Region.USE_PREF_SIZE);
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
            vyberF.getSelectionModel().selectFirst();
            listObor = FXCollections.observableArrayList(c.getViewObory(
                                vyberF.getSelectionModel().getSelectedItem()));
            vyberObor.setItems(listObor);
            
            Label oborL = new Label("Vyberte obor");
                oborL.setMinSize(110, Region.USE_PREF_SIZE);
            Button zobrazPlan = new Button("Zobraz plán");
        rokH.getChildren().addAll(rokL, vyberR, fakultaL, vyberF, oborL, vyberObor, zobrazPlan);
            
        ObservableList<ViewPlan> listPlan = FXCollections.observableArrayList(
                        c.getViewPlan(vyberR.getSelectionModel().getSelectedItem(),
                                vyberObor.getSelectionModel().getSelectedItem()));
                tableR.setItems(listPlan); 

//Konstrukce tabulky studijních plánů                
        kategorie.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
            kategorie.prefWidthProperty().setValue(125);
        zkratkaPredmetu.setCellValueFactory(new PropertyValueFactory<>("zkratkaP"));
            zkratkaPredmetu.prefWidthProperty().setValue(125);
        nazevPredmetu.setCellValueFactory(new PropertyValueFactory<>("nazevP"));
            nazevPredmetu.prefWidthProperty().setValue(250);
        odhadS.setCellValueFactory(new PropertyValueFactory<>("odhadS"));
            odhadS.prefWidthProperty().setValue(175);
        delkaP.setCellValueFactory(new PropertyValueFactory<>("delkaP"));
            delkaP.prefWidthProperty().setValue(150);
        zpusobVyuky.setCellValueFactory(new PropertyValueFactory<>("zpusobV"));
            zpusobVyuky.prefWidthProperty().setValue(150);
        zakonceniR.setCellValueFactory(new PropertyValueFactory<>("zakonceni"));
            zakonceniR.prefWidthProperty().setValue(125);
        formaVyuky.setCellValueFactory(new PropertyValueFactory<>("formaV"));
            formaVyuky.prefWidthProperty().setValue(150);
        sem.setCellValueFactory(new PropertyValueFactory<>("semestr"));
            sem.prefWidthProperty().setValue(125);
        rocnikV.setCellValueFactory(new PropertyValueFactory<>("rocnik"));
            rocnikV.prefWidthProperty().setValue(100);

        tableR.getColumns().addAll(zkratkaPredmetu, nazevPredmetu, kategorie,
                delkaP, rocnikV, sem, zpusobVyuky, zakonceniR, formaVyuky, odhadS);

//Při výběru fakulty se zobrazí seznam studijních oborů        
        vyberF.setOnAction(e -> {
                try {
                    if(vyberF.getSelectionModel().getSelectedItem() != null){
                        listObor = FXCollections.observableArrayList(c.getViewObory(
                                vyberF.getSelectionModel().getSelectedItem()));
                        vyberObor.setItems(listObor);
                    }
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Chyba při načítání oborů.");
                }                    
            });  

//Tlačítko pro zobrazení studijních plánu
        zobrazPlan.setOnAction(e ->{
            try {
                if (vyberR.getSelectionModel().getSelectedItem() != null &&
                        vyberF.getSelectionModel().getSelectedItem() != null &&
                        vyberObor.getSelectionModel().getSelectedItem() != null){
                    listPredmet = FXCollections.observableArrayList(c.getPredmetObor(
                            vyberObor.getSelectionModel().getSelectedItem().getNazev()));
                    vyberPredmet.setItems(listPredmet);
                    updateTable(c);                                     
                } else {
                    alerts.alertInfo("Oznámení", "Všechny parametry musí být vybrány.");
                }
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Chyba při načítání studijního plánu.");
            }
        });     
           
        Label l9 = new Label("Přidat, editovat či odebrat předmět ze studijního plánu");
            l9.setStyle("-fx-font-size: 20px");
    
//Tlačítko pro přidání, editaci či smazání záznamu
        HBox buttons4 = new HBox(10);
        
            Button pridejPredmet = new Button("Přidej", new ImageView("/img/add.png")); 
                pridejPredmet.setOnAction(e -> {  
                    if (vyberObor.getSelectionModel().getSelectedItem() != null) {
                        dialogStudijniPlany = new DialogStudijniPlany();
                        dialogStudijniPlany.addPredmet(c, vyberObor.getSelectionModel().getSelectedItem().getNazev());
                        updateTable(c);
                        updateListLet(c);
                    } else {
                        alerts.alertInfo("Oznámení", "Nejdříve vyberte obor, kterému chcete vytvořit studijní plán.");
                    }
                });
                
            Button editujPredmet = new Button("Edituj", new ImageView("/img/edit.png"));
                editujPredmet.setOnAction(e -> {
                    if (tableR.getSelectionModel().getSelectedItem() != null) {
                        ViewPlan vybranyPredmet = tableR.getSelectionModel().getSelectedItem();
                        dialogStudijniPlany = new DialogStudijniPlany();
                        dialogStudijniPlany.editPredmet(c, vyberObor.getSelectionModel().getSelectedItem().getNazev(), 
                                vyberR.getSelectionModel().getSelectedItem().getRok(), vybranyPredmet);
                        updateTable(c);
                        updateListLet(c);
                    } else {
                        alerts.alertInfo("Nelze editovat", "Nejdříve vyberte předmět, který chcete editovat.");
                    }
                });
                
            Button smazatPredmet = new Button("Smaž", new ImageView("/img/delete.png"));
                smazatPredmet.setOnAction(e -> {
                    try {
                        if (tableR.getSelectionModel().getSelectedItem() != null) {
                            int idPredmetPlan = tableR.getSelectionModel().getSelectedItem().getId();
                            c.deletePredmetPlan(idPredmetPlan);
                            updateTable(c);
                            updateListLet(c);
                        } else {
                            alerts.alertInfo("Nelze smazat", "Nejdříve vyberte předmět, který chcete smazat.");
                        }
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Nepodařilo se smazat přemět. Ujistěte se, jestli"
                                + "není součástí některé rozvrhové akce.");
                    }
                });

//////////Lukas
//Přiřazení viditelnosti objektů podle práva uživatele 
         switch (opravneni) {
            case 0:
                 buttons4.getChildren().addAll(pridejPredmet,editujPredmet,smazatPredmet);
                plany.getChildren().addAll(l7, rokH, tableR, l9, buttons4);
                break;
            case 1:
                plany.getChildren().addAll(l7, rokH, tableR);
                break;
            default: 
                plany.getChildren().addAll(l7, rokH, tableR);
                break;
        }
        
    return plany;    
    }

//Aktualizace dat v tabulce    
    private void updateTable(Controller c){
        try {
            ObservableList<ViewPlan> listPlan1 = FXCollections.observableArrayList(
                    c.getViewPlan(vyberR.getSelectionModel().getSelectedItem(),
            vyberObor.getSelectionModel().getSelectedItem()));
            tableR.setItems(listPlan1);
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace akademických let   
    private void updateListLet(Controller c){
        try {
            AkRok rok = null;
            if (vyberR.getSelectionModel().getSelectedItem() != null) {
                rok = vyberR.getSelectionModel().getSelectedItem();
            } 
            
            ObservableList<AkRok> listLet = FXCollections.observableArrayList(c.getRoky());
            vyberR.setItems(listLet);
            vyberR.getSelectionModel().select(rok);
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání seznamu akademických roků.");
        }
    } 
    
//Aktualizace fakult     
    public void updateData(Controller c){
        try {
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
            vyberF.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do seznamů.");
        }
    }
}
