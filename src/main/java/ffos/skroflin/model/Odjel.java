/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author svenk
 */
@Entity(name = "odjel")
public class Odjel extends Entitet{
    @Column(name = "naziv", nullable = false)
    private String nazivOdjela;
    @Column(name = "lokacija", nullable = false)
    private String lokacijaOdjela;
    @ManyToOne
    @JoinColumn(name = "tvrtka_sifra")
    private Tvrtka tvrtka;

    public Odjel(String nazivOdjela, String lokacijaOdjela, Tvrtka tvrtka) {
        this.nazivOdjela = nazivOdjela;
        this.lokacijaOdjela = lokacijaOdjela;
        this.tvrtka = tvrtka;
    }

    public Odjel() {
    }

    public String getNazivOdjela() {
        return nazivOdjela;
    }

    public void setNazivOdjela(String nazivOdjela) {
        this.nazivOdjela = nazivOdjela;
    }

    public String getLokacijaOdjela() {
        return lokacijaOdjela;
    }

    public void setLokacijaOdjela(String lokacijaOdjela) {
        this.lokacijaOdjela = lokacijaOdjela;
    }

    public Tvrtka getTvrtka() {
        return tvrtka;
    }

    public void setTvrtka(Tvrtka tvrtka) {
        this.tvrtka = tvrtka;
    }
    
    
}
