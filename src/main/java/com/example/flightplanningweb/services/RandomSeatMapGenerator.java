package com.example.flightplanningweb.services;

import java.util.ArrayList;
import java.util.Random;

public class RandomSeatMapGenerator {

    public static class DeckConfiguration {
        public int width;
        public int length;
        public int startSeatRow;
        public int endSeatRow;
        public DeckConfiguration(){}
    }
    public static class Deck {
        public String deckType;
        public DeckConfiguration deckConfiguration;
        public Seat[] seats;
        public Deck(){}
    }
    public static class Coordinates {
        public int x;
        public int y;
        public Coordinates(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    public static class Seat {
        public String number;
        public ArrayList<String> characteristicCodes;
        public Coordinates coordinates;
        public boolean available;
        public Seat(){
            characteristicCodes = new ArrayList<>();
        }
    }

    public static class MySeatMap {
        public String aircraftCode;
        public Deck[] decks;
        public MySeatMap(){}
    }

    /*Genereerib istmete plaani, kus on 20 rida, vahekäigust vasakul ja paremal 3 istet
    * ning mitte rohkem, kui etteantud arv broneeritavat kohta.
    * @aircraftCode - lennuki mudel
    * @numberOfBookableSeats - maksimaalne broneeritavate kohtade arv*/
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
        deck.deckConfiguration.length = 20;
        deck.deckConfiguration.width = 7;
        deck.deckConfiguration.startSeatRow = 0;
        deck.deckConfiguration.endSeatRow = 19;

        int totalRowsOfSeats = 20;
        String[] seatPositions = {"A", "B", "C", "D", "E", "F"};
        Seat[] seats = new Seat[totalRowsOfSeats * seatPositions.length];
        for (int row = 0; row < totalRowsOfSeats; row++) {
            for (int pos = 0; pos < seatPositions.length; pos++) {
                Seat seat = new Seat();
                /*vasakult esimene ja paremalt viimane on aknaalused kohad*/
                if (pos == 0 || pos == seatPositions.length - 1) seat.characteristicCodes.add("W");
                /*esimesed 5 rida on suurema jalaruumiga*/
                if (row > 0 && row < 5) seat.characteristicCodes.add("L");
                /*esimene ja 11. rida on suurema jalaruumiga ning väljapääsu lähedal*/
                if (row == 0 || row == 10)
                {
                    seat.characteristicCodes.add("L");
                    seat.characteristicCodes.add("E");
                }
                /*viimane rida on väljapääsule lähedal*/
                if (row == totalRowsOfSeats - 1) seat.characteristicCodes.add("E");
                seat.number = (row + 1) + seatPositions[pos];
                seat.coordinates = new Coordinates(pos <= 2 ? pos : pos + 1, row);
                /*iste on vaba, kui broneeritavate vabade kohtade arv pole täis ja juhuslik boolean väärtus on true*/
                seat.available = bookableSeats > 0 && random.nextBoolean();
                if (seat.available) --bookableSeats;
                seats[row * seatPositions.length + pos] = seat;
            }
        }
        deck.seats = seats;

        return mySeatMap;
    }
}
