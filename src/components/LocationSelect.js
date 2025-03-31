import React from "react";

function LocationSelect(props) {
    return (
        <div>
            {props.data.length > 0 && (
                <select onChange={(e) => props.handleChoice(e.target.value)}>
                    <option value="">Select airport</option>
                    {/* Placeholder */}
                    {props.data.map((location) => (
                        <option key={location.iataCode} value={location.iataCode}>
                            {location.name + " (" + location.iataCode + ")"}
                        </option>
                    ))}
                </select>
            )}
        </div>
    );

}

export default LocationSelect;