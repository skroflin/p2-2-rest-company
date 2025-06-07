/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 *
 * @author svenk
 */
@Entity(name = "tvrtka")
public class Tvrtka extends Entitet{
    @Column(name = "naziv", nullable = false)
    private String nazivTvrtke;
    @Column(name = "lokacija", nullable = false)
    private String lokacijaTvrtke;

    public Tvrtka(String nazivTvrtke, String lokacijaTvrtke) {
        this.nazivTvrtke = nazivTvrtke;
        this.lokacijaTvrtke = lokacijaTvrtke;
    }

    public Tvrtka() {
    }

    public String getNazivTvrtke() {
        return nazivTvrtke;
    }

    public void setNazivTvrtke(String nazivTvrtke) {
        this.nazivTvrtke = nazivTvrtke;
    }

    public String getLokacijaTvrtke() {
        return lokacijaTvrtke;
    }

    public void setLokacijaTvrtke(String lokacijaTvrtke) {
        this.lokacijaTvrtke = lokacijaTvrtke;
    }
    
    
}
