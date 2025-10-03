import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  const [imageFile, setImageFile] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => {
    setImageFile(e.target.files[0]);
    setResult(null);
  };

  const scanImage = async () => {
    if (!imageFile) {
      alert("Por favor selecciona una imagen.");
      return;
    }

    const reader = new FileReader();
    reader.onload = async (event) => {
      const base64Image = event.target.result.split(",")[1];

      setLoading(true);
      try {
        const response = await fetch("http://localhost:8080/scan", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ image: base64Image, userId: 1 }),
        });

        if (!response.ok) throw new Error(`Error ${response.status}`);

        const data = await response.json();
        setResult(data);
      } catch (err) {
        setResult({ error: err.message });
      } finally {
        setLoading(false);
      }
    };
    reader.readAsDataURL(imageFile);
  };

  return (
    <div
      className="d-flex flex-column justify-content-center align-items-center min-vh-100 bg-light"
      style={{ padding: "20px" }}
    >
      {/* Logo */}
      <div className="mb-4 text-center">
        <img src="/logo.png" alt="Logo" style={{ height: "100px" }} />
      </div>

      {/* Card central */}
      <div className="card shadow-lg p-4" style={{ maxWidth: "450px", width: "100%" }}>
        <h2 className="text-success text-center mb-3">Scan de Imagen</h2>
        <p className="text-center text-muted">Selecciona una imagen para analizarla.</p>

        <input
          type="file"
          accept="image/*"
          className="form-control mb-3"
          onChange={handleFileChange}
        />

        <button
          className="btn btn-success w-100 mb-3"
          onClick={scanImage}
          disabled={loading}
        >
          {loading ? "Escaneando..." : "Scan"}
        </button>

        {imageFile && (
          <div className="mb-3 text-center">
            <img
              src={URL.createObjectURL(imageFile)}
              alt="Preview"
              style={{ maxHeight: "200px" }}
              className="img-thumbnail"
            />
          </div>
        )}

        {result && (
          <pre
            className="bg-light p-3 border rounded"
            style={{ maxHeight: "200px", overflowY: "auto" }}
          >
            {JSON.stringify(result, null, 2)}
          </pre>
        )}
      </div>

      <p className="mt-3 text-muted text-center">
        Hecho con ❤️ usando React + Bootstrap
      </p>
    </div>
  );
}

export default App;
