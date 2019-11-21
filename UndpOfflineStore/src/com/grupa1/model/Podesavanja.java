
package com.grupa1.model;

/**
 *
 * @author nikolatimotijevic
 */
public class Podesavanja {

    //putanja do slika
    private String slikePath;
    //velicina uvecane slike
    private int slikeVelicina;
    //brisanje bookmark-a pri upisu u docx
    private boolean deleteBookmark = false;
    //putanja do prijemnica
    private String prijemnicePath;
    //putanja i naziv fajla template-a prijemnice
    private String prijemnicaTemplate;
    //putanja do faktura
    private String fakturePath;
    //putanja i naziv fajla do template-a fakture
    private String faktureTemplate;

    public Podesavanja(String slikePath, int slikeVelicina, boolean deleteBookmark, String prijemnicaPath, 
            String prijemnicaTemplate, String fakturePath, String faktureTemplate) {
        this.slikePath = slikePath;
        this.slikeVelicina= slikeVelicina;
        this.deleteBookmark = deleteBookmark;
        this.prijemnicePath = prijemnicaPath;
        this.prijemnicaTemplate = prijemnicaTemplate;
        this.fakturePath = fakturePath;
        this.faktureTemplate = faktureTemplate;
    }

    public Podesavanja() {
        slikePath = "D:\\UNDPOfflineStore\\Slike";
        slikeVelicina=400;
        deleteBookmark = false;
        prijemnicePath = "D:\\UNDPOfflineStore\\Prijemnice";
        prijemnicaTemplate = "D:\\UNDPOfflineStore\\Prijemnice\\Prijemnica v1.03.docx";
        fakturePath = "D:\\UNDPOfflineStore\\Fakture";
        faktureTemplate = "D:\\UNDPOfflineStore\\Fakture\\Faktura v1.03.docx";
    }
    
    public String getSlikePath() {
        return slikePath;
    }

    public void setSlikePath(String slikePath) {
        this.slikePath = slikePath;
    }

    public int getSlikeVelicina() {
        return slikeVelicina;
    }

    public void setSlikeVelicina(int slikeVelicina) {
        this.slikeVelicina = slikeVelicina;
    }

    public boolean isDeleteBookmark() {
        return deleteBookmark;
    }

    public void setDeleteBookmark(boolean deleteBookmark) {
        this.deleteBookmark = deleteBookmark;
    }

    public String getPrijemnicePath() {
        return prijemnicePath;
    }

    public void setPrijemnicePath(String prijemnicePath) {
        this.prijemnicePath = prijemnicePath;
    }

    public String getPrijemnicaTemplate() {
        return prijemnicaTemplate;
    }

    public void setPrijemnicaTemplate(String prijemnicaTemplate) {
        this.prijemnicaTemplate = prijemnicaTemplate;
    }

    public String getFakturePath() {
        return fakturePath;
    }

    public void setFakturePath(String fakturePath) {
        this.fakturePath = fakturePath;
    }

    public String getFaktureTemplate() {
        return faktureTemplate;
    }

    public void setFaktureTemplate(String faktureTemplate) {
        this.faktureTemplate = faktureTemplate;
    }
    
    
}
