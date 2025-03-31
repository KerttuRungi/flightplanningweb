import React, {useState} from "react";

function SeatMap(props) {
    const [seatMap, setSeatMap] = useState();
    const [selectedSeats, setSelectedSeats] = useState([]);
    const [filter, setFilter] = useState('all');

    function submit(event, props) {
        event.preventDefault();
        fetch("/api/randomSeatmap",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }/*,
                body: JSON.stringify(props.flight)*/
            }).then((response) => response.json())
            .then((json) => {
                setSeatMap(json);
            });

    }

    function getSeatType(seat) {
        if (seat.characteristicCodes.includes("E")) return "bg-yellow-500"
        if (seat.characteristicCodes.includes("L")) return "bg-blue-500"
        if (seat.characteristicCodes.includes("E")) return "bg-green-500"
        return "bg-gray-300"
    }

    function drawSeatmap() {
        if (!seatMap) return null;

        const seats = seatMap.decks[0].seats;
        const width = 7; // 7 columns: 6 seats + aisle
        const length = 20; // 20 rows (as defined in your backend)

        // Create a 2D grid (20 rows x 7 columns)
        let grid = Array.from({length}, () => Array(width).fill(null));

        // Place seats in their respective grid positions
        seats.forEach(seat => {
            const seatRow = seat.coordinates.y; // Row (0-indexed)
            const seatCol = seat.coordinates.x <= 2 ? seat.coordinates.x : seat.coordinates.x - 1; // Column (A-F), adjust for aisle between C and D
            grid[seatCol][seatRow] = seat;
        });

        return grid.map((row, rowIndex) => (
            <div key={rowIndex} style={{display: "flex", justifyContent: "center", gap: "8px", marginBottom: "5px"}}>
                {/* Render the seat row */}
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
                                backgroundColor: seat.available ? "green" : "red", // Available seats = green, unavailable = red
                                color: "white",
                                borderRadius: "5px"
                            }}
                        >
                            {seat.number}
                        </div>
                    ) : (
                        <div key={`aisle-${rowIndex}-${colIndex}`} style={{width: "40px"}}></div> // Empty aisle space
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
                    <p>{seatMap.decks[0].seats.length} seats</p><br/>
                </>
            }
            {seatMap && drawSeatmap()}
        </div>
    );
}

export default SeatMap;