
package util;

import com.grupa1.model.Dokument;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.Osoba;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import konstante.Konst;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.RangeFinder;
import org.docx4j.jaxb.Context;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.CTMarkupRange;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;


public class DocxExport {

    private static org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();


    public static void exportData(boolean prijemnica, Osoba osoba, Dokument dokument, ObservableList<KomponentaSaSlikom> komponente) throws Exception {

        //kreiranje mape za zamenu bookmarka
        Map<DataFieldName, String> map4Bookmarks = new HashMap<DataFieldName, String>();
        map4Bookmarks.put( new DataFieldName("broj"), String.valueOf(dokument.getDokumentId()));
        //tekuci datum u nasem formatu
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy.");  
        String strDate = formatter.format(date);  
        map4Bookmarks.put( new DataFieldName("datum"), strDate);
        map4Bookmarks.put( new DataFieldName("gradDrzavaPostanskiBroj"), osoba.getGrad()+", "+osoba.getDrzava()+", "+osoba.getPostBr());
        map4Bookmarks.put( new DataFieldName("nazivKlijenta"), osoba.getNaziv());
        map4Bookmarks.put( new DataFieldName("ulicaIBroj"), osoba.getUlica());
        map4Bookmarks.put( new DataFieldName("telefon"), osoba.getTelefon());

        //ucitavanje template docx fajla
        File docxfile=null;
        try {
            if (prijemnica)
                docxfile=new java.io.File(Konst.PRIJEMNICA_TEMPLATE);
            else
                docxfile=new java.io.File(Konst.FAKTURA_TEMPLATE);
        } catch (Exception ex) {
            throw new Exception("Na postoji template fajl " + (prijemnica?Konst.PRIJEMNICA_TEMPLATE:Konst.FAKTURA_TEMPLATE));
        }
        
        //ucitavanje elemenata docx fajla
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docxfile);
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

        org.docx4j.wml.Document wmlDocumentEl = (org.docx4j.wml.Document) documentPart.getJaxbElement();
        Body body = wmlDocumentEl.getBody();

        //BookmarksReplaceWithText bti = new BookmarksReplaceWithText();
        
        DocxExport bti=new DocxExport();
        //zamena bookmarka zadatim tekstom
        bti.replaceBookmarkContents(body.getContent(), map4Bookmarks);

        //ubacivanje komponenata u tabelu docx dokumenta

        //kreiranje mape palceholder, vrednost
        Map<String, String> map = new HashMap();
        //kreiranje liste mapa za ubacivanje vise vrsta u tabelu
        List<Map<String, String>> listaMapa = new ArrayList<>();
        
        int kolicina;
        double cena, ukupno, suma=0;
        //popunjavanje mape podacima
        for(KomponentaSaSlikom komponenta:komponente) {
            map = new HashMap<String, String>();
            map.put( "UX", String.valueOf(komponenta.getId()));
            map.put( "UNDP_opis", komponenta.getNaziv());
            kolicina=komponenta.getKolicina();
            cena=komponenta.getCena();
            ukupno=kolicina*cena;
            suma+=ukupno;
            map.put( "UNDP_kol", String.valueOf(kolicina));
            //format cene: dve decimale sa thousand separatorom
            map.put( "UNDP_jc", String.format("%,.2f", cena));
            map.put( "UNDP_uk", String.format("%,.2f",ukupno));
            listaMapa.add(map);
        };

        //pronalazenje tabele za smestaj komponenata u docx dokumentu
        List<Tbl> lista=new ArrayList<>();
        //preuzimanje svih tabela iz docx dokumenta
        lista=getAllTbls(wordMLPackage);
        //pronalazenje tabele sa nazivom "PrijemnicaTBL"
        Tbl tabela=null;
        for(Tbl t:lista)
            try {
                if (t.getTblPr().getTblCaption().getVal()!=null && t.getTblPr().getTblCaption().getVal().equals("PrijemnicaTBL"))
                    tabela=t;
            } catch (Exception ex) {}
        
        if (tabela!=null) {
            //zameni placeholdere sa vrednostima iz mape po kljucu
            popuniTabelu(tabela, new String[]{"UX", "UNDP_opis", "UNDP_kol", "UNDP_jc", "UNDP_uk"}, listaMapa, wordMLPackage);
            //ubaci vrednost za ukupno
            listaMapa=new ArrayList<>();
            map.put( "UNDP_UUU", String.format("%,.2f", suma));
            listaMapa.add(map);
            popuniTabelu(tabela, new String[]{"UNDP_UUU"}, listaMapa, wordMLPackage);
            //sacuvaj dokument sa brojem, datumom i vremenom kreiranja u nazivu
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
            String datum=formatter1.format(date);
            //zadavanje naziva fajla
            String nazivFajla=(prijemnica)?Konst.PRIJEMNICE_PATH+"\\Prijemnica ":Konst.FAKTURE_PATH+"\\Faktura ";
            wordMLPackage.save(new java.io.File(nazivFajla + "broj " + dokument.getDokumentId() + " " + datum +".docx"));
        } else
            throw new Exception("Ne postoji odgovarajuća tabela u dokumentu " + (prijemnica?"prijemnice!":"fakture!"));
        
    }


    private static void popuniTabelu(Tbl tabela, String[] placeholders, List<Map<String, String>> textToAdd,
                    WordprocessingMLPackage template) throws Docx4JException, JAXBException {
        //preuzmi vrste iz tabele
        List<Object> vrste = getAllElementFromObject(tabela, Tr.class);
        //druga vrsta je "sablonska" vrsta - sa poljima koja se popunjavaju
        Tr sablonVrsta = (Tr) vrste.get(1);
        for (Map<String, String> replacements : textToAdd) {
            //dodavanje nove vrste popunjene podacima
            dodajVrstuUTabelu(tabela, sablonVrsta, replacements, vrste.size());
        }
        //brisanje sablonske vrste
        tabela.getContent().remove(sablonVrsta);
    }

    private static void dodajVrstuUTabelu(Tbl tabela, Tr sablonVrsta, Map<String, String> replacements, int brojVrste) {
        Tr vrsta = (Tr) XmlUtils.deepCopy(sablonVrsta);
        List<?> textElements = getAllElementFromObject(vrsta, Text.class);
        for (Object object : textElements) {
                Text text = (Text) object;
                String replacementValue = (String) replacements.get(text.getValue());
                if (replacementValue != null)
                        text.setValue(replacementValue);
        }
        tabela.getContent().add(brojVrste, vrsta);
    }

    //pomocna metoda za kreiranje liste tabela docx dokumenta
    private static List<Tbl> getAllTbls(WordprocessingMLPackage wordMLPackage) {
        MainDocumentPart mainDocPart = wordMLPackage.getMainDocumentPart();
        List<Object> objList = getAllElementFromObject(mainDocPart, Tbl.class);
        if (objList == null) {
            return null;
        }
        List<Tbl> tblList = new ArrayList<Tbl>();
        for (Object obj : objList) {
            if (obj instanceof Tbl) {
                Tbl tbl = (Tbl) obj;
                tblList.add(tbl);
            }
        }
        return tblList;
    }

    //pomocna metoda za pruzimanje svih objekata iz docx dokumenta
    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }

    //metoda za zamenu bookmarka tekstom
    private void replaceBookmarkContents(List<Object> paragraphs,  Map<DataFieldName, String> data) throws Exception {

        RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
        new TraversalUtil(paragraphs, rt);

        for (CTBookmark bm : rt.getStarts()) {

            //da li je ovaj bookmark u mapi za zamenu
            if (bm.getName()==null) continue;
            String value = data.get(new DataFieldName(bm.getName()));
            if (value==null) continue;

            try {
                // Can't just remove the object from the parent,
                // since in the parent, it may be wrapped in a JAXBElement
                List<Object> theList = null;
                if (bm.getParent() instanceof P) {
                    theList = ((ContentAccessor)(bm.getParent())).getContent();
                } else {
                    continue;
                }

                int rangeStart = -1;
                int rangeEnd=-1;
                int i = 0;
                for (Object ox : theList) {
                    Object listEntry = XmlUtils.unwrap(ox); 
                    if (listEntry.equals(bm)) {
                        if (Konst.DELETE_BOOKMARK) {
                                rangeStart=i;
                        } else {
                                rangeStart=i+1;							
                        }
                    } else if (listEntry instanceof  CTMarkupRange) {
                        if ( ((CTMarkupRange)listEntry).getId().equals(bm.getId())) {
                            if (Konst.DELETE_BOOKMARK) {
                                    rangeEnd=i;
                            } else {
                                    rangeEnd=i-1;							
                            }
                            break;
                        }
                    }
                    i++;
                }

                if (rangeStart>0 && rangeEnd>rangeStart) {

                    // obrisi "bookmark range"
                    for (int j =rangeEnd; j>=rangeStart; j--) {
                        theList.remove(j);
                    }

                    // dodaj tekst
                    org.docx4j.wml.R  run = factory.createR();
                    org.docx4j.wml.Text  t = factory.createText();
                    run.getContent().add(t);		
                    t.setValue(value);

                    theList.add(rangeStart, run);
                }

            } catch (ClassCastException cce) {
                throw new Exception(cce.getMessage());
            }
        }
    }
    
}
