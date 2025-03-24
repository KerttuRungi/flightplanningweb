package com.example.flightplanningweb.services;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum AmadeusConnect {
    INSTANCE;
    private final Amadeus amadeus;

    AmadeusConnect() {
        this.amadeus = Amadeus
                .builder("0v1jMdAARGqMmTMWdF31L1P8QpMjFYGk", "3AS38cLOGNbbtgkC")
                .build();
    }

    public Location[] location(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", Locations.AIRPORT));
    }

    public FlightOfferSearch[] flights(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter apiFormatter = DateTimeFormatter.ISO_LOCAL_DATE;


        String formattedDepartDate = LocalDate.parse(departDate, inputFormatter).format(apiFormatter);
        String formattedReturnDate = LocalDate.parse(returnDate, inputFormatter).format(apiFormatter);
        return amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", formattedDepartDate)
                        .and("returnDate", formattedReturnDate)
                        .and("adults", adults)
                        .and("max", 3));
    }

    public FlightPrice confirm(FlightOfferSearch offer) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.pricing.post(offer);
    }
}

