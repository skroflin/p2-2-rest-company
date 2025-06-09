/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import com.github.javafaker.Faker;
import ffos.skroflin.model.Odjel;
import ffos.skroflin.model.Tvrtka;
import ffos.skroflin.model.dto.TvrtkaDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class TvrtkaService extends MainService{
    public List<Tvrtka> getAll(){
        return session.createQuery("from tvrtka", Tvrtka.class).list();
    }
    
    public Tvrtka getBySifra(int sifra){
        return session.get(Tvrtka.class, sifra);
    }
    
    public List<Odjel> getOdjele(int sifra){
        return session.createQuery("from odjel o where o.tvrtka.sifra =:sifra", Odjel.class)
                .setParameter("sifra", sifra)
                .list();
    }
    
    public Tvrtka post(TvrtkaDTO o){
        Tvrtka tvrtka = new Tvrtka(o.naziv(), o.lokacija());
        session.beginTransaction();
        session.persist(tvrtka);
        session.getTransaction().commit();
        return tvrtka;
    }
    
    public void put(int sifra, TvrtkaDTO o){
        session.beginTransaction();
        Tvrtka t = (Tvrtka) session.get(Tvrtka.class, sifra);
        t.setNazivTvrtke(o.naziv());
        t.setLokacijaTvrtke(o.lokacija());
        session.persist(t);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Tvrtka.class, sifra));
        session.getTransaction().commit();
    }
    
    public void masovnoDodavanje(int broj){
        Tvrtka t;
        Faker f = new Faker();
        session.beginTransaction();
        for (int i = 0; i < broj; i++) {
            t = new Tvrtka(f.company().name(), f.country().capital());
            session.persist(t);
        }
        session.getTransaction().commit();
    }
}
