package dialogs;

import data.BlokovaVyuka;
import data.Fakulta;
import data.Mistnost;
import data.Predmet;
import data.PridaniAkce;
import data.Role;
import data.ViewPlan;
import data.ViewPredmet;
import data.ViewRozvrh;
import data.ViewVyucujici;
import data.Vyucujici;
import gui.Controller;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DialogRozvrhyUcitel {
    private Alerts alerts = new Alerts();
    
    private Label predmetL = new Label("Předmět");   
    private ObservableList<Predmet> listPredmet = FXCollections.observableArrayList();
    private ObservableList<ViewPredmet> listP = FXCollections.observableArrayList();
    private ComboBox<Predmet> vyberPredmet = new ComboBox<>(listPredmet);
    
    private Label zpusob = new Label("Způsob výuky");
    private ObservableList<String> listZ = FXCollections.observableArrayList();
    private ComboBox<String> vyberZpusob = new ComboBox<>(listZ);
    
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
    
    private Label blokovaL = new Label("Bloková výuka");    
    private ObservableList<BlokovaVyuka> listBlokova = FXCollections.observableArrayList(
        new BlokovaVyuka(0, "Ne"));
    private ComboBox<BlokovaVyuka> vyberBlokovou = new ComboBox<>(listBlokova);
    
    private Button pridejR = new Button("Přidej");
    private Button editujR = new Button("Edituj");
    
    private ObservableList<ViewPlan> listPlan;
    private String obor;
    private String fakulta;
    private ViewVyucujici vyucujici;

//Dialog pro přidání rozvrhové akce    
    public void addRozvrh(Controller c, String rok, int opravneni, String email){
//Tvorba GUI        
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Přidat rozvrhovou akci");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/add.png")));
            window.setMinWidth(500);
            window.setHeight(600);
            
        getRozvrhLists(c, email);     
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridRozvrh(grid, opravneni);
            GridPane.setConstraints(pridejR, 1, 17);
            GridPane.setHalignment(pridejR, HPos.RIGHT);
            grid.getChildren().add(pridejR);
        root.getChildren().add(grid); 

//Nastavení parametrů k vybranému předmětu        
        if (vyberPredmet.getSelectionModel().getSelectedItem() != null) {
                ViewPredmet predmet = null;
                for(ViewPredmet pr : listP){
                    if (pr.getId() == vyberPredmet.getSelectionModel().getSelectedItem().getId()) {
                        predmet = pr;
                        obor = pr.getObor();
                        fakulta = pr.getFakulta();
                    }
                }

                if (predmet != null){
                    try {
                        listPlan = FXCollections.observableArrayList(c.getViewPlan(rok, predmet.getFakulta(), predmet.getObor(), predmet.getNazevP()));
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Předmět není ve studijním plánu.");
                    } catch (IndexOutOfBoundsException ex1) {
                        alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
                    }  
                } 
                
                if(!listPlan.isEmpty()){
                    listZ.clear();
                    listPlan.forEach((p) -> {
                        listZ.add(p.getZpusobV());
                    });
                    vyberZpusob.setItems(listZ);
                } else {
                    alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
                }    
        } 

//Při výběru předmětu se nastaví závislé parametry v dialogu        
        vyberPredmet.setOnAction(e -> {
            if (vyberPredmet.getSelectionModel().getSelectedItem() != null) {
                ViewPredmet predmet = null;
                for(ViewPredmet pr : listP){
                    if (pr.getId() == vyberPredmet.getSelectionModel().getSelectedItem().getId()) {
                        predmet = pr;
                        obor = pr.getObor();
                        fakulta = pr.getFakulta();
                    }
                }

                if (predmet != null){
                    try {
                        listPlan = FXCollections.observableArrayList(c.getViewPlan(rok, predmet.getFakulta(), predmet.getObor(), predmet.getNazevP()));
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Předmět není ve studijním plánu.");
                    } catch (IndexOutOfBoundsException ex1) {
                        alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
                    }  
                } 
                
                if(!listPlan.isEmpty()){
                    listZ.clear();
                    listPlan.forEach((p) -> {
                        listZ.add(p.getZpusobV());
                    });
                    vyberZpusob.setItems(listZ);
                } else {
                    alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
                } 
            } 
        });    

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
                        pocetH.setText(String.valueOf(plan.getDelkaP()));
                    }
                } else {
                    alerts.alertInfo("Oznámení", "Nejdříve vyberte způsob výuky.");
                }
            }
            
            if (vyberMistnost.getSelectionModel().getSelectedItem() != null) {
            if (vyberBlokovou.getSelectionModel().getSelectedItem() != null) {
            if (vyberDen.getSelectionModel().getSelectedItem() != null && 
                vyberBlokovou.getSelectionModel().getSelectedItem().getDatum().equals("Ne")) {
            if (vyberRoli.getSelectionModel().getSelectedItem() != null) {    
                int d = 0;
                int blokova = 0;

                if(vyberDen.getSelectionModel().getSelectedItem() != null) {
                    switch(vyberDen.getSelectionModel().getSelectedItem()){
                        case "pondělí": d = 1; break;
                        case "úterý": d = 2; break;
                        case "středa": d = 3; break;
                        case "čtvrtek": d = 4; break;
                        case "pátek": d = 5; break;
                    }
                } else {
//                    try {
//                        d = c.getDay(vyberBlokovou.getSelectionModel().getSelectedItem().getDatum());
//                    } catch (SQLException ex) {
//                        alerts.alertError("Chyba", "Chyba při získání dnu v týdnu");
//                    }
                }  

                switch (vyberBlokovou.getSelectionModel().getSelectedItem().getDatum()) {
                    case "Ne":
                        blokova = 0;
                        break;
                    default:
                        blokova = 1;
                         {
                            try {
                                d = c.getDay(vyberBlokovou.getSelectionModel().getSelectedItem().getDatum());
                            } catch (SQLException ex) {
                                alerts.alertError("Chyba", "Chyba při získání dnu v týdnu");
                            }
                        }
                        break;
                }

                if(opravneni == 0){
                    if (vyberPlatnost.getSelectionModel().getSelectedItem() != null) {
                        int p = 0;

                        switch(vyberPlatnost.getSelectionModel().getSelectedItem()){
                            case "platná": p = 1; break;
                            case "neplatná": p = 0; break;
                        }

//Kontrola volné akce před jejím vložením                        
                        try {                                    
                            if (p == 1 && plan != null){
                                volnaAkce = c.rozvrhovaAkce(rok, plan.getSemestr(), obor, plan.getNazevP(), vyucujici.getPrijmeni(),
                                        vyucujici.getJmeno(), d, Integer.parseInt(casOdTxt.getText()), Integer.parseInt(casDoTxt.getText()),
                                        vyberMistnost.getSelectionModel().getSelectedItem().getOznaceni(), Integer.parseInt(obsazenostTxt.getText()));

                                if (volnaAkce != null){
                                    if (volnaAkce.getMuzePridat().equals("true")) {
                                        PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                                        
                                        if (validniData != null) {
                                            if (validniData.getMuzePridat().equals("true")) {
                                                c.vlozRozvrhovaAkce(plan.getId(), vyucujici.getIdVyucujici(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                    vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p,
                                                    blokova, vyberBlokovou.getSelectionModel().getSelectedItem().getId());
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
                                        c.vlozRozvrhovaAkce(plan.getId(), vyucujici.getIdVyucujici(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                    vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p,
                                                    blokova, vyberBlokovou.getSelectionModel().getSelectedItem().getId());
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
                        alerts.alertInfo("Oznámení", "Vyberte také platnost akce.");
                    }
                } else if (plan != null){
                    try {
                        PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                        
                        if (validniData != null) {
                            if (validniData.getMuzePridat().equals("true")) {
                                c.vlozRozvrhovaAkce(plan.getId(), vyucujici.getIdVyucujici(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                        vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                        Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), 0,
                                        blokova, vyberBlokovou.getSelectionModel().getSelectedItem().getId());
                                window.close();
                            } else {
                                    alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                                }
                            }    
                    } catch (SQLException ex) {
                        alerts.alertError("Chyba", "Chyba při přidání neplatné akce.");
                    } catch (NumberFormatException ex) {//////////////////////////////lukas
                        alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                    }
                }
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také roli vyučujícího");
            }     
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také den");
            }     
            } else {
                alerts.alertInfo("Oznámení", "Má být výuka bloková?");
            }     
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také místnost.");
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
    public void editRozvrh(Controller c, String rok, ViewRozvrh rozvrhovaAkce, int opravneni, String email, int overeni){
//Tvorba GUI         
        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Editovat rozvrhovou akci");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(500);
            window.setHeight(600);
        
        getRozvrhLists(c, email);     
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();       
            setGridRozvrh(grid, opravneni);
            GridPane.setConstraints(editujR, 1, 17);
            GridPane.setHalignment(editujR, HPos.RIGHT);
            grid.getChildren().add(editujR);
        root.getChildren().add(grid);                               

//Vyplnění formulářů daty        
        String nazevP = null;
        for(ViewPredmet predmet : listP){
            if (predmet.getZkratkaP().equals(rozvrhovaAkce.getZkratka())) {
                nazevP = predmet.getNazevP();
                obor = predmet.getObor();
                fakulta = predmet.getFakulta();
                
                try {
                    listPlan = FXCollections.observableArrayList(c.getViewPlan(rok, predmet.getFakulta(), predmet.getObor(), predmet.getNazevP()));
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Předmět není ve studijním plánu.");
                } catch (IndexOutOfBoundsException ex1) {
                    alerts.alertInfo("Upozornění", "Daný předmět není ve studijním plánu.");
                }   
            }
        }
        
        Predmet pr = null;
        for(Predmet pred : listPredmet){
            if (pred.getNazev().equals(nazevP)) {
                pr = pred;               
            }
        }
        
        vyberPredmet.getSelectionModel().select(pr);

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
        
        if(rozvrhovaAkce.getBlokovaVyuka() == 0){
            vyberDen.getSelectionModel().select(rozvrhovaAkce.getDen());
        } 
        
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
        
        int blokovaIdVychozi = 0;
        vyberBlokovou.getSelectionModel().selectFirst();
        
        for(BlokovaVyuka b : listBlokova){
            if (b.getDatum().equals(rozvrhovaAkce.getDatum())) {
                vyberBlokovou.getSelectionModel().select(b);
                blokovaIdVychozi = b.getId();
                grid.getChildren().removeAll(den, vyberDen);
            }
        }
        
        final int blokovaIdVychoziFinal = blokovaIdVychozi;
        
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
       
        if (overeni == 1) {
            try {                               
                ObservableList<ViewVyucujici> listVyuc = FXCollections.observableArrayList(c.getViewVyucujici());
                for(ViewVyucujici v : listVyuc){
                    String s = v.getTitulPred() + " " + v.getPrijmeni() + " " + v.getJmeno() + " " + v.getTitulZa();
                    if (s.equals(rozvrhovaAkce.getVyucujici())) {
                        vyucujici = v;
                    }
                }
            } catch (SQLException ex) {
                alerts.alertError("Chyba", "Při načítaní vyučujících nastala chyba.");
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
            
            if (vyberMistnost.getSelectionModel().getSelectedItem() != null) { 
            if (vyberBlokovou.getSelectionModel().getSelectedItem() != null) {
            if (vyberDen.getSelectionModel().getSelectedItem() != null && 
                vyberBlokovou.getSelectionModel().getSelectedItem().getDatum().equals("Ne")) {
            if (vyberRoli.getSelectionModel().getSelectedItem() != null) {    
                int d = 0;
                int blokova = 0;

                if(vyberDen.getSelectionModel().getSelectedItem() != null){
                    switch(vyberDen.getSelectionModel().getSelectedItem()){
                        case "pondělí": d = 1; break;
                        case "úterý": d = 2; break;
                        case "středa": d = 3; break;
                        case "čtvrtek": d = 4; break;
                        case "pátek": d = 5; break;
                    }
                } else {
//                    try {
//                        d = c.getDay(vyberBlokovou.getSelectionModel().getSelectedItem().getDatum());
//                    } catch (SQLException ex) {
//                        alerts.alertError("Chyba", "Chyba při získání dnu v týdnu");
//                    }
                }  

                switch (vyberBlokovou.getSelectionModel().getSelectedItem().getDatum()) {
                    case "Ne":
                        blokova = 0;
                        break;
                    default:
                        blokova = 1;
                         {
                            try {
                                d = c.getDay(vyberBlokovou.getSelectionModel().getSelectedItem().getDatum());
                            } catch (SQLException ex) {
                                alerts.alertError("Chyba", "Chyba při získání dnu v týdnu");
                            }
                        }
                        break;
                }

                if(opravneni == 0){
                    if(vyberPlatnost.getSelectionModel().getSelectedItem() != null){
                        int p = 0;

                        switch(vyberPlatnost.getSelectionModel().getSelectedItem()){
                            case "platná": p = 1; break;
                            case "neplatná": p = 0; break;
                        }

//Kontrola volné akce před její editací                          
                        try {
                            if(p == 1 && plan != null){
                                c.deleteRozvrhovaAkce(rozvrhovaAkce.getId());
                                if (overeni == 1) {
                                    volnaAkce = c.rozvrhovaAkceUcitel(rok, plan.getSemestr(), obor, plan.getNazevP(), vyucujici.getPrijmeni(),
                                        vyucujici.getJmeno(), d, Integer.parseInt(casOdTxt.getText()), Integer.parseInt(casDoTxt.getText()),
                                        vyberMistnost.getSelectionModel().getSelectedItem().getOznaceni(), Integer.parseInt(obsazenostTxt.getText()));
                                } else {
                                    volnaAkce = c.rozvrhovaAkce(rok, plan.getSemestr(), obor, plan.getNazevP(), vyucujici.getPrijmeni(),
                                        vyucujici.getJmeno(), d, Integer.parseInt(casOdTxt.getText()), Integer.parseInt(casDoTxt.getText()),
                                        vyberMistnost.getSelectionModel().getSelectedItem().getOznaceni(), Integer.parseInt(obsazenostTxt.getText()));
                                }                                     
                                c.vlozRozvrhovaAkceNOA(rozvrhovaAkce.getId(), plan.getId(), vyucujici.getIdVyucujici(), roleIdVychoziFinal, mistnostIdVychoziFinal, denVychoziFinal, 
                                        Integer.parseInt(rozvrhovaAkce.getCasOd()), Integer.parseInt(rozvrhovaAkce.getCasDo()), rozvrhovaAkce.getObsazenost(), rozvrhovaAkce.getPlatnost(),
                                        rozvrhovaAkce.getBlokovaVyuka(), blokovaIdVychoziFinal);                                

                                if (volnaAkce != null){
                                    if (volnaAkce.getMuzePridat().equals("true")) {
                                        PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());

                                        if (validniData != null) {
                                            if (validniData.getMuzePridat().equals("true")) {
                                                c.editRozvrhovaAkce(rozvrhovaAkce.getId(), plan.getId(), vyucujici.getIdVyucujici(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                    vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p,
                                                    blokova, vyberBlokovou.getSelectionModel().getSelectedItem().getId());
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
                                PridaniAkce formAkce = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                            Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                                
                                if (formAkce != null) {
                                    if (formAkce.getMuzePridat().equals("true")) {
                                        c.editRozvrhovaAkce(rozvrhovaAkce.getId(), plan.getId(), vyucujici.getIdVyucujici(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                                vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                                Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), p,
                                                blokova, vyberBlokovou.getSelectionModel().getSelectedItem().getId());
                                        window.close();
                                    } else {
                                        alerts.alertInfo("Nelze přidat", formAkce.getHlaska());
                                    }
                                }   
                            }
                        } catch (SQLException ex) {
                            //System.out.println(ex.getSQLState() + " " + ex.getErrorCode() + ex.getMessage());
                            alerts.alertError("Chyba", "Chyba při kontrole editace akce.");
                        } catch (NumberFormatException ex) { //////////////////////////////lukas
                                alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                            }
                    } else {
                        alerts.alertInfo("Oznámení", "Vyberte také platnost akce.");
                    }
                } else if (plan != null) {
                    try {
                        PridaniAkce validniData = c.formularRozvrh(Integer.parseInt(casOdTxt.getText()),
                                    Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), plan.getDelkaP());
                        
                        if (validniData != null) {
                                if (validniData.getMuzePridat().equals("true")) {
                                c.editRozvrhovaAkce(rozvrhovaAkce.getId(), plan.getId(), vyucujici.getIdVyucujici(), vyberRoli.getSelectionModel().getSelectedItem().getId(),
                                        vyberMistnost.getSelectionModel().getSelectedItem().getId(), d, Integer.parseInt(casOdTxt.getText()),
                                        Integer.parseInt(casDoTxt.getText()), Integer.parseInt(obsazenostTxt.getText()), 0,
                                        blokova, vyberBlokovou.getSelectionModel().getSelectedItem().getId());
                                window.close();
                            } else {
                                    alerts.alertInfo("Nelze přidat", validniData.getHlaska());
                                }
                            }    
                    } catch (SQLException ex) {
                        Logger.getLogger(DialogRozvrhy.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NumberFormatException ex) {//////////////////////////////lukas
                        alerts.alertInfo("Nelze přidat", "Zkontolujte zda jste zadali číselné hodnoty správně");
                    }                                       
                }                                     
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také roli vyučujícího");
            }     
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také den");
            }     
            } else {
                alerts.alertInfo("Oznámení", "Má být výuka bloková?");
            }     
            } else {
                alerts.alertInfo("Oznámení", "Vyberte také místnost.");
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
            ObservableList<ViewVyucujici> listVyuc = FXCollections.observableArrayList(c.getViewVyucujici());
            String fakulta1 = null;
            for(ViewVyucujici v : listVyuc){
                if (v.getEmail().equals(email)) {
                    fakulta1 = v.getFakulta();
                    vyucujici = v;
                }
            }
            
            ObservableList<Fakulta> listFakult = FXCollections.observableArrayList(c.getFakulty());
            Fakulta fak = null;
            for(Fakulta f : listFakult){
                if(f.getNazevF().equals(fakulta1)){
                    fak = f;
                }
            }
                        
            listPredmet = FXCollections.observableArrayList(c.getPredmet(fakulta1));
            listP = FXCollections.observableArrayList(c.getViewPredmety(fak));
            vyberPredmet.setItems(listPredmet);
            vyberPredmet.getSelectionModel().selectFirst();
            
            listRole = FXCollections.observableArrayList(c.getRoleNazvy());
            vyberRoli.setItems(listRole);
            
            ObservableList<BlokovaVyuka> listBlok = FXCollections.observableArrayList(c.getBlokovaVyuka());
            for(BlokovaVyuka b : listBlok){
                listBlokova.add(b);
            }
            vyberBlokovou.setItems(listBlokova);
            //vyberBlokovou.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Při načítaní seznamů nastala chyba.");
        }
    }

//Tvorba GUI    
    private void setGridRozvrh(GridPane grid, int opravneni){
        grid.setPadding(new Insets(10, 10, 10, 10));        
        grid.setVgap(8);
        grid.setHgap(10);        
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
               
        GridPane.setConstraints(grid, 0, 1);
        GridPane.setConstraints(predmetL, 0, 2);
        GridPane.setConstraints(vyberPredmet, 1, 2);
        GridPane.setConstraints(zpusob, 0, 3);
        GridPane.setConstraints(vyberZpusob, 1, 3);
        GridPane.setConstraints(blokovaL, 0, 4);
        GridPane.setConstraints(vyberBlokovou, 1, 4);
        GridPane.setConstraints(roleV, 0, 8);
        GridPane.setConstraints(vyberRoli, 1, 8);       
        GridPane.setConstraints(den, 0, 9);
        GridPane.setConstraints(vyberDen, 1, 9);
        GridPane.setConstraints(pocetH, 1, 10);
        GridPane.setConstraints(casOd, 0, 11);
        GridPane.setConstraints(casOdTxt, 1, 11);
        GridPane.setConstraints(casDo, 0, 12);
        GridPane.setConstraints(casDoTxt, 1, 12);     
        GridPane.setConstraints(obsazenost, 0, 13);
        GridPane.setConstraints(obsazenostTxt, 1, 13);
        GridPane.setConstraints(mistnost, 0, 14);
        GridPane.setConstraints(vyberMistnost, 1, 14);
        GridPane.setConstraints(platnost, 0, 15);
        GridPane.setConstraints(vyberPlatnost, 1, 15);       
        
        grid.getChildren().clear();
        
        if (opravneni == 0) {
                grid.getChildren().addAll(predmetL, vyberPredmet, zpusob, vyberZpusob, roleV, vyberRoli, den, vyberDen, casOd, 
                casOdTxt, casDo, casDoTxt, pocetH, obsazenost, obsazenostTxt, mistnost, vyberMistnost, 
                platnost, vyberPlatnost, blokovaL, vyberBlokovou);         
        } else {
                grid.getChildren().addAll(predmetL, vyberPredmet, zpusob, vyberZpusob, roleV, vyberRoli, den, vyberDen, casOd, 
                casOdTxt, casDo, casDoTxt, pocetH, obsazenost, obsazenostTxt, mistnost, vyberMistnost, 
                blokovaL, vyberBlokovou);
        }
        
        vyberBlokovou.setOnAction(e -> {
            if (vyberBlokovou.getSelectionModel().getSelectedIndex() != 0) {               
                if (opravneni == 0) {
                    grid.getChildren().removeAll(den, vyberDen);         
                } else {
                    grid.getChildren().removeAll(den, vyberDen);   
                }
            } else {      
                if (opravneni == 0) {
                        grid.getChildren().removeAll(den, vyberDen);
                        grid.getChildren().addAll(den, vyberDen);         
                } else {
                        grid.getChildren().removeAll(den, vyberDen);
                        grid.getChildren().addAll(den, vyberDen);
                }
            }
        });
    }

//Vyčištění formulářů    
    private void clearDataRozvrh(){
        casOdTxt.clear();
        casDoTxt.clear();
        obsazenostTxt.clear();
    }

//Dialog pro přidání a editace blokové rozvrhové akce    
    public void blokoveVyuky(Controller c){
//Tvorba GUI        
        Label seznamB = new Label("Seznam blokových výuk");
            seznamB.setStyle("-fx-font-size: 20px");
        Button pridat = new Button("Nové datum");
        Button odebrat = new Button("Odebrat");       

        Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Upravit blokové výuky");
            window.getIcons().add(new Image(DialogPracoviste.class
                .getResourceAsStream("/img/edit.png")));
            window.setMinWidth(450);
            window.setHeight(400);
            
        Label vyuky = new Label("Bloková výuka");    
        ObservableList<BlokovaVyuka> listVyuk = null;
        try {
            listVyuk = FXCollections.observableArrayList(c.getBlokovaVyuka());
        } catch (SQLException ex) {
            alerts.alertError("Chyba", "Chyba při získání sezamu blokových výuk.");
        }
        ComboBox<BlokovaVyuka> vyberVyuku = new ComboBox<>(listVyuk);
        vyberVyuku.getSelectionModel().selectLast();
        vyberVyuku.setPrefWidth(250);         
        
        GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);
            root.setId("gridRoot");
        GridPane grid = new GridPane();                                          
            grid.setPadding(new Insets(10, 10, 10, 10));        
            grid.setVgap(8);
            grid.setHgap(10);        
            grid.setAlignment(Pos.CENTER);
            
            GridPane.setConstraints(seznamB, 0, 0, 2, 1);
            GridPane.setConstraints(vyuky, 0, 1);
            GridPane.setConstraints(vyberVyuku, 0, 1, 2, 1);
            GridPane.setConstraints(pridat, 0, 2);
            GridPane.setConstraints(odebrat, 1, 2);
            
            grid.getChildren().addAll(seznamB, vyuky, vyberVyuku, pridat, odebrat);
        root.getChildren().add(grid); 

//Tlačítko pro přidání blokové výuky        
        pridat.setOnAction(e -> {
            Label format = new Label("Zadejte datum ve formátu DD.MM.YYYY:");
            TextField datumTxt = new TextField();
            Button dokoncit = new Button("Vložit datum");
            
            GridPane.setConstraints(format, 0, 5, 2, 1);
            GridPane.setConstraints(datumTxt, 0, 6);
            GridPane.setConstraints(dokoncit, 0, 7);
            
            grid.getChildren().clear();
            grid.getChildren().addAll(seznamB, vyuky, vyberVyuku, pridat, odebrat,
                    format, datumTxt, dokoncit);

//Tlačítko pro dokončení přidání blokové výuky            
            dokoncit.setOnAction(e1 -> {
                try {
                    c.vlozBlokovaVyuka(datumTxt.getText());
                    alerts.alertInfo("Oznámení", "Vložení proběhlo úspěšně");
                    datumTxt.clear();
                    ObservableList<BlokovaVyuka> list = FXCollections.observableArrayList(c.getBlokovaVyuka());
                    vyberVyuku.setItems(list);
                    vyberVyuku.getSelectionModel().selectLast();
                    grid.getChildren().clear();
                    grid.getChildren().addAll(seznamB, vyuky, vyberVyuku, pridat, odebrat);
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Chyba při vkládání nového data.");
                }
            });
        });

//Tlačítko pro smazání blokové výuky        
        odebrat.setOnAction(e -> {
            if (vyberVyuku.getSelectionModel().getSelectedItem() != null) {
                try {
                    c.deleteBlokovaVyuka(vyberVyuku.getSelectionModel().getSelectedItem().getId());
                    alerts.alertInfo("Oznámení", "Odstranění proběhlo úspěšně");
                    ObservableList<BlokovaVyuka> list = FXCollections.observableArrayList(c.getBlokovaVyuka());
                    vyberVyuku.setItems(list);
                    vyberVyuku.getSelectionModel().selectLast();
                } catch (SQLException ex) {
                    alerts.alertError("Chyba", "Nepodařilo se smazat datum, ujistěte se zda není "
                            + "součástí nějaké blokové výuky.");
                }
            } else {
                alerts.alertInfo("Oznámení", "Vyberte prosím datum, které chcete smazat.");
            }           
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/MainStyle.css")
               .toExternalForm());        
        window.setScene(scene);
        window.showAndWait();
    }
}
