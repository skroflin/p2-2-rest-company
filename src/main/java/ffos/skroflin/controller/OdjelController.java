/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.Odjel;
import ffos.skroflin.model.dto.OdjelDTO;
import ffos.skroflin.service.OdjelService;
import ffos.skroflin.service.TvrtkaService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenk
 */

@Tag(name = "Odjel", description = "Dostupne rute za entitet tvrtka. Sve CRUD funkcionalnosti - GET, POST, PUT i DELETE.")
@RestController
@RequestMapping("/api/skroflin/odjel")
public class OdjelController {
    private final OdjelService odjelService;

    public OdjelController(OdjelService odjelService) {
        this.odjelService = odjelService;
    }
    
    @Operation(
            summary = "Dohvaća sve odjele", tags = {"get", "odjel"},
            description = "Dohvaća sve odjele sa svim podacima"
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Odjel.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(odjelService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća odjel po šifri",
            description = "Dohvaća odjel po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji odjel za danu šifru vraća prazan odgovor",
            tags = {"odjel", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ odjela u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Odjel.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji odjel za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Odjel o = odjelService.getBySifra(sifra);
            if (o == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(o, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira novi odjel",
            tags = {"post", "odjel"},
            description = "Kreira novi odjel. Naziv i lokacija odjela obavezno!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = Odjel.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji naziv ili lokacaij)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) OdjelDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Podaci nisu primljeni" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv odjela je obavezan!" + dto.naziv(), HttpStatus.BAD_REQUEST);
            }
            
            if (dto.lokacija() == null || dto.lokacija().isEmpty()) {
                return new ResponseEntity<>("Lokacija odjela je obavezna!" + dto.lokacija(), HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(odjelService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Mijenja podatke o odjelu",
            tags = {"put", "odjel"},
            description = "Mijenja podatke o odjelu. Naziv i lokacija obavezno!",
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ odjela u bazi podataka, mora biti veći od nula",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promjenjeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji naziv ili lokacija odjela)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/put")
    public ResponseEntity<String> put(
            @RequestParam int sifra,
            @RequestBody(required = true) OdjelDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Podaci nisu primljeni" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Odjel o = odjelService.getBySifra(sifra);
            if (o == null) {
                return new ResponseEntity<>("Ne postoji odjel s navedenom šifrom:" + " " + sifra + " " + "nije promijenjeno!", HttpStatus.BAD_REQUEST);
            }
            
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv odjela obavezno!" +  " " + dto.naziv(), HttpStatus.BAD_REQUEST);
            }
            
            if (dto.lokacija() == null || dto.lokacija().isEmpty()) {
                return new ResponseEntity<>("Lokacija odjela obavezno!" + " " + dto.lokacija(), HttpStatus.BAD_REQUEST);
            }
            
            odjelService.put(sifra, dto);
            return new ResponseEntity<>("Promijenjeno", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Briše odjel po šifri",
            description = "Briše odjel koji nema ni jednog djelatnika. "
            + "Ukoliko postoji jedan ili više djelatnika na kojima je postavljen zadani odjel vraća poruku o grešci",
            tags = {"delete", "odjel"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ odjela u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula ili odjel koji se želi brisati ne postoji "
                + "ili se ne može obrisati jer ima jednog ili više djelatnika postavljeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Odjel o = odjelService.getBySifra(sifra);
            if (o == null) {
                return new ResponseEntity<>("Ne postoji odjel s navedenom šifrom" + " " + sifra + " " + ", nije moguće obrisati", HttpStatus.BAD_REQUEST);
            }
            
            if (!odjelService.isBrisanje(sifra)) {
                return new ResponseEntity<>("Odjel se ne može obrisati jer ima jedan ili više djelatnika", HttpStatus.BAD_REQUEST);
            }
            
            odjelService.delete(sifra);
            return new ResponseEntity<>("Obrisan odjel!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
