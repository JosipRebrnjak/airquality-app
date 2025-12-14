import React, { useState } from "react";

const PostajaEdit = ({ postaja, onBack, onUpdate }) => {
  const [editablePostaja, setEditablePostaja] = useState(postaja);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);

    fetch(`/api/stations/${encodeURIComponent(editablePostaja.naziv)}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nazivEng: editablePostaja.nazivEng,
        aktivna: editablePostaja.aktivna,
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error("Update nije uspio");
        return res.json(); 
      })
      .then(() => {
        onUpdate(); 
        onBack();  
      })
      .catch(err => {
        console.error(err);
        setError("Greška pri ažuriranju postaje");
      })
      .finally(() => setLoading(false));
  };

  return (
    <div>
      <h3>Uredi postaju: {editablePostaja.naziv}</h3>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Naziv (eng): </label>
          <input
            type="text"
            value={editablePostaja.nazivEng}
            onChange={(e) =>
              setEditablePostaja({ ...editablePostaja, nazivEng: e.target.value })
            }
          />
        </div>
        <div>
          <label>
            <input
              type="checkbox"
              checked={editablePostaja.aktivna}
              onChange={(e) =>
                setEditablePostaja({ ...editablePostaja, aktivna: e.target.checked })
              }
            />
            Aktivna
          </label>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Spremanje..." : "Spremi"}
        </button>
        <button type="button" onClick={onBack}>Nazad</button>
      </form>
    </div>
  );
};

export default PostajaEdit;
