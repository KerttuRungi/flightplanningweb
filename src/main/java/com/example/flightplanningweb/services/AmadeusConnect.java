package com.example.flightplanningweb.services;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightPrice;
import com.amadeus.resources.SeatMap;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

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
        return amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("returnDate", returnDate)
                        .and("adults", adults)
                        .and("max", 3));
    }

    public FlightPrice confirm(FlightOfferSearch offer) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.pricing.post(offer);
    }

    public SeatMap[] seatmap(FlightOfferSearch offer) throws ResponseException {
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(offer).getAsJsonObject();
        return amadeus.shopping.seatMaps.post(json);
    }
}

