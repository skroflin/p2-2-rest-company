/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.Odjel;
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
        Odjel odjel = new Odjel(o.naziv(), o.lokacija(), o.tvrtka());
        session.beginTransaction();
        session.persist(odjel);
        session.getTransaction().commit();
        return odjel;
    }
    
    public void put(int sifra, OdjelDTO o){
        session.beginTransaction();
        Odjel od = (Odjel) session.get(Odjel.class, sifra);
        od.setNazivOdjela(o.naziv());
        od.setLokacijaOdjela(o.lokacija());
        od.setTvrtka(o.tvrtka());
        session.persist(od);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Odjel.class, sifra));
        session.getTransaction().commit();
    }
}
