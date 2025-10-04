import React, { useState, useEffect } from 'react';
import { Toaster, toast } from 'sonner'

// URL base para el backend (se recomienda usar un proxy en un entorno de producción)
const API_BASE_URL = "http://localhost:8090";

// --- Componentes Reutilizables ---

// 1. Mensajes de error/info
const MessageBox = ({ message, type }) => {
  const baseClasses = "p-3 rounded-xl text-sm font-medium transition-opacity duration-300";
  const typeClasses = type === 'error'
    ? 'bg-red-100 text-red-700 border border-red-200'
    : type === 'success'
    ? 'bg-green-100 text-green-700 border border-green-200'
    : 'bg-yellow-100 text-yellow-700 border border-yellow-200';

  if (!message) return null;

  return (
    <div className={`${baseClasses} ${typeClasses} mt-4`}>
      {message}
    </div>
  );
};

// 2. Elemento de navegación de la Sidebar
const NavItem = ({ icon, label, active, onClick }) => (
  <div 
    onClick={onClick}
    className={`flex items-center space-x-3 p-3 rounded-lg cursor-pointer transition-colors duration-200 ${
      active ? 'bg-green-100 text-green-700 font-bold' : 'text-gray-600 hover:bg-green-50 hover:text-green-600'
    }`}
  >
    {React.cloneElement(icon, { className: 'h-6 w-6' })}
    <span>{label}</span>
  </div>
);

// 3. Resultados del escaneo
const ScanResults = ({ result }) => {
  if (!result || !Array.isArray(result.detections)) {
    return (
      <div className="mt-6">
        <h3 className="text-xl font-bold text-gray-700 border-b pb-2 mb-4">
            Error o Resultado Inesperado de la API
        </h3>
        <p className="text-red-500 mb-3">La API no devolvió el formato esperado (detections).</p>
        <pre className="bg-gray-100 p-4 rounded-xl text-xs overflow-auto max-h-60 border border-gray-200 mt-2">
          {JSON.stringify(result, null, 2)}
        </pre>
      </div>
    );
  }

  const totalPoints = result.detections.reduce((sum, d) => sum + d.points, 0);

  return (
    <div className="mt-6">
      <div className="bg-white p-6 rounded-2xl shadow-xl border-t-4 border-green-500/50">
        <h3 className="text-xl font-bold text-green-600 border-b pb-2 mb-4">{result.message}</h3>
        <p className="text-2xl font-extrabold text-gray-800 mb-4">
          Total de Puntos: <span className="text-green-500">+{totalPoints}pts</span>
        </p>
        <div className="space-y-3">
          {result.detections.map((d, index) => (
            <div key={index} className="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span className="font-semibold text-gray-700">
                {d.count} {d.count === 1 ? 'plástico' : 'plásticos'} tipo: <strong className="text-green-700">{d.type}</strong>
              </span>
              <span className="font-bold text-green-500"> (+{d.points}pts)</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};


// --- Nueva Pantalla de Administración de Cupones ---
const AddCoupon = ({ userId }) => {
  const [formData, setFormData] = useState({
    coupon_id: '',
    expiration_date: '',
    price: '', // Puntos de canje
    amount: '', // Cantidad disponible
    description: '',
    link: '',
    image: '', // URL de la imagen del cupón
    enterprise_cuit: '',
  });
  const [loading, setLoading] = useState(false);
  const [responseMessage, setResponseMessage] = useState(null);
  const [responseType, setResponseType] = useState(null);

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'number' ? parseInt(value) || '' : value,
    }));
  };

  const handleAddCoupon = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResponseMessage(null);
    setResponseType(null);

    // Validación básica
    if (!formData.description || !formData.price || !formData.expiration_date) {
      setResponseMessage("Todos los campos marcados con * son obligatorios.");
      setResponseType('error');
      setLoading(false);
      return;
    }

    try {
      // Prepara los datos para que coincidan con el CouponModel de Java
      const payload = {
        coupon_id: parseInt(formData.coupon_id) || 0, // Asume que el backend asigna el ID si es 0
        expiration_date: formData.expiration_date,
        price: parseInt(formData.price),
        amount: parseInt(formData.amount) || 1,
        description: formData.description,
        link: formData.link || '#',
        image: formData.image || 'https://placehold.co/100x100/A8E6CF/00A651?text=CUPON',
        enterprise_cuit: formData.enterprise_cuit,
      };

      const response = await fetch(`${API_BASE_URL}/admin/coupon`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        // Aquí se envía el payload al backend
        body: JSON.stringify(payload), 
      });

      if (!response.ok) {
        throw new Error(`Error ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      setResponseMessage(`✅ Cupón añadido con éxito. ID de respuesta: ${data.coupon_id || 'N/A'}`);
      setResponseType('success');
      
      // Opcional: limpiar el formulario
      setFormData({
        coupon_id: '',
        expiration_date: '',
        price: '',
        amount: '',
        description: '',
        link: '',
        image: '',
        enterprise_cuit: '',
      });

    } catch (err) {
      setResponseMessage(`❌ Falló la conexión o la API. Asegúrate de que el backend esté corriendo en ${API_BASE_URL}. Detalle: ${err.message}`);
      setResponseType('error');
    } finally {
      setLoading(false);
    }
  };
  
  // Array de campos para renderizado dinámico
  const formFields = [
    { name: 'description', label: 'Descripción del Cupón *', type: 'textarea', required: true, span: 2 },
    { name: 'price', label: 'Puntos de Canje *', type: 'number', required: true, span: 1 },
    { name: 'amount', label: 'Cantidad Disponible *', type: 'number', required: true, span: 1 },
    { name: 'expiration_date', label: 'Fecha de Expiración *', type: 'date', required: true, span: 1 },
    { name: 'image', label: 'URL de la Imagen', type: 'url', required: false, span: 3 },
    { name: 'link', label: 'Link de Redirección', type: 'url', required: false, span: 2 },
    { name: 'enterprise_cuit', label: 'CUIT de la Empresa', type: 'text', required: false, span: 3 },
    { name: 'coupon_id', label: 'ID (Opcional, si es pre-asignado)', type: 'number', required: false, span: 2 },
  ];

  return (
    <div className="bg-white p-6 rounded-2xl shadow-lg border-t-4 border-green-500/50">
      <h2 className="text-2xl font-bold text-green-700 mb-4 border-b pb-2">
        Crear Nuevo Cupón de Canje
      </h2>
      
      <form onSubmit={handleAddCoupon} className="space-y-6">
        <MessageBox message={responseMessage} type={responseType} />
        
        <div className="grid grid-cols-1 md:grid-cols-5 gap-6">
          
          {formFields.map((field) => (
            <div key={field.name} className={`md:col-span-${field.span}`}>
              <label htmlFor={field.name} className="block text-sm font-medium text-gray-700 mb-1">
                {field.label}
              </label>
              {field.type === 'textarea' ? (
                <textarea
                  id={field.name}
                  name={field.name}
                  rows="3"
                  required={field.required}
                  value={formData[field.name]}
                  onChange={handleChange}
                  placeholder={field.name === 'description' ? 'Ej: 15% de descuento en café orgánico' : ''}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition duration-150 shadow-sm resize-none"
                />
              ) : (
                <input
                  id={field.name}
                  name={field.name}
                  type={field.type}
                  required={field.required}
                  value={formData[field.name]}
                  onChange={handleChange}
                  placeholder={field.label.split(' ')[0]}
                  min={field.type === 'number' ? 0 : undefined}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition duration-150 shadow-sm"
                />
              )}
            </div>
          ))}
        </div>
        
        <div className="pt-4 border-t">
          <button
            type="submit"
            disabled={loading}
            className={`px-8 py-3 rounded-xl text-white font-bold shadow-lg transition duration-200 ${
              loading ? 'bg-green-400 cursor-not-allowed' : 'bg-green-500 hover:bg-green-600'
            }`}
          >
            {loading ? 'Guardando Cupón...' : 'GUARDAR NUEVO CUPÓN'}
          </button>
        </div>
      </form>
    </div>
  );
};

// --- Componente Principal APP ---

const App = () => {
  // Estado para la navegación: 'scan' (default) o 'add_coupon'
  const [view, setView] = useState('scan'); 
  
  // Estado para el escaner de plásticos
  const [imageFile, setImageFile] = useState(null);
  const [scanResult, setScanResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  // Mock User ID (debería venir de una autenticación real)
  const MOCK_USER_ID = 1; 

  const handleFileChange = (e) => {
    setImageFile(e.target.files[0]);
    setScanResult(null);
    setError(null);
  };

  const handleScan = async () => {
    if (!imageFile) {
      setError("❌ Por favor, selecciona una imagen para comenzar el escaneo.");
      return;
    }

    const reader = new FileReader();
    reader.onload = async (event) => {
      const base64Image = event.target.result.split(',')[1];
      
      setLoading(true);
      setError(null);

      try {
        // Llama al endpoint de escaneo
        const response = await fetch(`${API_BASE_URL}/recycling/scan`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ image: base64Image, userId: MOCK_USER_ID }), 
        });

        if (!response.ok) {
          let errorDetail = await response.text();
          try {
            const errorJson = JSON.parse(errorDetail);
            errorDetail = errorJson.message || errorDetail;
          } catch (e) {}
          throw new Error(`Error ${response.status}: ${errorDetail}`);
        }

        const data = await response.json();
        setScanResult({
          message: '¡Felicidades!', 
          ecoMessage: '¡Tus acciones al reciclar hacen una gran diferencia!',
          ...data 
        });

      } catch (err) {
        setError(`⚠️ Falló el escaneo. Asegúrate de que tu backend esté corriendo en ${API_BASE_URL}. Detalle: ${err.message}`);
        setScanResult(null);
      } finally {
        setLoading(false);
      }
    };
    reader.readAsDataURL(imageFile);
    
  };

  // Sidebar izquierda (separada para mejor lectura)
  const Sidebar = () => (
    <div className="w-64 bg-white shadow-xl border-r flex flex-col">
      {/* Logo */}
      <div className="p-6 flex items-center space-x-2 border-b">

        <img src="/logo.png" alt="Logo" className="w-15 h-15" />

        <h1 className="font-bold text-xl text-green-600">Reeasy</h1>

      </div>

      {/* Nav Items */}
      <div className="flex-1 p-4 space-y-2">
        {/* Ítems originales */}
        <NavItem 
          icon={<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0 7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>} 
          label="Home (Scanear)" 
          active={view === 'scan'}
          onClick={() => setView('scan')}
        />
        <NavItem 
          icon={<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 15.546c-.523 0-1.046.1-.013c.09.227.18.455.268.682M8 4h.01M12 4h.01M16 4h.01M8 8h.01M12 8h.01M16 8h.01M12 12h.01M16 12h.01M8 12h.01M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z"/></svg>}
          label="Canjear" 
          active={false} 
          onClick={() => toast.warning("Navegar a Recolectar (no implementado)")}
        />
        <NavItem 
          icon={<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 22V8a1 1 0 011-1h13M4 17h16M4 21h16M4 13h16M14 2h-4a2 2 0 00-2 2v3h8V4a2 2 0 00-2-2z"/></svg>} 
          label="Recolectar" 
          active={false} 
          onClick={() => toast.warning("Navegar a Recolectar (no implementado)")}
        />
        
        {/* Ítem de administración de cupones (Nuevo) */}
        <div className="pt-4 border-t mt-4">
            <NavItem 
              icon={<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 7h10M7 11h10M11 15h6M7 15h.01M12 3v18M3 12h18"/></svg>} 
              label="Admin. Cupones" 
              active={view === 'add_coupon'} 
              onClick={() => setView('add_coupon')}
            />
        </div>
        
      </div>
      
      {/* Footer de usuario */}
      <div className="p-4 border-t">
        <NavItem 
          icon={<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14c4.418 0 8 3.582 8 8H4c0-4.418 3.582-8 8-8z"/></svg>} 
          label="Perfil" 
          active={false} 
          onClick={() => toast.warning("Navegar a Perfil (no implementado)")}
        />
      </div>
    </div>
  );

  // Contenido de la vista 'scan'
  const ScanContent = () => (
    <div className="bg-white p-6 rounded-2xl shadow-lg">
      <h2 className="text-xl font-semibold text-center text-gray-700 mb-4">
        {scanResult ? 'Resultados del Escaneo' : 'Escanear Objeto'}
      </h2>

      {error && <MessageBox message={error} type="error" />}

      {!scanResult && (
        <>
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            className="w-full text-sm text-gray-500
              file:mr-4 file:py-3 file:px-6
              file:rounded-full file:border-0
              file:text-sm file:font-semibold
              file:bg-green-50 file:text-green-700
              hover:file:bg-green-100"
          />

          {imageFile && (
            <div className="mt-4 mb-4 p-2 bg-gray-50 rounded-lg shadow-inner">
              <img src={URL.createObjectURL(imageFile)} alt="Preview" className="w-full max-h-60 object-contain rounded-lg border" />
            </div>
          )}

          <button
            onClick={handleScan}
            disabled={loading || !imageFile}
            className={`w-full py-3 rounded-xl text-white font-bold mt-4 shadow-md ${
              loading ? 'bg-green-400' : 'bg-green-500 hover:bg-green-600'
            }`}
          >
            {loading ? 'Escaneando...' : 'ESCANEAR AHORA'}
          </button>
        </>
      )}

      {scanResult && <ScanResults result={scanResult} />}
    </div>
  );


  return (
    // Contenedor principal para escritorio
    <div className="flex min-h-screen w-full bg-gray-100 font-sans">
      
      {/* Sidebar: Visible en todo momento */}
      <Sidebar />

      {/* Contenido principal: Ocupa el espacio restante */}
      <div className="flex-1 p-8 overflow-y-auto">
        
        {/* Header Superior del Contenido */}
        <header className="flex justify-between items-center mb-6 p-4 bg-white rounded-xl shadow-md">
          <div>
            <h2 className="text-2xl font-bold text-gray-800">Hola Simona Sposito!</h2>
            <p className="text-lg text-green-600">
              {view === 'scan' ? 'Escaneo de Plásticos' : 'Administración de Cupones'}
            </p>
          </div>
          <div className="p-2 bg-gray-50 rounded-full shadow cursor-pointer hover:bg-gray-100 transition">
            <svg className="text-gray-600 h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1"/>
            </svg>
          </div>
        </header>

        {/* Mensaje Eco (Solo en vista de escaneo) */}
        {view === 'scan' && (
          <div className="mb-6 p-4 bg-yellow-100 rounded-xl shadow-md flex items-start">
            <span className="text-2xl mr-2">💡</span>
            <p className="text-sm text-gray-700 font-medium">
              {scanResult?.ecoMessage || "El escaneo ayuda a clasificar plásticos y sumar puntos de impacto ambiental."}
            </p>
          </div>
        )}

        {/* Contenido Dinámico */}
        <div className="max-w-4xl mx-auto">
          {view === 'scan' && <ScanContent />}
          {view === 'add_coupon' && <AddCoupon userId={MOCK_USER_ID} />}
        </div>
        
      </div>

      <Toaster position="top-right" richColors />

    </div>
  );
};

export default App;
