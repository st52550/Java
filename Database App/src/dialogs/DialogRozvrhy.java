package dialogs;

import data.Mistnost;
import data.PridaniAkce;
import data.Role;
import data.ViewPlan;
import data.ViewRozvrh;
import data.Vyucujici;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author st52550
 */
public class DialogRozvrhy {
    private Alerts alerts = new Alerts();
    
    private Label zpusob = new Label("Způsob výuky");
    private ObservableList<String> listZ = FXCollections.observableArrayList();
    private ComboBox<String> vyberZpusob = new ComboBox<>(listZ);
    
    private Label vyucujici = new Label("Vyberte vyučujícího");
    private ObservableList<Vyucujici> listV = FXCollections.observableArrayList();
    private ComboBox<Vyucujici> vyberVyucujici = new ComboBox<>(listV);
    
    private Label roleV = new Label("Vyberte roli");    
    private ObservableList<Role> listRole = FXCollections.observableArrayList();
    private ComboBox<Role> vyberRoli = new ComboBox<>(listRole);
    
    private Label den = new Label("Vyberte den");    
    private ObservableList<String> listDen = FXCollections.observableArrayList("pondělí", 
            "úterý", "středa", "čtvrtek", "pátek");
    private ComboBox<String> vyberDen = new ComboBox<>(listDen);
    
    private Label casOd = new Label("Čas od");
    private TextField casOdTxt = new TextField();
    private Label casDo = new Label("Čas do");
    private TextField casDoTxt = new TextField();
    private Label pocetH = new Label("Rozsah hodin: ");
    private Label obsazenost = new Label("Max. obsazenost");
    private TextField obsazenostTxt = new TextField();
    
    private Label mistnost = new Label("Vyberte místnost");    
    private ObservableList<Mistnost> listMistnost = FXCollections.observableArrayList();
    private ComboBox<Mistnost> vyberMistnost = new ComboBox<>(listMistnost);
    
    private Label platnost = new Label("Platnost");    
    private ObservableList<String> listPlatnost = FXCollections.observableArrayList("platná", "neplatná");
    private ComboBox<String> vyberPlatnost = new ComboBox<>(listPlatnost);
    
    private Button pridejR = new Button("Přidej");
    private Button editujR = new Button("Edituj");

//Dialog pro přidání rozvrhové akce   
    public void addRozvrh(Controller c, final String rok, String obor, ObservableList<ViewPlan> listPlan, String email, String fakulta){
//Tvorba GUI        
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat rozvrhovou akci");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(500);
            window.setHeight(600);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridRozvrh(grid);
            GridPane.setConstraints(pridejR, 1, 17);
            GridPane.setHalignment(pridejR, HPos.RIGHT);
            grid.getChildren().add(pridejR);
        root.getChildren().add(grid); 
        
        getRozvrhLists(c, email); 
        
        if(!listPlan.isEmpty()){
            listZ.clear();
            listPlan.forEach((p) -> {
                listZ.add(p.getZpusobV());
            });
            vyberZpusob.setItems(listZ);
        } else {
            alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
        } 

//Při výběru místnosti se zobrazí místnosti splňující zadanou kapacitu akce        
        vyberMistnost.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!obsazenostTxt.getText().equals("")) {
                    try {
                        listMistnost = FXCollections.observableArrayList(c.getMistnosti(fakulta, Integer.parseInt(obsazenostTxt.getText())));
                        vyberMistnost.hide();                       
                        vyberMistnost.setItems(listMistnost);
                        vyberMistnost.show();  
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Chyba při získání seznamu místností.");
                    }catch (NumberFormatException ex) {//////////////////////////////lukas
                        alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve uveďte maximální obsazenost studijní skupiny.");
                }
            }
        });

//Při výběru způsobu výuky se vybere daný předmět ze studijního plánu a nastaví se rozsah hodin        
        vyberZpusob.setOnAction(e -> {
            ViewPlan plan;
            
            for(ViewPlan p : listPlan){
                if (vyberZpusob.getSelectionModel().getSelectedItem() != null) {
                    if (p.getZpusobV().equals(vyberZpusob.getSelectionModel().getSelectedItem())) {
                        plan = p;
                        pocetH.setText("Rozsah hodin: " + String.valueOf(plan.getDelkaP()));
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte způsob výuky.");
                }
            }
        });

//Tlačítko pro přidání rozvrhové akce      
        pridejR.setOnAction(e -> {
            ViewPlan plan = null;
            PridaniAkce volnaAkce;
            
            for(ViewPlan p : listPlan){
                if (vyberZpusob.getSelectionModel().getSelectedItem() != null) {
                    if (p.getZpusobV().equals(vyberZpusob.getSelectionModel().getSelectedItem())) {
                        plan = p;
                        pocetH.setText("Rozsah hodin: " + String.valueOf(plan.getDelkaP()));
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte způsob výuky.");
                }
            }
            
            Vyucujici v;
            if (vyberVyucujici.getSelectionModel().getSelectedItem() != null) {
                v = vyberVyucujici.getSelectionModel().getSelectedItem();
                if (vyberMistnost.getSelectionModel().getSelectedItem() != null) { 
                    if (vyberDen.getSelectionModel().getSelectedItem() != null) {
                        if (vyberPlatnost.getSelectionModel().getSelectedItem() != null) {
                        if (vyberRoli.getSelectionModel().getSelectedItem() != null) {    
                            int d = 0;
                            int p = 0;

                            switch(vyberDen.getSelectionModel().getSelectedItem()){
                                case "pondělí": d = 1; break;
                                case "úterý": d = 2; break;
                                case "středa": d = 3; break;
                                case "čtvrtek": d = 4; break;
                                case "pátek": d = 5; break;
                            }

                            switch(vyberPlatnost.getSelectionModel().getSelectedItem()){
                                case "platná": p = 1; break;
                                case "neplatná": p = 0; break;
                            }

//Kontrola volné akce před jejím vložením                            
                                try {                                   
                                    if (p == 1 && plan != null){
                                        volnaAkce = c.rozvrhovaAkce(rok, plan.getSemestr(), obor, plan.getNazevP(), v.getPrijmeni(),
                                                v.getJmeno(), d, Integer.parseInt(casOdTxt.getText()), Integer.parseInt(casDoTxt.getText()),
                                                vyberMistnost.getSelectionModel().getSelectedItem().getOznaceni(), Integer.parseInt(obsazenostTxt.getText()));

                                        if (volnaAkce != null){
                                            if (volnaAkce.getMuzePridat().equals("true")) {
                                                PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                                                
                                                if (validniData != null) {
                                                    if (validniData.getMuzePridat().equals("true")) {
                                                        c.vlozRozvrhovaAkce(plan.getId(), v.getId(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                            vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p, 0, 0);
                                                        window.close();
                                                    } else {
                                                        alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                                                    }
                                                }    
                                            } else {
                                                alerts.alertInfo("Nelze přidat", volnaAkce.getHlaska());
                                            }
                                        }
                                    } else if (plan != null) {
                                        PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                                        
                                        if (validniData != null) {
                                            if (validniData.getMuzePridat().equals("true")) {
                                                c.vlozRozvrhovaAkce(plan.getId(), v.getId(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                            vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p, 0, 0);
                                                window.close();
                                            } else {
                                                alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                                            }
                                        }    
                                    }   
                                } catch (SQLException ex) {
                                    //System.out.println(ex.getSQLState() + " " + ex.getErrorCode() + ex.getMessage());
                                    alerts.alertError("Chyba", "Chyba při kontrole přidání akce.");
                                } catch (NumberFormatException ex) {//////////////////////////////lukas
                                    alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                                }
                            } else {
                                alerts.alertInfo("Oznámení", "Vyberte také roli vyučujícího.");
                            }       
                        } else {
                            alerts.alertInfo("Oznámení", "Vyberte také platnost akce.");
                        }
                    } else {
                        alerts.alertInfo("Oznámení", "Vyberte také den.");
                    }                   
                } else {
                    alerts.alertInfo("Oznámení", "Vyberte také místnost.");
                }
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také vyučujícího.");
            }                      
        });
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearDataRozvrh();
    }
    
//Dialog pro editaci rozvrhové akce
    public void editRozvrh(Controller c, String rok, String obor, ObservableList<ViewPlan> listPlan, ViewRozvrh rozvrhovaAkce, String email, String fakulta){
//Tvorba GUI     
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat rozvrhovou akci");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(500);
            window.setHeight(600);
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridRozvrh(grid);
            GridPane.setConstraints(editujR, 1, 17);
            GridPane.setHalignment(editujR, HPos.RIGHT);
            grid.getChildren().add(editujR);
        root.getChildren().add(grid); 
        
        getRozvrhLists(c, email); 
        
        if(!listPlan.isEmpty()){
            listZ.clear();
            listPlan.forEach((p) -> {
                listZ.add(p.getZpusobV());
            });
            vyberZpusob.setItems(listZ);
        } else {
            alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
        } 
        
        vyberZpusob.getSelectionModel().select(rozvrhovaAkce.getZpusob());

//Vyplnění formulářů daty
        Vyucujici vyuc = null;
        int vyucujiciIdVychozi = 0;
        for(Vyucujici vy : listV){
            String s = vy.getTitulPred() + " " + vy.getPrijmeni() + " " + vy.getJmeno() + " " + vy.getTitulZa();
            if (s.equals(rozvrhovaAkce.getVyucujici())) {
                vyuc = vy;
                vyucujiciIdVychozi = vy.getId();
            }
        }
        vyberVyucujici.getSelectionModel().select(vyuc);
        final int vyucujiciIdVychoziFinal = vyucujiciIdVychozi;
        
        Role ro = null;
        int roleIdVychozi = 0;
        for(Role r : listRole){
            if (r.getRole().equals(rozvrhovaAkce.getRole())) {
                ro = r;
                roleIdVychozi = r.getId();
            }
        }
        vyberRoli.getSelectionModel().select(ro);
        
        final int roleIdVychoziFinal = roleIdVychozi; 
        
        vyberDen.getSelectionModel().select(rozvrhovaAkce.getDen());
        int denVychozi = 0;
            switch(rozvrhovaAkce.getDen()){
                case "pondělí": denVychozi = 1; break;
                case "úterý": denVychozi = 2; break;
                case "středa": denVychozi = 3; break;
                case "čtvrtek": denVychozi = 4; break;
                case "pátek": denVychozi = 5; break;
            }
            
        final int denVychoziFinal = denVychozi; 
        
        vyberPlatnost.getSelectionModel().select(rozvrhovaAkce.getPlatnostSlovy());
        
        casOdTxt.setText(rozvrhovaAkce.getCasOd());
        casDoTxt.setText(rozvrhovaAkce.getCasDo());
        obsazenostTxt.setText(String.valueOf(rozvrhovaAkce.getObsazenost()));
        
        int mistnostIdVychozi = 0;
        try {
            listMistnost = FXCollections.observableArrayList(c.getMistnosti(fakulta, Integer.parseInt(obsazenostTxt.getText())));
            Mistnost mist = null;
            for(Mistnost m : listMistnost){
                if (m.getOznaceni().equals(rozvrhovaAkce.getMistnost())) {
                    mist = m;
                    mistnostIdVychozi = m.getId();
                }
            }
            vyberMistnost.getSelectionModel().select(mist);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při zobrazení místnosti.");
        }catch (NumberFormatException ex) {//////////////////////////////lukas
            alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
        }
        
        final int mistnostIdVychoziFinal = mistnostIdVychozi;

//Při výběru místnosti se zobrazí místnosti splňující zadanou kapacitu akce        
        vyberMistnost.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!obsazenostTxt.getText().equals("")) {
                    try {
                        listMistnost = FXCollections.observableArrayList(c.getMistnosti(fakulta, Integer.parseInt(obsazenostTxt.getText())));
                        vyberMistnost.hide();                       
                        vyberMistnost.setItems(listMistnost);
                        vyberMistnost.show();  
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Chyba při získání seznamu místností.");
                    }catch (NumberFormatException ex) {//////////////////////////////lukas
                        alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve uveďte maximální obsazenost studijní skupiny.");
                }
            }
        });
        
        for (ViewPlan p : listPlan) {
            ViewPlan plan = null;
                if (vyberZpusob.getSelectionModel().getSelectedItem() != null) {
                    if (p.getZpusobV().equals(vyberZpusob.getSelectionModel().getSelectedItem())) {
                        plan = p;
                        pocetH.setText("Rozsah hodin: " +String.valueOf(plan.getDelkaP()));
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte způsob výuky.");
                }
         }

//Tlačítko pro editaci rozvrhové akce        
        editujR.setOnAction(e -> {
            ViewPlan plan = null;
            PridaniAkce volnaAkce = null;
            
            for(ViewPlan p : listPlan){
                if (vyberZpusob.getSelectionModel().getSelectedItem() != null) {
                    if (p.getZpusobV().equals(vyberZpusob.getSelectionModel().getSelectedItem())) {
                        plan = p;
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte způsob výuky.");
                }
            }
            
            Vyucujici v;
            if (vyberVyucujici.getSelectionModel().getSelectedItem() != null) {
                v = vyberVyucujici.getSelectionModel().getSelectedItem();
                if (vyberMistnost.getSelectionModel().getSelectedItem() != null) { 
                    if (vyberDen.getSelectionModel().getSelectedItem() != null) {
                        if(vyberPlatnost.getSelectionModel().getSelectedItem() != null){
                        if (vyberRoli.getSelectionModel().getSelectedItem() != null) {     
                            int d = 0;
                            int p = 0;

                            switch(vyberDen.getSelectionModel().getSelectedItem()){
                                case "pondělí": d = 1; break;
                                case "úterý": d = 2; break;
                                case "středa": d = 3; break;
                                case "čtvrtek": d = 4; break;
                                case "pátek": d = 5; break;
                            }

                            switch(vyberPlatnost.getSelectionModel().getSelectedItem()){
                                    case "platná": p = 1; break;
                                    case "neplatná": p = 0; break;
                                }

//Kontrola volné akce před její editací                            
                                try {
                                    if(p == 1 && plan != null){
                                        c.deleteRozvrhovaAkce(rozvrhovaAkce.getId());
                                        volnaAkce = c.rozvrhovaAkce(rok, plan.getSemestr(), obor, plan.getNazevP(), v.getPrijmeni(),
                                                v.getJmeno(), d, Integer.parseInt(casOdTxt.getText()), Integer.parseInt(casDoTxt.getText()),
                                                vyberMistnost.getSelectionModel().getSelectedItem().getOznaceni(), Integer.parseInt(obsazenostTxt.getText()));
                                        c.vlozRozvrhovaAkceNOA(rozvrhovaAkce.getId(), plan.getId(), vyucujiciIdVychoziFinal, roleIdVychoziFinal, mistnostIdVychoziFinal, denVychoziFinal, 
                                                Integer.parseInt(rozvrhovaAkce.getCasOd()), Integer.parseInt(rozvrhovaAkce.getCasDo()), rozvrhovaAkce.getObsazenost(), 
                                                rozvrhovaAkce.getPlatnost(), 0, 0);                                

                                        if (volnaAkce != null){
                                            if (volnaAkce.getMuzePridat().equals("true")) {
                                                PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());

                                                if (validniData != null) {
                                                    if (validniData.getMuzePridat().equals("true")) {
                                                        c.editRozvrhovaAkce(rozvrhovaAkce.getId(), plan.getId(), v.getId(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                            vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p, 0, 0);
                                                        window.close();
                                                    } else {
                                                    alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                                                    }
                                                }    
                                            } else {
                                                alerts.alertInfo("Nelze editovat", volnaAkce.getHlaska());
                                            }
                                        }
                                    } else if (plan != null) {
                                        PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                                        
                                        if (validniData != null) {
                                            if (validniData.getMuzePridat().equals("true")) {
                                                c.editRozvrhovaAkce(rozvrhovaAkce.getId(), plan.getId(), v.getId(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                            vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p, 0, 0);
                                                window.close();
                                            } else {
                                            alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                                            }
                                        }    
                                    }
                                } catch (SQLException ex) {
                                    //System.out.println(ex.getSQLState() + " " + ex.getErrorCode() + ex.getMessage());
                                    alerts.alertError("Chyba", "Chyba při kontrole editace akce.");
                                } catch (NumberFormatException ex) {//////////////////////////////lukas
                                    alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                                } 
                            } else {
                                alerts.alertInfo("Oznámení", "Vyberte také roli vyučujícího.");
                            }     
                        }
                    } else {
                        alerts.alertInfo("Oznámení", "Nejdříve vyberte den.");
                    }                   
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte místnost.");
                }
            } else {
                alerts.alertInfo("Oznámení", "Nejdříve vyberte vyučujícího.");
            }                      
        });
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
        
        clearDataRozvrh();
    }

//Aktualizace seznamu vyučujících a rolí z databáze    
    private void getRozvrhLists(Controller c, String email){
        try { 
            listV = FXCollections.observableArrayList(c.getVyucujici());
            vyberVyucujici.setItems(listV);
            
            listRole = FXCollections.observableArrayList(c.getRoleNazvy());
            vyberRoli.setItems(listRole);
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Při načítaní seznamů nastala chyba.");
        }
    }

//Tvorba GUI    
    private void setGridRozvrh(GridPane grid){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(zpusob, 0, 3);
        GridPane.setConstraints(vyberZpusob, 1, 3);
        GridPane.setConstraints(vyucujici, 0, 6);
        GridPane.setConstraints(vyberVyucujici , 1, 6);
        GridPane.setConstraints(roleV, 0, 7);
        GridPane.setConstraints(vyberRoli, 1, 7);       
        GridPane.setConstraints(den, 0, 8);
        GridPane.setConstraints(vyberDen, 1, 8);
        GridPane.setConstraints(pocetH, 1, 9);
        GridPane.setConstraints(casOd, 0, 10);
        GridPane.setConstraints(casOdTxt, 1, 10);
        GridPane.setConstraints(casDo, 0, 11);
        GridPane.setConstraints(casDoTxt, 1, 11);
        GridPane.setConstraints(obsazenost, 0, 12);
        GridPane.setConstraints(obsazenostTxt, 1, 12);
        GridPane.setConstraints(mistnost, 0, 13);
        GridPane.setConstraints(vyberMistnost, 1, 13);
        GridPane.setConstraints(platnost, 0, 14);
        GridPane.setConstraints(vyberPlatnost, 1, 14);
        
        grid.getChildren().clear();
        
        grid.getChildren().addAll(zpusob, vyberZpusob, vyucujici, vyberVyucujici, roleV, vyberRoli, den, 
                vyberDen, casOd, casOdTxt, casDo, casDoTxt, pocetH, obsazenost, obsazenostTxt, 
                mistnost, vyberMistnost, platnost, vyberPlatnost);
    }

//Vyčištění formulářů    
    private void clearDataRozvrh(){
        casOdTxt.clear();
        casDoTxt.clear();
        obsazenostTxt.clear();
    }
}
