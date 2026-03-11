package com.sentura.countries_api.Service;

import com.sentura.countries_api.Model.Country;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private List<Country> cachedCountries = new ArrayList<>();
    private long lastFetchTime = 0;

    private static final long CACHE_DURATION = 10 * 60 * 1000;

    public List<Country> getCountries() {

        long currentTime = System.currentTimeMillis();

        if (cachedCountries.isEmpty() || (currentTime - lastFetchTime) > CACHE_DURATION) {

            RestTemplate restTemplate = new RestTemplate();

            Object[] response = restTemplate.getForObject(
                    "https://restcountries.com/v3.1/all",
                    Object[].class
            );

            List<Country> result = new ArrayList<>();

            for (Object obj : response) {

                Map countryMap = (Map) obj;

                Map nameMap = (Map) countryMap.get("name");

                String name = (String) nameMap.get("common");

                List capitalList = (List) countryMap.get("capital");
                String capital = capitalList != null ? capitalList.get(0).toString() : "N/A";

                String region = (String) countryMap.get("region");

                int population = (int) countryMap.get("population");

                Map flagMap = (Map) countryMap.get("flags");
                String flag = (String) flagMap.get("png");

                result.add(new Country(name, capital, region, population, flag));
            }

            cachedCountries = result;
            lastFetchTime = currentTime;
        }

        return cachedCountries;
    }

    public List<Country> searchCountries(String keyword) {

        return getCountries().stream()
                .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
