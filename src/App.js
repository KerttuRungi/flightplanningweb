import React, {useState} from 'react'
import './App.css';
import Locate from './components/Locate';
import Flight from "./components/Flight";
import 'bootstrap/dist/css/bootstrap.min.css';
import SeatMap from "./components/SeatMap";

export default function App() {
    const [origin, setOrigin] = useState();
    const [destination, setDestination] = useState();
    const [flight, setFlight] = useState();

    return (
        <div>
            <Locate handleChoice={setDestination} display={"Departure"}/>
            <Locate handleChoice={setOrigin} display={"Destination"}/>
            {
                origin &&
                destination &&
                <Flight origin={origin} destination={destination} setFlight={setFlight}/>
            }
            {
                flight && <SeatMap flight={flight}/>
            }
        </div>
    )
}