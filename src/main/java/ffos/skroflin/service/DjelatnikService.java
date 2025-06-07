/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.Djelatnik;
import ffos.skroflin.model.Odjel;
import ffos.skroflin.model.dto.DjelatnikDTO;
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
}
