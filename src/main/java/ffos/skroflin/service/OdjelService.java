/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import com.github.javafaker.Faker;
import ffos.skroflin.model.Djelatnik;
import ffos.skroflin.model.Odjel;
import ffos.skroflin.model.Tvrtka;
import ffos.skroflin.model.dto.OdjelDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class OdjelService extends MainService{
    public List<Odjel> getAll(){
        return session.createQuery("from odjel", Odjel.class).list();
    }
    
    public Odjel getBySifra(int sifra){
        return session.get(Odjel.class, sifra);
    }
    
    public Odjel post(OdjelDTO o){
        session.beginTransaction();
        Tvrtka tvrtka = session.get(Tvrtka.class, o.tvrtkaSifra());
        Odjel odjel = new Odjel();
        odjel.setNazivOdjela(o.naziv());
        odjel.setLokacijaOdjela(o.lokacija());
        odjel.setTvrtka(tvrtka);
        session.persist(odjel);
        session.getTransaction().commit();
        return odjel;
    }
    
    public void put(int sifra, OdjelDTO o){
        session.beginTransaction();
        Odjel od = (Odjel) session.get(Odjel.class, sifra);
        Tvrtka tvrtka = session.get(Tvrtka.class, o.tvrtkaSifra());
        od.setNazivOdjela(o.naziv());
        od.setLokacijaOdjela(o.lokacija());
        od.setTvrtka(tvrtka);
        session.persist(od);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Odjel.class, sifra));
        session.getTransaction().commit();
    }
    
    public boolean isBrisanje(int sifra){
        List<Djelatnik> djelatnici = session.createQuery("from djelatnik d join d.odjel_sifra = :sifra", Djelatnik.class)
                .setParameter("sifra", sifra)
                .list();
        
        return djelatnici == null || djelatnici.isEmpty();
    }
    
    public List<Odjel> getByTvrtka(int sifraTvrtke){
        session.beginTransaction();
        List<Odjel> odjeli = session.createQuery(
                "from odjel o where o.tvrtka.sifra = :sifraTvrtke", Odjel.class)
                .setParameter("sifraTvrtke", sifraTvrtke)
                .list();
        session.getTransaction().commit();
        return odjeli;
    }
    
    public void masovnoDodavanje(int broj){
        Odjel o;
        Faker f = new Faker();
        int maksTvrtkaSifra = 5;
        session.beginTransaction();
        for (int i = 0; i < broj; i++) {
            int sifraTvrtke = f.number().numberBetween(1, maksTvrtkaSifra + 1);
            Tvrtka t = session.get(Tvrtka.class, sifraTvrtke);
            o = new Odjel(f.company().industry(), f.address().cityName() + " " + f.address().countryCode(), t);
            session.persist(o);
        }
        session.getTransaction().commit();
    }
}
