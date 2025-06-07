/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author svenk
 */
public record TvrtkaDTO(
        @Schema(example = "ORKA d.o.o.") String naziv,
        @Schema(example = "Ulica Svetog Leopolda Bogdana Mandića 111-O") String lokacija
        ) {
    
}
