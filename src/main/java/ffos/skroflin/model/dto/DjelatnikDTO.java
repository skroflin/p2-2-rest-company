/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 *
 * @author svenk
 */
public record DjelatnikDTO(
        @Schema(example = "Sven") String ime,
        @Schema(example = "Kroflin") String prezime,
        @Schema(example = "1500,65") BigDecimal placa,
        @Schema(example = "1") int odjelSifra
        ) {
    
}
