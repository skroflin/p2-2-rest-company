/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author svenk
 */
@Entity(name = "djelatnik")
public class Djelatnik extends Osoba{
    @Column(columnDefinition = "float", nullable = false)
    private BigDecimal placa;
    @Column(name = "datum_azuriranja", nullable = false)
    private Date datumAzuriranja;
    @ManyToOne
    private Odjel odjel;

    public Djelatnik(BigDecimal placa, Date datumAzuriranja, Odjel odjel) {
        this.placa = placa;
        this.datumAzuriranja = datumAzuriranja;
        this.odjel = odjel;
    }

    public BigDecimal getPlaca() {
        return placa;
    }

    public void setPlaca(BigDecimal placa) {
        this.placa = placa;
    }

    public Date getDatumAzuriranja() {
        return datumAzuriranja;
    }

    public void setDatumAzuriranja(Date datumAzuriranja) {
        this.datumAzuriranja = datumAzuriranja;
    }

    public Odjel getOdjel() {
        return odjel;
    }

    public void setOdjel(Odjel odjel) {
        this.odjel = odjel;
    }
    
    
}
