/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.Djelatnik;
import ffos.skroflin.model.dto.DjelatnikDTO;
import ffos.skroflin.service.DjelatnikService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenk
 */

@Tag(name = "Djelatnik", description = "Dostupne rute za entitet tvrtka. Sve CRUD funkcionalnosti - GET, POST, PUT i DELETE.")
@RestController
@RequestMapping("/api/skroflin/djelatnik")
public class DjelatnikController {
    private final DjelatnikService djelatnikService;

    public DjelatnikController(DjelatnikService djelatnikService) {
        this.djelatnikService = djelatnikService;
    }
    
    @Operation(
            summary = "Dohvaća sve djelatnike", tags = {"get", "djelatnik"},
            description = "Dohvaća sve djelatnike sa svim svojim pripadajućim podacima."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Djelatnik.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(djelatnikService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća djelatnika po šifri",
            description = "Dohvaća djelatnika po danoj šifri sa svim svojim pripadajućim podacima. "
            + "Ukoliko ne postoji djelatnik za danu šifru vraća prazan odgovor",
            tags = {"djelatnik", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ djelatnika u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Djelatnik.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji profesor za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule:" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Djelatnik d = djelatnikService.getBySifra(sifra);
            if (d == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(d, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira novog djelatnika",
            tags = {"post", "djelatnik"},
            description = "Kreira novog djelatnika. Ime, prezime i plaća djelatnika je obavezno!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = Djelatnik.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji ime, prezime ili plaća)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) DjelatnikDTO dto
    ){
        try {
            if(dto == null){
                return new ResponseEntity<>("Podaci nisu primljeni" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            
            if (dto.ime() == null || dto.ime().isEmpty()) {
                return new ResponseEntity<>("Ime djelatnika obavezno" + " " + dto.ime(), HttpStatus.BAD_REQUEST);
            }
            
            if (dto.prezime() == null || dto.prezime().isEmpty()) {
                return new ResponseEntity<>("Prezime djelatnika obavezno" + " " + dto.prezime(), HttpStatus.BAD_REQUEST);
            }
            
            if (dto.placa() == null) {
                return new ResponseEntity<>("Plaća djelatnika obavezno" + " " + dto.placa(), HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(djelatnikService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
