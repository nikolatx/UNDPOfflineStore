
package konstante;

import com.grupa1.model.Podesavanja;

public class Konst {
    
    //putanja do slika
    public static String SLIKE_PATH;// = "D:\\UNDPOfflineStore\\Slike";
    //velicina uvecane slike
    public static int VELICINA_SLIKE;//=400;
    //brisanje bookmark-a pri upisu u docx
    public static boolean DELETE_BOOKMARK;// = false;
    //putanja do prijemnica
    public static String PRIJEMNICE_PATH;// = "D:\\UNDPOfflineStore\\Prijemnice";
    //putanja i naziv fajla template-a prijemnice
    public static String PRIJEMNICA_TEMPLATE;// = "D:\\UNDPOfflineStore\\Prijemnice\\Prijemnica v1.03.docx";
    //putanja do faktura
    public static String FAKTURE_PATH;// = "D:\\UNDPOfflineStore\\Fakture";
    //putanja i naziv fajla do template-a fakture
    public static String FAKTURA_TEMPLATE;// = "D:\\UNDPOfflineStore\\Fakture\\Faktura v1.03.docx";

    public static void podesiParametre(Podesavanja podesavanja) {
        SLIKE_PATH=podesavanja.getSlikePath();
        VELICINA_SLIKE=podesavanja.getSlikeVelicina();
        DELETE_BOOKMARK=podesavanja.isDeleteBookmark();
        PRIJEMNICE_PATH=podesavanja.getPrijemnicePath();
        PRIJEMNICA_TEMPLATE=podesavanja.getPrijemnicaTemplate();
        FAKTURE_PATH=podesavanja.getFakturePath();
        FAKTURA_TEMPLATE=podesavanja.getFaktureTemplate();
    }
            
}
