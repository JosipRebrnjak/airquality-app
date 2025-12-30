import React, { useState, useEffect, useCallback } from "react";
import PostajaEdit from "./PostajaEdit";

function PostajeList({ mrezaNaziv, onBack }) {
  const [postaje, setPostaje] = useState([]);
  const [editingPostaja, setEditingPostaja] = useState(null);


  const fetchPostaje = useCallback(() => {
    fetch(`/api/stations/by-network/${encodeURIComponent(mrezaNaziv)}`)
      .then(res => res.json())
      .then(data => setPostaje(data))
      .catch(err => console.error("Greška pri dohvat postaja:", err));
  }, [mrezaNaziv]); 

  useEffect(() => {
    fetchPostaje();
  }, [fetchPostaje]); 

  const handleUpdate = () => {
    fetchPostaje(); 
  };

  if (editingPostaja) {
    return (
      <PostajaEdit
        postaja={editingPostaja} 
        mrezaNaziv={mrezaNaziv} 
        onBack={() => setEditingPostaja(null)}
        onUpdate={handleUpdate}
      />
    );
  }

  return (
    <div>
      <button onClick={onBack}>Nazad</button>
      <ul>
        {postaje.map(p => (
          <li
            key={`${p.mrezaNaziv}-${p.naziv}`}
            style={{ color: p.aktivna ? "green" : "grey", cursor: "pointer" }}
            onClick={() => setEditingPostaja(p)}
          >
            {p.naziv}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PostajeList;
