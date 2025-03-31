package com.example.flightplanningweb.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSeatMapGenerator {

    public class DeckConfiguration {
        public int width;
        public int length;
        public int startSeatRow;
        public int endSeatRow;
        public DeckConfiguration(){}
    }
    public class Deck {
        public String deckType;
        public DeckConfiguration deckConfiguration;
        public Seat[] seats;
        public Deck(){}
    }
    public class Coordinates {
        public int x;
        public int y;
        public Coordinates(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    public class Seat {
        public String number;
        public ArrayList<String> characteristicCodes;
        public Coordinates coordinates;
        public boolean available;
        public Seat(){
            characteristicCodes = new ArrayList<String>();
        }
    }

    public class MySeatMap {
        public String aircraftCode;
        public Deck[] decks;
        public MySeatMap(){}
    }

    public MySeatMap generateRandomSeatMap(String aircraftCode, int numberOfBookableSeats) {
        Random random = new Random();
        int bookableSeats = numberOfBookableSeats;
        MySeatMap mySeatMap = new MySeatMap();
        mySeatMap.aircraftCode = aircraftCode;
        mySeatMap.decks = new Deck[1];
        Deck deck = new Deck();
        mySeatMap.decks[0] = deck;
        deck.deckType = "MAIN";
        deck.deckConfiguration = new DeckConfiguration();
        deck.deckConfiguration.length = 24;
        deck.deckConfiguration.width = 7;
        deck.deckConfiguration.startSeatRow = 0;
        deck.deckConfiguration.endSeatRow = 23;

        int totalRowsOfSeats = 20;
        String[] seatPositions = {"A", "B", "C", "D", "E", "F"};
        Seat[] seats = new Seat[totalRowsOfSeats * seatPositions.length];
        for (int row = 0; row < totalRowsOfSeats; row++) {
            for (int pos = 0; pos < seatPositions.length; pos++) {
                Seat seat = new Seat();
                if (pos == 0 || pos == seatPositions.length - 1) seat.characteristicCodes.add("W");
                if (row > 0 && row < 5) seat.characteristicCodes.add("L");
                if (row == 0 || row == 10)
                {
                    seat.characteristicCodes.add("L");
                    seat.characteristicCodes.add("E");
                }
                if (row == totalRowsOfSeats - 1) seat.characteristicCodes.add("E");
                seat.number = (row + 1) + seatPositions[pos];
                seat.coordinates = new Coordinates(pos <= 2 ? pos : pos + 1, row);
                seat.available = bookableSeats > 0 && random.nextBoolean();
                if (seat.available) --bookableSeats;
                seats[row * seatPositions.length + pos] = seat;
            }
        }
        deck.seats = seats;

        return mySeatMap;
    }
}
