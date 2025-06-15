/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.Odjel;
import ffos.skroflin.model.Tvrtka;
import ffos.skroflin.model.dto.TvrtkaDTO;
import ffos.skroflin.service.TvrtkaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@Tag(name = "Tvrtka", description = "Dostupne rute za entitet tvrtka. Sve CRUD funkcionalnosti - GET, POST, PUT i DELETE.")
@RestController
@RequestMapping("/api/skroflin/tvrtka")
public class TvrtkaController {
    private final TvrtkaService tvrtkaService;

    public TvrtkaController(TvrtkaService tvrtkaService) {
        this.tvrtkaService = tvrtkaService;
    }
    
    @Operation(
            summary = "Dohvaća sve tvrtke iz tablice tvrtka", tags = {"get", "tvrtka"},
            description = "Dohvaća sve tvrtke iz tablice tvrtka, sa svojim podacima."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Tvrtka.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(tvrtkaService.getAll(), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća tvrtku po šifri",
            description = "Dohvaća tvrtku po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji tvrtka za danu šifru vraća prazan odgovor",
            tags = {"tvrtka", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ tvrtke u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tvrtka.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji student za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Tvrtka tvrtka = tvrtkaService.getBySifra(sifra);
            if (tvrtka == null) {
                return new ResponseEntity<>("Tvrtka s navedenom šifrom:" + " " + sifra + " " + "ne postoji", HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(tvrtka, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira novu tvrtku",
            tags = {"post", "tvrtka"},
            description = "Kreira novu tvrtku - novi unos u tablicu Tvrtka. Ime/naziv i lokacija tvrtke obavezno!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = Tvrtka.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji naziv ili lokacija tvrtke)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) TvrtkaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Ime tvrtke je obavezna" + " " + dto.naziv(), HttpStatus.BAD_REQUEST);
            }
            
            if (dto.lokacija() == null || dto.lokacija().isEmpty()) {
                return new ResponseEntity<>("Lokacija tvrtke je obavezna" + " " + dto.lokacija(), HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(tvrtkaService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Mijenja podatke o tvrtki",
            tags = {"put", "tvrtka"},
            description = "Mijenja podatke o tvrtki. Naziv i lokacija tvrtke obavezno!",
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ tvrtke u bazi podataka, mora biti veći od nula!",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promjenjeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji naziv ili lokacija tvrtke)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/put")
    public ResponseEntity<String> put(
            @RequestParam int sifra,
            @RequestBody(required = true) TvrtkaDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Tvrtka t = tvrtkaService.getBySifra(sifra);
            if (t == null) {
                return new ResponseEntity<>("Ne postoji Tvrtka s navedenom šifrom" +  " " + sifra + " " + ", nije moguće promijeniti!", HttpStatus.BAD_REQUEST);
            }
            
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv tvrtke je obavezan" + " " + dto.naziv(), HttpStatus.BAD_REQUEST);
            }
            
            if (dto.lokacija() == null || dto.lokacija().isEmpty()) {
                return new ResponseEntity<>("Lokacija tvrtke je obavezna" + " " + dto.lokacija(), HttpStatus.BAD_REQUEST);
            }
            
            tvrtkaService.put(sifra, dto);
            return new ResponseEntity<>("Promijenjena tvrtka!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Briše tvrtku po šifri",
            description = "Briše tvrtku u kojoj se ne nalazi niti jednan odjel. "
            + "Ukoliko postoji jedan ili više odjela unutar jedne tvrtke vraća poruku o grešci!",
            tags = {"delete", "tvrtka"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ tvrtke u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obrisano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula ili student koji se želi brisati ne postoji "
                + "ili se ne može obrisati jer ima jedan ili više odjela postavljen", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }

            Tvrtka t = tvrtkaService.getBySifra(sifra);
            if (t == null) {
                return new ResponseEntity<>("Ne postoji tvrtka s navedenom šifrom" + " " + sifra + " " + ", nije obrisana!", HttpStatus.BAD_REQUEST);
            }

            List<Odjel> odjeli = tvrtkaService.getOdjele(sifra);
            if (odjeli != null && !odjeli.isEmpty()) {
                return new ResponseEntity<>("Tvrtka se ne može obrisati jer ime jedan ili više odjela!", HttpStatus.BAD_REQUEST);
            }

            tvrtkaService.delete(sifra);
            return new ResponseEntity<>("Tvrtka obrisana!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća odjele tvrtke po šifri",
            description = "Dohvaća listu odjela koji se nalazi unutar tvrtke po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji odjel za danu šifru vraća prazan odgovor",
            tags = {"tvrtka", "odjel"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ tvrtke u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Odjel.class)))),
        @ApiResponse(responseCode = "204", description = "Ne postoji tvrtka za danu šifru"),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getOdjele")
    public ResponseEntity getOdjele(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Tvrtka t = tvrtkaService.getBySifra(sifra);
            if (t == null) {
                return new ResponseEntity<>("Ne postoji tvrtka s navedenom šifrom" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(tvrtkaService.getOdjele(sifra), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira nove tvrtke sa slučajni podacima",
            tags = {"post", "tvrtka"},
            description = "Kreira onoliko tvrtki koliko primi kroz parametar sa slučajnim podacima koristeći Faker biblioteku", parameters = {
                @Parameter(
                        name = "broj",
                        allowEmptyValue = false,
                        required = true,
                        description = "Broj tvrtki koji će biti kreirani",
                        example = "10"
                )})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kreirano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen broj koliko odjela treba dodati)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/masovnoDodavanje")
    public ResponseEntity masovnoDodavanje(
            @RequestParam int broj
    ){
        try {
            if (broj <= 0) {
                return new ResponseEntity<>("Broj mora biti veći od nule." + " " + broj, HttpStatus.BAD_REQUEST);
            }
            
            tvrtkaService.masovnoDodavanje(broj);
            return new ResponseEntity<>("Uspješno dodan" + " " + broj + " " + "tvrtki", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
