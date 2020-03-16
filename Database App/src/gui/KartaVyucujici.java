package gui;

import data.Fakulta;
import data.ViewVyucujici;
import dialogs.Alerts;
import dialogs.DialogVyucujici;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author st52550
 */
public class KartaVyucujici {
    private TableView<ViewVyucujici> tableV = new TableView();
    private TableColumn<ViewVyucujici, String> titP = new TableColumn<>("Titul před");
    private TableColumn<ViewVyucujici, String> prijmeni = new TableColumn<>("Příjmení");
    private TableColumn<ViewVyucujici, String> jmeno = new TableColumn<>("Jméno");
    private TableColumn<ViewVyucujici, String> titZa = new TableColumn<>("Titul za");
    private TableColumn<ViewVyucujici, String> katedra = new TableColumn<>("Katedra");
    private TableColumn<ViewVyucujici, String> fakulta = new TableColumn<>("Fakulta");
    private TableColumn<ViewVyucujici, Integer> telefon = new TableColumn<>("Telefon");
    private TableColumn<ViewVyucujici, Integer> mobil = new TableColumn<>("Mobil");
    private TableColumn<ViewVyucujici, String> email = new TableColumn<>("E-mail");
    
    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private DialogVyucujici dialogVyucujici;
    private Alerts alerts = new Alerts();
    private ImageView imageView = new ImageView();
        
    public VBox getKartaVyucujici(Controller c, int opravneni) throws SQLException{
//Tvroba GUI
        VBox vyucujici = new VBox(10);
            vyucujici.setId("layout");
            vyucujici.setPadding(new Insets(10, 10, 10, 10));
        
        Label l1 = new Label("Evidence vyučujících");
            l1.setStyle("-fx-font-size: 20px");
            
        HBox vyberFakultu = new HBox(10);
            vyberFakultu.setAlignment(Pos.CENTER_LEFT);
            Label nazevF = new Label("Fakulta: ");
            
            updateData(c);
        vyberFakultu.getChildren().addAll(nazevF, vyberF);
        
        vyberF.setOnAction(e -> {
            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        });
            
//Konstrukce tabulky vyučujících
        titP.setCellValueFactory(new PropertyValueFactory<>("titulPred"));
            titP.prefWidthProperty().setValue(90);
        prijmeni.setCellValueFactory(new PropertyValueFactory<>("prijmeni"));
            prijmeni.prefWidthProperty().setValue(150);
        jmeno.setCellValueFactory(new PropertyValueFactory<>("jmeno"));
            jmeno.prefWidthProperty().setValue(150);
        titZa.setCellValueFactory(new PropertyValueFactory<>("titulZa"));
            titZa.prefWidthProperty().setValue(90);
        katedra.setCellValueFactory(new PropertyValueFactory<>("katedra"));
            katedra.prefWidthProperty().setValue(300);
        fakulta.setCellValueFactory(new PropertyValueFactory<>("fakulta"));
            fakulta.prefWidthProperty().setValue(350);
        telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
            telefon.prefWidthProperty().setValue(125);
        mobil.setCellValueFactory(new PropertyValueFactory<>("mobil"));
            mobil.prefWidthProperty().setValue(125);
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
            email.prefWidthProperty().setValue(300);

        tableV.getColumns().addAll(titP, prijmeni, jmeno, titZa, telefon, 
                mobil, email, katedra, fakulta); 
        tableV.setPrefHeight(500);
        updateTable(c, vyberF.getSelectionModel().getSelectedItem());         
            
        Label l2 = new Label("Přidat, editovat či odebrat vyučijícího");
            l2.setStyle("-fx-font-size: 20px");               
            
//Tlačítko pro přidání, editaci či smazání vyučujícího
//Přidej a Edituj je realizováno pomocí samostatných dialogů
        HBox buttons1 = new HBox(10);
        
            Button pridej = new Button("Přidej", new ImageView("/img/add.png")); 
            pridej.setOnAction(e -> {
                dialogVyucujici = new DialogVyucujici();
                dialogVyucujici.add(c);
                updateTable(c, vyberF.getSelectionModel().getSelectedItem());
            });
                
            Button edituj = new Button("Edituj", new ImageView("/img/edit.png"));
            edituj.setOnAction(e -> {
                ViewVyucujici vybranyVyucujici = tableV.getSelectionModel().getSelectedItem();
                if (vybranyVyucujici != null) {
                    dialogVyucujici = new DialogVyucujici();
                    dialogVyucujici.edit(c, vybranyVyucujici);
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                } else {
                    alerts.alertInfo("Nelze editovat", "Nejdříve vyberte vyučujícího, kterého chcete editovat.");
                }               
            });
                
            Button smazat = new Button("Smaž", new ImageView("/img/delete.png"));
            smazat.setOnAction(e -> {
                try {   
                    if (tableV.getSelectionModel().getSelectedItem() != null) {                                            
                        int idVyucujici = tableV.getSelectionModel().getSelectedItem().getIdVyucujici();
                        String emailVyucujici = tableV.getSelectionModel().getSelectedItem().getEmail();
                        c.deleteVyucujici(idVyucujici, emailVyucujici);
                        updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                    } else {
                        alerts.alertInfo("Nelze smazat", "Nejdříve vyberte vyučujícího, kterého chcete smazat.");
                    }    
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Nepodařilo se smazat vyučujícího. Ujistěte se, "
                            + "jestli nemá naplánované rozvrhové akce.");
                }
            }); 
            
/////////////////////Lukas
//Import vyučujících z CSV souboru do databáze
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

//Doplneni vytvoreni objektu, pomoci if else ohlidat pocet prvku v poli(pokud ucitel nema email je prvku 7 jinak jich je 10)
//Vlozeni do db(volani metody k tomu urcene)
                        if (poleProcesu.length == 10) {
                            c.vlozVyucujiciho(poleProcesu[1], poleProcesu[2], poleProcesu[3], poleProcesu[4],
                                    poleProcesu[5], poleProcesu[7], poleProcesu[8], poleProcesu[9]);

                        } else if (poleProcesu.length == 7) {
                            c.vlozVyucujiciho(poleProcesu[1], poleProcesu[2], poleProcesu[3], poleProcesu[4],
                                    poleProcesu[5], "", "", "");
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
//////////////////Lukas
//Zobrazení a načtení obrázku vyučujícího z databáze
//Tlačítko pro přizazení nové fotky vyučujícímu
        HBox profilImg = new HBox(10);
        profilImg.setPadding(new Insets(10, 0, 10, 10));

        imageView.setFitHeight(115);
        imageView.setFitWidth(115);
        profilImg.getChildren().add(imageView);

        tableV.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event1) -> {
            switch (event1.getButton()) {
                case PRIMARY:
                    try {
                        if (tableV.getSelectionModel().getSelectedItem() != null) {
                            int id = tableV.getSelectionModel().getSelectedItem().getIdVyucujici();
                        nactiObr(c, id);
                        }
                        
                    } catch (NumberFormatException ex) {
                        alerts.alertError("Chyba", "Spatny cislo.");
                    } catch (SQLException | IOException ex) {
                        alerts.alertError("Chyba", "Chyba pri nacitani obrazku");
                    }
                    break;
            }
        });

        Button ulozB = new Button("Přiřadit fotku", new ImageView("/img/photo.png"));
        ulozB.setOnAction(e -> {
            try {
                if (tableV.getSelectionModel().getSelectedItem() != null){
                int id = tableV.getSelectionModel().getSelectedItem().getIdVyucujici();
                ulozObr(c, id);
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte vyučujícho, kterému chcete přiřadit fotku.");
                }
            } catch (NumberFormatException ex) {
                alerts.alertError("Chyba", "Spatny cislo.");
            }
        });

//Přiřazení viditelnosti objektů podle práva uživatele
        switch (opravneni) {
            case 0:
                buttons1.getChildren().addAll(pridej, edituj, smazat, importCSV, ulozB);
                vyucujici.getChildren().addAll(l1, vyberFakultu, tableV, profilImg, l2, buttons1);
                break;
            case 1:
                vyucujici.getChildren().addAll(l1, vyberFakultu, tableV, profilImg);
                break;
            default:
                vyucujici.getChildren().addAll(l1, vyberFakultu, tableV, profilImg);
                break;
        } 
    
        return vyucujici;    
    }

//Aktualizace dat v tabulce
    private void updateTable(Controller c, Fakulta f){
        ObservableList<ViewVyucujici> listVyucujicich;
            try {
                listVyucujicich = FXCollections.observableArrayList(c.getViewVyucujici(f));
                tableV.setItems(listVyucujicich);
                imageView.setImage(null);
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
            imageView.setImage(null);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do seznamů.");
        }
    }
    
/////////////Lukas
//Načtení obrázku z databáze
    private void nactiObr(Controller c, int id) throws SQLException, IOException {
        InputStream is = c.nactiObrazek(id);
        if (is == null) {
            imageView.setImage(null);
        } else {
            Image image = new Image(is);
            imageView.setImage(image);
        }
    }

//Výběr obrázku z počítače a přiřazení vyučujícímu
    private void ulozObr(Controller c, int id) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Vyber obr");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Obrazky", "*.jpg", "*.png"));
        File file = chooser.showOpenDialog(null);
        if (file == null) {
            alerts.alertError("Chyba", "Vybrán špatný nebo žádný obrázek");
        } else {
            String name = file.getName();
            String nazev = name.substring(0, name.lastIndexOf("."));
            String pripona = name.substring(name.lastIndexOf("."), name.length());

            InputStream is;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                c.ulozObrazek(is, nazev, pripona, id);
            } catch (FileNotFoundException ex) {
                alerts.alertError("Chyba", "Chyba obrazek nenalezen");
            } catch (Exception ex) {
                alerts.alertError("Chyba", "Chyba ukladani obrazku.");
            }

        }
    }
}
