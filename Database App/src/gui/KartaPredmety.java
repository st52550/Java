package gui;

import data.Fakulta;
import data.ViewPredmet;
import dialogs.Alerts;
import dialogs.DialogPredmety;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 *
 * @author st52550
 */
public class KartaPredmety {
    private TableView<ViewPredmet> tableP = new TableView();
    private TableColumn<ViewPredmet, String> zkratkaP = new TableColumn<>("Zkrakta");
    private TableColumn<ViewPredmet, String> nazevP = new TableColumn<>("Název předmětu");
    private TableColumn<ViewPredmet, Integer> kredity = new TableColumn<>("Počet kreditů");
    private TableColumn<ViewPredmet, Integer> rocnik = new TableColumn<>("Ročník výuky");
    private TableColumn<ViewPredmet, Integer> pocetPr = new TableColumn<>("Počet přednášek");
    private TableColumn<ViewPredmet, Integer> pocetCv = new TableColumn<>("Počet cvičení");
    private TableColumn<ViewPredmet, Integer> pocetSem = new TableColumn<>("Počet seminářů");
    private TableColumn<ViewPredmet, String> oborP = new TableColumn<>("Studijní obor");
    private TableColumn<ViewPredmet, String> katedraP = new TableColumn<>("Katedra");
    private TableColumn<ViewPredmet, String> fakultaP = new TableColumn<>("Fakulta");
    
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private Alerts alerts = new Alerts();
    private DialogPredmety dialogPredmety;
    
    public VBox getKartaPredmety(Controller c, int opravneni) throws SQLException{
//Tvorba GUI        
        VBox predmety = new VBox(10);
            predmety.setId("layout");
            predmety.setPadding(new Insets(10, 10, 10, 10));
            
        Label l5 = new Label("Seznam předmětů");
            l5.setStyle("-fx-font-size: 20px");
            
        HBox vyberFakultu = new HBox(10);
            vyberFakultu.setAlignment(Pos.CENTER_LEFT);
            Label nazevFakulty = new Label("Fakulta: ");
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
        vyberFakultu.getChildren().addAll(nazevFakulty, vyberF);
        
        vyberF.getSelectionModel().selectFirst();
            
        vyberF.setOnAction(e -> {
            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        });        

//Konstrukce tabulky předmětů        
        zkratkaP.setCellValueFactory(new PropertyValueFactory<>("zkratkaP"));
            zkratkaP.prefWidthProperty().setValue(100);
        nazevP.setCellValueFactory(new PropertyValueFactory<>("nazevP"));
            nazevP.prefWidthProperty().setValue(325);
        kredity.setCellValueFactory(new PropertyValueFactory<>("kredity"));
            kredity.prefWidthProperty().setValue(125);
        rocnik.setCellValueFactory(new PropertyValueFactory<>("rocnik"));
            rocnik.prefWidthProperty().setValue(125);
        pocetPr.setCellValueFactory(new PropertyValueFactory<>("pocetPr"));
            pocetPr.prefWidthProperty().setValue(150);
        pocetCv.setCellValueFactory(new PropertyValueFactory<>("pocetCv"));
            pocetCv.prefWidthProperty().setValue(125);
        pocetSem.setCellValueFactory(new PropertyValueFactory<>("pocetSem"));
            pocetSem.prefWidthProperty().setValue(150);
        oborP.setCellValueFactory(new PropertyValueFactory<>("obor"));
            oborP.prefWidthProperty().setValue(225);
        katedraP.setCellValueFactory(new PropertyValueFactory<>("katedra"));
            katedraP.prefWidthProperty().setValue(300);
        fakultaP.setCellValueFactory(new PropertyValueFactory<>("fakulta"));
            fakultaP.prefWidthProperty().setValue(350);

        tableP.getColumns().addAll(zkratkaP, nazevP, kredity, rocnik, oborP, katedraP, fakultaP);
        updateTable(c, vyberF.getSelectionModel().getSelectedItem());

        Label l6 = new Label("Přidat, editovat či odebrat předmět");
            l6.setStyle("-fx-font-size: 20px"); 
            
//Tlačítko pro přidání, editaci či smazání předmětu
        HBox buttons3 = new HBox(10); 
        
            Button pridejPred = new Button("Přidej", new ImageView("/img/add.png")); 
                pridejPred.setOnAction(e -> {  
                    dialogPredmety = new DialogPredmety();
                    dialogPredmety.add(c, vyberF.getSelectionModel().getSelectedItem().getNazevF());
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                });
                
            Button editujPred = new Button("Edituj", new ImageView("/img/edit.png"));
                editujPred.setOnAction(e -> {
                    ViewPredmet vybranyPredmet = tableP.getSelectionModel().getSelectedItem();
                    if (vybranyPredmet != null) {
                        dialogPredmety = new DialogPredmety();
                        dialogPredmety.edit(c, vybranyPredmet, vyberF.getSelectionModel().getSelectedItem().getNazevF());
                        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                    } else {
                        alerts.alertInfo("Nelze editovat", "Nejdříve vyberte předmět, který chcete editovat.");
                    }
                });
                
            Button smazatPred = new Button("Smaž", new ImageView("/img/delete.png"));
                smazatPred.setOnAction(e -> {
                    try {
                        if (tableP.getSelectionModel().getSelectedItem() != null) {
                            int idPredmet = tableP.getSelectionModel().getSelectedItem().getId();
                            c.deletePredmet(idPredmet);
                            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                        } else {
                            alerts.alertInfo("Nelze smazat", "Nejdříve vyberte předmět, který chcete smazat.");
                        }  
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Nepodařilo se smazat předmět. Ujistěte se, jestli "
                                + "není součástí některého studijního plánu.");
                    }
                });
        
///////////////////////Lukas
//Import předmětů z CSV souboru do databáze
            Button importCSV = new Button("Import", new ImageView("/img/import.png"));
            importCSV.setOnAction(e -> {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Vyber soubor");
                chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
                File file = chooser.showOpenDialog(null);
                if (file == null) {
                    alerts.alertError("Chyba", "Vybrán špatný nebo žádný soubor");
                } else {

                BufferedReader bufReader = null;
                try {

                    bufReader = new BufferedReader(new FileReader(file));
                    String radek;
                    String[] poleProcesu;
                    radek = bufReader.readLine(); //precteni hlavicky

                    while ((radek = bufReader.readLine()) != null) {

                        poleProcesu = radek.split(";");

//Vlozeni do db(volani metody k tomu urcene)
                        if (poleProcesu.length == 9) {
                            c.vlozPredmet(poleProcesu[1], poleProcesu[0], poleProcesu[5], poleProcesu[2], poleProcesu[3], poleProcesu[4],
                                    poleProcesu[6], poleProcesu[8]);
                        }

                        //vypis radku - pak smazat
                        /*System.out.print(poleProcesu.length + ": ");
                        for (int i = 0; i < poleProcesu.length; i++) {
                            System.out.print(poleProcesu[i] + ", ");
                        }
                        System.out.println("");*/
                    }
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());

                } catch (IOException|SQLException ex) {
                    alerts.alertError("Chyba", "Nepodařilo se načíst soubor");
                } finally {
                    if (bufReader != null) {
                        try {
                            bufReader.close();
                        } catch (IOException ex) {
                            alerts.alertError("Chyba", "Nepodařilo se zavřít soubor");
                        }
                    }
                }
                }
            });
            
/////////////////////Lukas
//Přiřazení viditelnosti objektů podle práva uživatele
        switch (opravneni) {
            case 0:
                buttons3.getChildren().addAll(pridejPred, editujPred, smazatPred, importCSV);
                predmety.getChildren().addAll(l5, vyberFakultu, tableP, l6, buttons3);
                break;
            case 1:
                predmety.getChildren().addAll(l5, vyberFakultu, tableP);
                break;
            default:
                predmety.getChildren().addAll(l5, vyberFakultu, tableP);
                break;
        }                
    return predmety;
    } 

//Aktualizace dat v tabulce    
    private void updateTable(Controller c, Fakulta f){
        try {
            if (f != null){
                ObservableList<ViewPredmet> listPredmetu = 
                    FXCollections.observableArrayList(c.getViewPredmety(f));
                tableP.setItems(listPredmetu);
            }
        } catch (SQLException ex) {
             alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
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
