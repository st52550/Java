package gui;

import data.Fakulta;
import data.ViewPrihlaseni;
import dialogs.Alerts;
import dialogs.DialogPrihlaseni;
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

/**
 *
 * @author st52550
 */
public class KartaPrihlaseni {

    private TableView<ViewPrihlaseni> tableV = new TableView();
    private TableColumn<ViewPrihlaseni, Integer> id = new TableColumn<>("ID");
    private TableColumn<ViewPrihlaseni, Integer> idVyuc = new TableColumn<>("ID vyučující");
    private TableColumn<ViewPrihlaseni, String> username = new TableColumn<>("Username");
    private TableColumn<ViewPrihlaseni, String> prijmeni = new TableColumn<>("Příjmení");
    private TableColumn<ViewPrihlaseni, String> jmeno = new TableColumn<>("Jméno");
    private TableColumn<ViewPrihlaseni, Integer> opravneniUziv = new TableColumn<>("Oprávnění");

    private ComboBox<Fakulta> vyberF = new ComboBox<>();
    private ObservableList<Fakulta> listFakult;
    private DialogPrihlaseni dialogPrihlaseni;
    private Alerts alerts = new Alerts();

    public VBox getKartaPrihlaseni(Controller c, int opravneni) throws SQLException {
//Tvorba GUI         
        VBox vyucujici = new VBox(10);
        vyucujici.setId("layout");
        Label l1 = new Label("Evidence loginů");
        l1.setPadding(new Insets(10, 0, 0, 10));
        l1.setStyle("-fx-font-size: 20px");
        HBox vyberFakultu = new HBox(10);
        vyberFakultu.setPadding(new Insets(10, 0, 0, 10));
        vyberFakultu.setAlignment(Pos.CENTER_LEFT);
        Label nazevF = new Label("Fakulta: ");

        updateData(c);

        vyberFakultu.getChildren().addAll(nazevF, vyberF);

        vyberF.setOnAction(e -> {
            updateTable(c, vyberF.getSelectionModel().getSelectedItem());
        });

//Konstrukce tabulky účtů
        id.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            id.prefWidthProperty().setValue(50);
        idVyuc.setCellValueFactory(new PropertyValueFactory<>("id_vyucujici"));
            idVyuc.prefWidthProperty().setValue(125);
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
            username.prefWidthProperty().setValue(250);
        prijmeni.setCellValueFactory(new PropertyValueFactory<>("prijmeni"));
            prijmeni.prefWidthProperty().setValue(150);
        jmeno.setCellValueFactory(new PropertyValueFactory<>("jmeno"));
            jmeno.prefWidthProperty().setValue(150);
        opravneniUziv.setCellValueFactory(new PropertyValueFactory<>("opravneni"));
            opravneniUziv.prefWidthProperty().setValue(200);

        tableV.getColumns().addAll(id, idVyuc, username, jmeno, prijmeni, opravneniUziv);
        tableV.setPrefHeight(750);
        updateTable(c, vyberF.getSelectionModel().getSelectedItem());

        Label l2 = new Label("Přidat, editovat či odebrat login");
        l2.setPadding(new Insets(10, 0, 0, 10));
        l2.setStyle("-fx-font-size: 20px");

//Tlačítko pro přidání, editaci či smazání účtu
        HBox buttons1 = new HBox(10);
        buttons1.setPadding(new Insets(10, 0, 10, 10));

        Button pridej = new Button("Přidej", new ImageView("/img/add.png"));
        pridej.setOnAction(e -> {
            dialogPrihlaseni = new DialogPrihlaseni();
            dialogPrihlaseni.add(c, vyberF.getSelectionModel().getSelectedItem());
            updateTable(c, vyberF.getSelectionModel().getSelectedItem());

        });

        Button edituj = new Button("Edit login", new ImageView("/img/edit.png"));
        edituj.setOnAction(e -> {
            ViewPrihlaseni vybranyVyucujici = tableV.getSelectionModel().getSelectedItem();
            if (vybranyVyucujici != null) {
                dialogPrihlaseni = new DialogPrihlaseni();
                dialogPrihlaseni.editLogin(c, vybranyVyucujici, vyberF.getSelectionModel().getSelectedItem());
                updateTable(c, vyberF.getSelectionModel().getSelectedItem());
            } else {
                alerts.alertInfo("Nelze editovat", "Nejdříve vyberte vyučujícího, kterého chcete editovat.");
            }

        });

        Button edituj1 = new Button("Edit heslo", new ImageView("/img/edit.png"));
        edituj1.setOnAction(e -> {
            ViewPrihlaseni vybranyVyucujici = tableV.getSelectionModel().getSelectedItem();
            if (vybranyVyucujici != null) {
                dialogPrihlaseni = new DialogPrihlaseni();
                dialogPrihlaseni.editHeslo(c, vybranyVyucujici, vyberF.getSelectionModel().getSelectedItem());
                updateTable(c, vyberF.getSelectionModel().getSelectedItem());
            } else {
                alerts.alertInfo("Nelze editovat", "Nejdříve vyberte vyučujícího, kterého chcete editovat.");
            }

        });

        Button smazat = new Button("Smaž", new ImageView("/img/delete.png"));
        smazat.setOnAction(e -> {
            try {
                if (tableV.getSelectionModel().getSelectedItem() != null) {
                    int id = tableV.getSelectionModel().getSelectedItem().getId_user();

                    c.deleteLogin(id);
                    updateTable(c, vyberF.getSelectionModel().getSelectedItem());
                } else {
                    alerts.alertInfo("Nelze smazat", "Nejdříve vyberte vyučujícího, kterého chcete smazat.");
                }
            } catch (Exception ex) {
                alerts.alertError("Chyba", "Nepodařilo se smazat login.");
            }
        });

//Přiřazení viditelnosti objektů podle práva uživatele          
         switch (opravneni) {
            case 0:
                 buttons1.getChildren().addAll(pridej, edituj, edituj1, smazat);
                 vyucujici.getChildren().addAll(l1, vyberFakultu, tableV, l2, buttons1);
                break;
            case 1:
                vyucujici.getChildren().addAll(l1, vyberFakultu, tableV,  l2, buttons1);
                break;
            default:
                vyucujici.getChildren().addAll(l1, vyberFakultu, tableV);
                break;
        }
        

        return vyucujici;
    }

//Aktualizace dat v tabulce     
    private void updateTable(Controller c, Fakulta f) {
        ObservableList<ViewPrihlaseni> listVyucujicich;
        try {
            listVyucujicich = FXCollections.observableArrayList(c.getViewPrihlaseni(f));
            tableV.setItems(listVyucujicich);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do tabulky.");
        }
    }

//Aktualizace fakult 
    public void updateData(Controller c) {
        try {
            listFakult = FXCollections.observableArrayList(c.getFakulty());
            vyberF.setItems(listFakult);
            vyberF.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při načítání dat do seznamů.");
        }
    }

}
