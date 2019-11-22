
package com.grupa1.dbconnection;

/**
 *
 * @author nikolatimotijevic
 */
public class IzvestajDAO {
    
    /*
    
    select Monthname(f.datum), sum(df.cena*df.kolicina) from faktura as f inner join detaljifakture as df on f.faktura_id=df.faktura_id where f.datum between date_sub(now(), interval 365 day) and now() group by MonthName(f.datum) order by month(f.datum)
    
    poslednjih 12 meseci
    sa izborom: poslednji mesec, nedelja, dan
    5 najprodavanijih komponenata
    5 komponenata na kojima je najvise zaradjeno
    
    
    
    */
    
    
    
}
