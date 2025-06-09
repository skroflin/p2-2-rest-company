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
import java.math.BigDecimal;
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
    
    @Operation(
            summary = "Mijenja djelatnika",
            tags = {"put", "djelatnika"},
            description = "Mijenja podatke djelatnika. Ime, prezime i plaća djelatnika je obavezno!",
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ djelatnika u bazi podataka, mora biti veći od nula",
                        example = "2"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promjenjeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji ime, prezime ili plaća.)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/put")
    public ResponseEntity<String> put(
            @RequestParam int sifra,
            @RequestBody (required = true) DjelatnikDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Podaci nisu primljeni" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Djelatnik d = djelatnikService.getBySifra(sifra);
            if (d == null) {
                return new ResponseEntity<>("Ne postoji djelatnik s navedenom šifrom" +  " " + sifra, HttpStatus.BAD_REQUEST);
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
            
            djelatnikService.put(sifra, dto);
            return new ResponseEntity<>("Promijenjeno!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Briše djelatnika po šifri",
            description = "Briše djelatnika i sve njegove pripadajuće podatke sa sobom. ",
            tags = {"delete", "djelatnik"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ djelatnika u bazi podataka, mora biti veći od nula",
                        example = "2"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obrisano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula ili djelatnik koji se želi brisati ne postoji ", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
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
            
            Djelatnik d = djelatnikService.getBySifra(sifra);
            if (d == null) {
                return new ResponseEntity<>("Ne postoji djelatnik s navedenom šifrom" +  " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            djelatnikService.delete(sifra);
            return new ResponseEntity<>("Obrisano!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća prosječnu plaću za odjel",
            description = "Vraća prosječnu plaću za djelatnike određenog odjela prema nazivu odjela.",
            parameters = {
                @Parameter(
                        name = "nazivOdjela",
                        allowEmptyValue = false,
                        required = true,
                        description = "Naziv odjela za koji se izračunava prosječna plaća",
                        example = "Development"
                )
            }
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prosječna plaća za zadani odjel", content = @Content(schema = @Schema(implementation = BigDecimal.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Nije unesen naziv odjela ili odjel nema djelatnika", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/prosjecnaPlacaOdjela")
    public ResponseEntity<?> prosjecnaPlacaOdjela(
            @RequestParam String nazivOdjela
    ){
        try {
            if (nazivOdjela == null || nazivOdjela.isEmpty()) {
                return new ResponseEntity<>("Naziv odjela je obavezan!" + " " + nazivOdjela, HttpStatus.BAD_REQUEST);
            }
            
            BigDecimal prosjecnaPlaca = djelatnikService.prosjecnaPlacaOdjela(nazivOdjela);
            if (prosjecnaPlaca == null) {
                return new ResponseEntity<>("Djelatnici nemaju plaću iz navedenog odjela" + " " + nazivOdjela, HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(prosjecnaPlaca, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
    }
    
    @Operation(
            summary = "Dohvaća broj djelatnika po lokaciji",
            description = "Vraća listu lokacija i broj djelatnika koji rade na svakoj lokaciji. "
            + "Ako neka lokacija nema djelatnika, neće biti uključena u rezultat."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Popis lokacija i broj djelatnika",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera",
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getBrojDjelatnikaPoLokaciji")
    public ResponseEntity<?> getBrojDjelatnikaPoLokaciji(){
        try {
            List<Object[]> rezultati = djelatnikService.getBrojDjelatnikaPoLokaciji();
            return new ResponseEntity<>(rezultati, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća djelatnike s najvišom plaćom",
            description = "Vraća sve djelatnike koji imaju najvišu plaću u sustavu. "
            + "Moguće je da više djelatnika dijeli isti iznos najviše plaće."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Popis djelatnika s najvišom plaćom",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Nema djelatnika u sustavu",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera",
                content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getDjelatniciSaNajvisomPlacom")
    public ResponseEntity<?> getDjelatniciSaNajvisomPlacom(){
        try {
            List<Djelatnik> djelatnici = djelatnikService.getDjelatniciSaNajvisomPlacom();
            if (djelatnici == null || djelatnici.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(djelatnici, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira nove djelatnike sa slučajni podacima",
            tags = {"post", "djelatnik"},
            description = "Kreira onoliko djelatnika koliko primi kroz parametar sa slučajnim podacima koristeći Faker biblioteku", parameters = {
                @Parameter(
                        name = "broj",
                        allowEmptyValue = false,
                        required = true,
                        description = "Broj djelatnika koji će biti kreirani",
                        example = "10"
                )})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kreirano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen broj koliko djelatnika treba dodati)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/masovnoDodavanje")
    public ResponseEntity masovnoDodavanje(
            @RequestParam int broj
    ){
        try {
            if (broj <= 0) {
                return new ResponseEntity<>("Broj mora biti veći od nule" + " " + broj, HttpStatus.BAD_REQUEST);
            }
            
            djelatnikService.masovnoDodavanje(broj);
            return new ResponseEntity<>("Uspješno dodano" + " " + broj + " " + "djelatnika", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
