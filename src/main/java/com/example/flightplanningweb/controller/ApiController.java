package com.example.flightplanningweb.controller;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.SeatMap;
import com.example.flightplanningweb.services.AmadeusConnect;
import com.amadeus.resources.Location;
import com.example.flightplanningweb.services.RandomSeatMapGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.amadeus.resources.FlightOfferSearch;

@RestController
@RequestMapping(value="/api")
public class ApiController {
    @GetMapping("/locations")
    public Location[] locations(@RequestParam(required=true) String keyword) throws ResponseException {
        return AmadeusConnect.INSTANCE.location(keyword);
    }
    @GetMapping("/flights")
    public FlightOfferSearch[] flights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departDate,
            @RequestParam String adults,
            @RequestParam(required = false) String returnDate
    ) throws ResponseException {
        return AmadeusConnect.INSTANCE.flights(origin, destination, departDate, adults, returnDate);
    }
    @PostMapping("/seatmap")
    public SeatMap seatmap(@RequestBody(required=true) FlightOfferSearch search) throws ResponseException {
        return AmadeusConnect.INSTANCE.seatmap(search)[0];
    }
    @PostMapping("/randomSeatmap")
    public RandomSeatMapGenerator.MySeatMap randomSeatmap(@RequestBody(required=false) FlightOfferSearch search) throws ResponseException {
        RandomSeatMapGenerator generator = new RandomSeatMapGenerator();
        return generator.generateRandomSeatMap("A320");
    }
}

