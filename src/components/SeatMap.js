import React, {useState} from "react";

function SeatMap(props) {
    const [seatMap, setSeatMap] = useState();
    const [selectedSeats, setSelectedSeats] = useState([]);

    function submit(event, props) {
        event.preventDefault();
        fetch("/api/randomSeatmap",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(props.flight)
            }).then((response) => response.json())
            .then((json) => {
                setSeatMap(json);
            });
    }

    /*Uuendab valitud istmete info istmel klikkimisel*/
    function toggleSeatSelection(seat) {
        setSelectedSeats(prevSelected =>
            prevSelected.includes(seat.number)
                ? prevSelected.filter(num => num !== seat.number)
                : [...prevSelected, seat.number]
        );
    }

    /*Värvib iste vastavalt omadustele*/
    function colourUnselected(seat) {
        if (seat.available) {
            /*E - väljapääsule lähedal*/
            if (seat.characteristicCodes.includes("E")) return "rgb(40,175,82)"
            /*L - lisa jalaruum*/
            if (seat.characteristicCodes.includes("L")) return "rgb(82,250,82)"
            /*W - aknaalune*/
            if (seat.characteristicCodes.includes("W")) return "rgb(54,167,208)"
            return "rgb(103,102,102)"
        } else
            return "rgb(224, 224, 224)";
    }

    /*Joonistab istmete plaani*/
    function drawSeatmap() {
        const deck = seatMap.decks[0];
        const seats = deck.seats;
        const width = deck.deckConfiguration.width;
        const length = deck.deckConfiguration.length;

        /*istmete plaani ruudustik*/
        let grid = Array.from({length}, () => Array(width).fill(null));
        /*paigutab istmed ruudustikku*/
        seats.forEach(seat => {
            grid[seat.coordinates.y][seat.coordinates.x] = seat;
        });
        /*joonistab ruudustiku koos istmete, numbri ja omadustele vastava värviga*/
        return grid.map((row, rowIndex) => (
            <div key={rowIndex} style={{display: "flex", justifyContent: "center", gap: "8px", marginBottom: "5px"}}>
                {row.map((seat, colIndex) =>
                    seat ? (
                        <div
                            key={seat.number}
                            style={{
                                width: "40px",
                                height: "40px",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                backgroundColor: selectedSeats.includes(seat.number)
                                    ? "purple"
                                    : colourUnselected(seat),
                                color: "white",
                                borderRadius: "5px",
                                cursor: seat.available ? "pointer" : "not-allowed",
                                border: selectedSeats.includes(seat.number) ? "3px solid white" : "none"
                            }}
                            onClick={seat.available ? () => toggleSeatSelection(seat) : null}
                        >
                            {seat.number}
                        </div>
                    ) : (
                        <div key={`aisle-${rowIndex}-${colIndex}`} style={{width: "40px"}}></div>
                    )
                )}
            </div>
        ));
    }

    return (
        <div>
            {!seatMap &&
                <form onSubmit={(e) => submit(e, props)}>
                    <input type="submit"/>
                </form>
            }
            {seatMap &&
                <>
                    <p>Selected Seats: {selectedSeats.join(", ")}</p>
                </>
            }
            {seatMap && drawSeatmap()}
        </div>
    );
}

export default SeatMap;