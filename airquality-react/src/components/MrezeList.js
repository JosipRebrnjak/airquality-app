import React, { useEffect, useState } from "react";
import PostajeList from "./PostajeList";

function MrezeList() {
    const [networks, setNetworks] = useState([]);
    const [selectedNetwork, setSelectedNetwork] = useState(null);
  
    useEffect(() => {
      fetch("/api/networks") // imamo postavljen proxy u package.json
        .then(res => res.json())
        .then(data => setNetworks(data))
        .catch(err => console.error("Greška pri dohvat mreža:", err));
    }, []);
  
    if (selectedNetwork) {
      return (
        <PostajeList mrezaNaziv={selectedNetwork} onBack={() => setSelectedNetwork(null)} />
      );
    }
  
    return (
      <ul>
        {networks.map(n => (
          <li key={n.naziv} style={{ cursor: "pointer" }} onClick={() => setSelectedNetwork(n.naziv)}>
            {n.naziv}
          </li>
        ))}
      </ul>
    );
  }
  
export default MrezeList;

