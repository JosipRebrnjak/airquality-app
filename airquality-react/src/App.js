import React from "react";
import Header from "./components/Header";
import MrezeList from "./components/MrezeList";
import "./App.css";

function App() {
  return (
    <div>
      <Header />
      <main style={{ padding: "20px" }}>
        <MrezeList />
      </main>
    </div>
  );
}

export default App;
