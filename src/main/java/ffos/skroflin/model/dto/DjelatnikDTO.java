/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import ffos.skroflin.model.Odjel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author svenk
 */
public record DjelatnikDTO(
        @Schema(example = "Sven") String ime,
        @Schema(example = "Kroflin") String prezime,
        @Schema(example = "1500,65") BigDecimal placa,
        @Schema(example = "07.06.2025.") Date datumAzuriranja,
        @Schema(example = "1") Odjel odjel
        ) {
    
}
