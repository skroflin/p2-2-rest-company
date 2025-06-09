/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import com.github.javafaker.Faker;
import ffos.skroflin.model.Djelatnik;
import ffos.skroflin.model.Odjel;
import ffos.skroflin.model.dto.DjelatnikDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class DjelatnikService extends MainService{
    public List<Djelatnik> getAll(){
        return session.createQuery("from djelatnik", Djelatnik.class).list();
    }
    
    public Djelatnik getBySifra(int sifra){
        return session.get(Djelatnik.class, sifra);
    }
    
    public Djelatnik post(DjelatnikDTO o){
        Odjel odjel = session.get(Odjel.class, o.odjelSifra());
        Djelatnik d = new Djelatnik();
        d.setIme(o.ime());
        d.setPrezime(o.prezime());
        d.setPlaca(o.placa());
        d.setOdjel(odjel);
        session.beginTransaction();
        session.persist(d);
        session.getTransaction().commit();
        return d;
    }
    
    public void put(int sifra, DjelatnikDTO o){
        session.beginTransaction();
        Djelatnik d = (Djelatnik) session.get(Djelatnik.class, sifra);
        d.setIme(o.ime());
        d.setPrezime(o.prezime());
        d.setPlaca(o.placa());
        Odjel odjel = session.get(Odjel.class, o.odjelSifra());
        d.setOdjel(odjel);
        session.persist(d);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Djelatnik.class, sifra));
        session.getTransaction().commit();
    }
    
    public Double prosjecnaPlacaOdjela(String nazivOdjela){
        session.beginTransaction();
        Double prosjek = session.createQuery(
                "select avg(d.placa) "
                        + "from djelatnik d "
                        + "where d.odjel.nazivOdjela = :nazivOdjela", Double.class
                )
                .setParameter("nazivOdjela", nazivOdjela)
                .uniqueResult();
        session.getTransaction().commit();
        return prosjek;
    }
    
    public List<Object[]> getBrojDjelatnikaPoLokaciji(){
        session.beginTransaction();
        List<Object[]> rezultat = session.createQuery(
            "select d.odjel.lokacijaOdjela, count(d) " +
            "from djelatnik d " +
            "group by d.odjel.lokacijaOdjela", Object[].class)
            .list();
        session.getTransaction().commit();
        return rezultat;
    }
    
    public List<Djelatnik> getDjelatniciSaNajvisomPlacom(){
        BigDecimal maksPlaca = session.createQuery(
                "select max(d.placa) from djelatnik d", BigDecimal.class)
                .uniqueResult();
        
        if (maksPlaca == null) {
            return List.of();
        }
        
        return session.createQuery(
                "from djelatnik d where d.placa = :maksPlaca", Djelatnik.class)
                .setParameter("maksPlaca", maksPlaca)
                .list();
    }
    
    public void masovnoDodavanje(int broj){
        Djelatnik d;
        Faker f = new Faker();
        int maksOdjelaSifra = 5;
        BigDecimal placa = BigDecimal.valueOf(f.number().randomDouble(2, 1200, 1800));
        Date datumAzuriranja = new Date();
        session.beginTransaction();
        for (int i = 0; i < broj; i++) {
            int sifraOdjela = f.number().numberBetween(1, maksOdjelaSifra);
            Odjel o = session.get(Odjel.class, sifraOdjela);
            d = new Djelatnik();
            d.setIme(f.name().firstName());
            d.setPrezime(f.name().lastName());
            d.setPlaca(placa);
            d.setDatumAzuriranja(datumAzuriranja);
            d.setOdjel(o);
            session.persist(d);
        }
        session.getTransaction().commit();
    }
}
