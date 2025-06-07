/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import ffos.skroflin.model.Tvrtka;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author svenk
 */
public record OdjelDTO(
        @Schema(example = "Development-Razvoj") String naziv,
        @Schema(example = "Osijek") String lokacija,
        @Schema(example = "1") Tvrtka tvrtka
        ) {
    
}
