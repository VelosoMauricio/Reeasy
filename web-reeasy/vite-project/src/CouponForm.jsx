import React, { useState } from "react";

const CouponForm = () => {
  const [coupon, setCoupon] = useState({
    coupon_id: "",
    expiration_date: "",
    price: "",
    amount: "",
    description: "",
    link: "",
    image: "",
    Enterprise_cuit: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCoupon({ ...coupon, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/coupons", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(coupon),
      });

      if (!response.ok) throw new Error("Error al guardar cupón");

      alert("Cupón agregado con éxito 🚀");
      setCoupon({
        coupon_id: "",
        expiration_date: "",
        price: "",
        amount: "",
        description: "",
        link: "",
        image: "",
        Enterprise_cuit: "",
      });
    } catch (err) {
      console.error(err);
      alert("Hubo un error al agregar el cupón ❌");
    }
  };

  return (
    <div className="max-w-md mx-auto p-6 border rounded-2xl shadow-lg bg-white">
      <h1 className="text-xl font-bold mb-4">Agregar Cupón</h1>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3">
        <input
          type="number"
          name="coupon_id"
          placeholder="ID del cupón"
          value={coupon.coupon_id}
          onChange={handleChange}
          className="p-2 border rounded-lg"
          required
        />
        <input
          type="date"
          name="expiration_date"
          value={coupon.expiration_date}
          onChange={handleChange}
          className="p-2 border rounded-lg"
          required
        />
        <input
          type="number"
          name="price"
          placeholder="Precio"
          value={coupon.price}
          onChange={handleChange}
          className="p-2 border rounded-lg"
          required
        />
        <input
          type="number"
          name="amount"
          placeholder="Cantidad"
          value={coupon.amount}
          onChange={handleChange}
          className="p-2 border rounded-lg"
          required
        />
        <input
          type="text"
          name="description"
          placeholder="Descripción"
          value={coupon.description}
          onChange={handleChange}
          className="p-2 border rounded-lg"
          required
        />
        <input
          type="url"
          name="link"
          placeholder="Link del cupón"
          value={coupon.link}
          onChange={handleChange}
          className="p-2 border rounded-lg"
        />
        <input
          type="text"
          name="image"
          placeholder="URL de la imagen"
          value={coupon.image}
          onChange={handleChange}
          className="p-2 border rounded-lg"
        />
        <input
          type="number"
          name="Enterprise_cuit"
          placeholder="CUIT de la empresa"
          value={coupon.Enterprise_cuit}
          onChange={handleChange}
          className="p-2 border rounded-lg"
        />

        <button
          type="submit"
          className="mt-4 px-4 py-2 bg-green-500 text-white rounded-lg shadow hover:bg-green-600"
        >
          Guardar Cupón
        </button>
      </form>
    </div>
  );
};

export default CouponForm;
