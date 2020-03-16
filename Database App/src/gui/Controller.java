package gui;

import data.AkRok;
import data.BlokovaVyuka;
import data.Fakulta;
import data.FormaVyuky;
import data.Katedra;
import data.Kategorie;
import data.Mistnost;
import data.Predmet;
import data.PridaniAkce;
import data.Role;
import data.Semestr;
import data.StudijniObor;
import data.ViewPredmet;
import data.ViewKatedry;
import data.ViewObor;
import data.ViewPlan;
import data.ViewRozvrh;
import data.ViewVyucujici;
import data.Vyucujici;
import data.VyucujiciRole;
import data.Zakonceni;
import data.ZpusobVyuky;
import data.ViewPrihlaseni;
import database.DatabaseHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author st52550
 */
public class Controller {
    private DatabaseHelper dh;
    
    public boolean login(String login, String psw) throws SQLException{
        try {
            dh = new DatabaseHelper(login, psw);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }           
        return false;
    }
    
    public ArrayList<ViewVyucujici> getViewVyucujici() throws SQLException {
        return dh.getViewVyucujici();
    }
    
    public ArrayList<ViewVyucujici> getViewVyucujici(Fakulta f) throws SQLException {
        return dh.getViewVyucujici(f);
    }
    
    public ArrayList<ViewKatedry> getViewKatedry(Fakulta f) throws SQLException {
        return dh.getViewKatedry(f);
    }
    
    public ArrayList<ViewPredmet> getViewPredmety(Fakulta f) throws SQLException {
        return dh.getViewPredmety(f);
    }
    
    public ArrayList<ViewObor> getViewObory(Fakulta f) throws SQLException {
        return dh.getViewObory(f);
    }
    
    public ArrayList<ViewPlan> getViewPlan(AkRok rok, ViewObor obor) throws SQLException {
        return dh.getViewPlan(rok, obor);
    }
    
    public ArrayList<ViewPlan> getViewPlan(AkRok rok, Fakulta fakulta, ViewObor obor, Predmet predmet) throws SQLException {
        return dh.getViewPlan(rok, fakulta, obor, predmet);
    }
    
    public ArrayList<ViewPlan> getViewPlan(String rok, String fakulta, String obor, String predmet) throws SQLException {
        return dh.getViewPlan(rok, fakulta, obor, predmet);
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrh(AkRok rok, ViewObor obor, Predmet predmet) throws SQLException {
        return dh.getViewRozvrh(rok, obor, predmet);
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrhPlatneA(AkRok rok, ViewObor obor, Predmet predmet) throws SQLException {
        return dh.getViewRozvrhPlatneA(rok, obor, predmet);
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrhNeplatneA(AkRok rok) throws SQLException {
        return dh.getViewRozvrhNeplatneA(rok);
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrhBlokoveA(AkRok rok, String email) throws SQLException {
        return dh.getViewRozvrhBlokoveA(rok, email);
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrh(AkRok rok, String email) throws SQLException {
        return dh.getViewRozvrh(rok, email);
    }
    
    public ArrayList<ViewRozvrh> getViewRozvrhPlatneA(AkRok rok, String email) throws SQLException {
        return dh.getViewRozvrhPlatneA(rok, email);
    }
    
    public ArrayList<Katedra> getKatedry() throws SQLException {
        return dh.getKatedry();
    }
    
    public ArrayList<Katedra> getKatedry(String fakulta) throws SQLException {
        return dh.getKatedry(fakulta);
    }
    
    public ArrayList<StudijniObor> getObory() throws SQLException {
        return dh.getObory();
    }
    
    public ArrayList<StudijniObor> getObory(String fakulta) throws SQLException {
        return dh.getObory(fakulta);
    }
    
    public ArrayList<Fakulta> getFakulty() throws SQLException {
        return dh.getFakulty();
    }
    
    public ArrayList<Mistnost> getMistnosti(String fakulta) throws SQLException {
        return dh.getMistnosti(fakulta);
    }
    
    public ArrayList<Mistnost> getMistnosti(String fakulta, int max_obsazenost) throws SQLException {
        return dh.getMistnosti(fakulta, max_obsazenost);
    }
    
    public ArrayList<AkRok> getRoky() throws SQLException {
        return dh.getRoky();
    }
    
    public int getDay(String datum) throws SQLException{
        return dh.getDay(datum);
    }
    
    public VyucujiciRole getRole(int idPredmet) throws SQLException {
        return dh.getRole(idPredmet);
    }
    
    public ArrayList<Role> getRoleNazvy() throws SQLException {
        return dh.getRoleNazvy();
    }
    
    public ArrayList<Vyucujici> getVyucujici() throws SQLException {
        return dh.getVyucujici();
    }
    
    public ArrayList<Kategorie> getKategorie() throws SQLException {
        return dh.getKategorie();
    }
    
    public ArrayList<Predmet> getPredmet(String fakulta) throws SQLException {
        return dh.getPredmet(fakulta);
    }
    
    public ArrayList<Predmet> getPredmetObor(String obor) throws SQLException {
        return dh.getPredmetObor(obor);
    }
    
    public ArrayList<ZpusobVyuky> getZpusobVyuky() throws SQLException {
        return dh.getZpusobVyuky();
    }
    
    public ArrayList<Zakonceni> getZakonceni() throws SQLException {
        return dh.getZakonceni();
    }
    
    public ArrayList<FormaVyuky> getFormaVyuky() throws SQLException {
        return dh.getFormaVyuky();
    }
    
    public ArrayList<Semestr> getSemestr() throws SQLException {
        return dh.getSemestr();
    }
    
    public ArrayList<BlokovaVyuka> getBlokovaVyuka() throws SQLException {
        return dh.getBlokovaVyuka();
    }
    
    public void vlozVyucujiciho(String titPred, String prijmeni, String jmeno, String titZa,
            String katedra, String telefon, String mobil, String email) throws SQLException, IOException{
        dh.insertVyucujici(titPred, prijmeni, jmeno, titZa, katedra, telefon, mobil, email);
    };
    
    public void vlozKatedru(String zkK, String katedra, String fakulta) throws SQLException{
        dh.insertKatedra(zkK, katedra, fakulta);
    }
    
    public void vlozFakultu(String zkF, String fakulta, String telefon, 
            String mobil, String email) throws SQLException{
        dh.insertFakulta(zkF, fakulta, telefon, mobil, email);
    }
    
    public void vlozPredmet(String zkPred, String nazPred, String kredity, String pocPr, String pocCv,
            String pocSem, String rocnik, String obor) throws SQLException{
        dh.insertPredmet(zkPred, nazPred, kredity,  pocPr, pocCv, pocSem, rocnik, obor);
    }
    
    public void vlozMistnost(String oznaceni, int kapacita, String katedra) throws SQLException{
        dh.insertMistnost(oznaceni, kapacita, katedra);
    }
    
    public void vlozObor(String oznaceni, String nazO, String katedra) throws SQLException {
        dh.insertObor(oznaceni, nazO, katedra);
    }
    
    public void vlozBlokovaVyuka(String datum) throws SQLException {
        dh.insertBlokovaVyuka(datum);
    }
    
    public void pridatRole(int idPredmet, int idVyucujici, int idRole) throws SQLException{
        dh.pridatRole(idPredmet, idVyucujici, idRole);
    }
    
    public void vlozPredmetPlan(String akRok, int idKategorie, int idPredmet, String delka,
            String odhad, int idVyuka, int idZakonceni, int idForma, int idSemestr) throws SQLException{
        dh.insertPredmetPlan(akRok, idKategorie, idPredmet, delka, odhad, idVyuka, idZakonceni, idForma, idSemestr);
    }
    
    public void vlozRozvrhovaAkce(int akRokId, int vyucujiciId, int roleId, int mistnostId,
            int den, int casOd, int casDo, int max_obsazenost, int platnost, int blokovaVyuka, int blokovaVyukaId) throws SQLException{
        dh.insertRozvrhovaAkce(akRokId, vyucujiciId, roleId, mistnostId, den, casOd, casDo, max_obsazenost, platnost,
                blokovaVyuka, blokovaVyukaId);
    }
    
    public void vlozRozvrhovaAkceNOA(int rozvrhovaAkceId, int akRokId, int vyucujiciId, int roleId, int mistnostId,
            int den, int casOd, int casDo, int max_obsazenost, int platnost, int blokovaVyuka, int blokovaVyukaId) throws SQLException{
        dh.insertRozvrhovaAkce(rozvrhovaAkceId, akRokId, vyucujiciId, roleId, mistnostId, den, casOd, casDo, 
                max_obsazenost, platnost, blokovaVyuka, blokovaVyukaId);
    }
    
    public void editVyucujiciho(int idVyucujici, String titPred, String prijmeni, String jmeno, String titZa,
            String katedra, String telefon, String mobil, String email) throws SQLException{
        dh.updateVyucujici(idVyucujici, titPred, prijmeni, jmeno, titZa, katedra, telefon, mobil, email);
    }
    
    public void editKatedra(int idKatedra, String zkK, String katedra, String fakulta) throws SQLException{
        dh.updateKatedra(idKatedra, zkK, katedra, fakulta);
    }
    
    public void editFakulta(int idFakulta, String zkF, String fakulta,
            String telefon, String mobil, String email) throws SQLException{
        dh.updateFakulta(idFakulta, zkF, fakulta, telefon, mobil, email);
    }
    
    public void editPredmet(int idPredmet, String zkPred, String nazPred, String kredity, String pocPr, String pocCv,
            String pocSem, String rocnik, String obor) throws SQLException{
        dh.updatePredmet(idPredmet, zkPred, nazPred, kredity,  pocPr, pocCv, pocSem, rocnik, obor);
    }
    
    public void editMistnost(int idMistnost, String oznaceni, int kapacita, String katedra) throws SQLException{
        dh.updateMistnost(idMistnost, oznaceni, kapacita, katedra);
    }
    
    public void editObor(int idObor, String oznaceni, String nazO, String katedra) throws SQLException {
        dh.updateObor(idObor, oznaceni, nazO, katedra);
    }
    
    public void editRole(int idPredmet, int idVyucujici, int idRole) throws SQLException{
        dh.updateRole(idPredmet, idVyucujici, idRole);
    }
    
    public void editPredmetPlan(int idPredmetPlan, String akRok, int idKategorie, int idPredmet, String delka,
            String odhad, int idVyuka, int idZakonceni, int idForma, int idSemestr) throws SQLException{
        dh.updatePredmetPlan(idPredmetPlan, akRok, idKategorie, idPredmet, delka, odhad, idVyuka, idZakonceni, idForma, idSemestr);
    }
    
    public void editRozvrhovaAkce(int idRozvrhovaAkce, int akRokId, int vyucujiciId, int roleId, int mistnostId,
            int den, int casOd, int casDo, int max_obsazenost, int platnost, int blokovaVyuka, int blokovaVyukaId) throws SQLException{
        dh.updateRozvrhovaAkce(idRozvrhovaAkce, akRokId, vyucujiciId, roleId, mistnostId, den, casOd, casDo, 
                max_obsazenost, platnost, blokovaVyuka, blokovaVyukaId);
    }
    
    public void deleteVyucujici(int idVyucujici, String email) throws SQLException{
        dh.deleteVyucujici(idVyucujici, email);
    }
    
    public void deleteKatedra(int idKatedra) throws SQLException{
        dh.deleteKatedra(idKatedra);
    }
    
    public void deleteFakulta(String fakulta, String email) throws SQLException{
        dh.deleteFakulta(fakulta, email);
    }
    
    public void deletePredmet(int idPredmet) throws SQLException{
        dh.deletePredmet(idPredmet);
    }
    
    public void deleteMistnost(int idMistnost) throws SQLException {
        dh.deleteMistnost(idMistnost);
    }
    
    public void deleteObor(int idObor) throws SQLException{
        dh.deleteObor(idObor);
    }
    
    public void deleteBlokovaVyuka(int idBlokovaVyuka) throws SQLException {
        dh.deleteBlokovaVyuka(idBlokovaVyuka);
    }
    
    public void deletePredmetPlan(int idPredmetPlan) throws SQLException{
        dh.deletePredmetPlan(idPredmetPlan);
    }
    
    public void deleteRozvrhovaAkce(int idRozvrhovaAkce) throws SQLException {
        dh.deleteRozvrhovaAkce(idRozvrhovaAkce);
    }
    
    public PridaniAkce rozvrhovaAkce(String rok, String semestr, String obor, String predmet, String prijmeni,
            String jmeno, int den, int casOd, int casDo, String mistnost, int max_obsazenost) throws SQLException {
        return dh.rozvrhovaAkce(rok, semestr, obor, predmet, prijmeni, jmeno, den, casOd, casDo, mistnost, max_obsazenost);
    }
    
    public PridaniAkce rozvrhovaAkceUcitel(String rok, String semestr, String obor, String predmet, String prijmeni,
            String jmeno, int den, int casOd, int casDo, String mistnost, int max_obsazenost) throws SQLException {
        return dh.rozvrhovaAkceUcitel(rok, semestr, obor, predmet, prijmeni, jmeno, den, casOd, casDo, mistnost, max_obsazenost);
    }
    
    ////////////////////Lukas
    public int prihlaseniUzivatele(String jmeno, String heslo) throws SQLException{
        return dh.prihlaseniUzivatele(jmeno, heslo);
    }  

    public void ulozObrazek(InputStream is,String nazev, String pripona, int idU) throws SQLException, IOException {
        dh.ulozObrazek(is, nazev, pripona,idU);
    }
    
    public InputStream nactiObrazek(int id) throws SQLException, FileNotFoundException, IOException {
        return dh.nactiObrazek(id);
    }
    
    public ArrayList<ViewPrihlaseni> getViewPrihlaseni(Fakulta f) throws SQLException {
        return dh.getViewPrihlaseni(f);
    }
    public void vlozLogin(String username, String heslo, int idU) throws SQLException, IOException{
        dh.insertLogin(username, heslo, idU);
    };
    public void editLogin(int id_user,String username) throws SQLException{
        dh.updateLogin(id_user,username);
    }
    public void editLoginHeslo(int id_user, String heslo) throws SQLException{
        dh.updateLoginHeslo(id_user, heslo);
    }
    public void deleteLogin(int id) throws SQLException{
        dh.deleteLogin(id);
    }
    
    /////////////////lukas
    public PridaniAkce formularVyucujici(String titulPred, String jmeno, String prijmeni, String titulZa,
            int telefon, int mobil, String email) throws SQLException {
        return dh.formularVyucujici(titulPred, jmeno, prijmeni, titulZa, telefon, mobil, email);
    }

    public PridaniAkce formularPredmet(String zkratka, String nazev, int pocetKreditu, int pocetPr,
            int pocetCv, int pocetSem, int dopRoc) throws SQLException {
        return dh.formularPredmet(zkratka, nazev, pocetKreditu, pocetPr, pocetCv, pocetSem, dopRoc);
    }

    public PridaniAkce formularFakulta(String zkratka, String nazev, int telefon, int mobil, String email) throws SQLException {
        return dh.formularFakulta(zkratka, nazev, telefon, mobil, email);
    }

    public PridaniAkce formularKatedra(String zkratka, String nazev) throws SQLException {
        return dh.formularKatedra(zkratka, nazev);
    }

    public PridaniAkce formularMistnost(String oznaceni, int kapacita) throws SQLException {
        return dh.formularMistnost(oznaceni, kapacita);
    }

    public PridaniAkce formularObor(String oznaceni, String nazev) throws SQLException {
        return dh.formularObor(oznaceni, nazev);
    }

    public PridaniAkce formularPrih(String username) throws SQLException {
        return dh.formularPrih(username);
    }

    public PridaniAkce formularPlan(String akRok, int delka, int odhad) throws SQLException {
        return dh.formularPlan(akRok, delka, odhad);
    }

    public PridaniAkce formularRozvrh(int casOd, int casDo, int obsazenost,int rozsah) throws SQLException {
        return dh.formularRozvrh(casOd, casDo, obsazenost,rozsah);
    }
}
