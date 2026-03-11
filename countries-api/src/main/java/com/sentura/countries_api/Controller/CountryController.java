package com.sentura.countries_api.Controller;

import com.sentura.countries_api.Model.Country;
import com.sentura.countries_api.Service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/countries")
@CrossOrigin
public class CountryController {
    private final CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Country> getCountries() {
        return service.getCountries();
    }

    @GetMapping("/search")
    public List<Country> searchCountries(@RequestParam String q) {
        return service.searchCountries(q);
    }
}
