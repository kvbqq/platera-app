import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import api from "../api/axios";
import { Store, Plus, Trash2 } from "lucide-react";

interface Restaurant {
  id: number;
  name: string;
  city: string;
  address: string;
  description: string;
}

export const AdminDashboard = () => {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const { register, handleSubmit, reset } = useForm();
  const [isCreating, setIsCreating] = useState(false);

  const fetchRestaurants = () => {
    api
      .get("/restaurants")
      .then((res) => {
        const data = Array.isArray(res.data)
          ? res.data
          : res.data.content || [];
        setRestaurants(data);
      })
      .catch(console.error);
  };

  useEffect(() => {
    fetchRestaurants();
  }, []);

  const onCreate = async (data: any) => {
    try {
      await api.post("/restaurants", data);
      alert("Restauracja dodana!");
      reset();
      setIsCreating(false);
      fetchRestaurants();
    } catch (err) {
      alert("Błąd tworzenia restauracji");
    }
  };

  const onDelete = async (id: number) => {
    if (!confirm("Usunąć restaurację?")) return;
    try {
      await api.delete(`/restaurants/${id}`);
      fetchRestaurants();
    } catch (err) {
      alert("Nie można usunąć.");
    }
  };

  return (
    <div className="max-w-6xl mx-auto px-4">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Panel Zarządzania</h1>
        <button
          onClick={() => setIsCreating(!isCreating)}
          className="bg-indigo-600 text-white px-4 py-2 rounded shadow hover:bg-indigo-700 flex items-center gap-2"
        >
          <Plus size={20} /> {isCreating ? "Anuluj" : "Dodaj Restaurację"}
        </button>
      </div>

      {/* FORMULARZ TWORZENIA */}
      {isCreating && (
        <div className="bg-white p-6 rounded-lg shadow-lg mb-8 border border-indigo-100 animate-fade-in">
          <h2 className="text-xl font-bold mb-4 text-indigo-800">Nowy Lokal</h2>
          <form onSubmit={handleSubmit(onCreate)} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input
                {...register("name")}
                placeholder="Nazwa Restauracji"
                className="border p-2 rounded"
                required
              />
              <input
                {...register("city")}
                placeholder="Miasto"
                className="border p-2 rounded"
                required
              />
              <input
                {...register("address")}
                placeholder="Adres"
                className="border p-2 rounded"
                required
              />
              <input
                {...register("description")}
                placeholder="Krótki opis"
                className="border p-2 rounded"
              />
            </div>
            <button className="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700 w-full font-bold">
              ZAPISZ
            </button>
          </form>
        </div>
      )}

      {/* LISTA RESTAURACJI */}
      <div className="grid grid-cols-1 gap-6">
        {restaurants.map((r) => (
          <div
            key={r.id}
            className="bg-white p-6 rounded-lg shadow border-l-4 border-indigo-500 flex flex-col md:flex-row justify-between items-center hover:shadow-md transition-shadow"
          >
            <div className="flex items-center gap-4 mb-4 md:mb-0 w-full md:w-auto">
              <div className="bg-indigo-100 p-3 rounded-full text-indigo-600 hidden sm:block">
                <Store size={24} />
              </div>
              <div>
                <h2 className="text-xl font-bold text-gray-900">{r.name}</h2>
                <p className="text-sm text-gray-500">
                  {r.city}, {r.address}
                </p>
              </div>
            </div>

            <div className="flex flex-wrap gap-2 w-full md:w-auto justify-end">
              <Link
                to={`/admin/restaurant/${r.id}`}
                className="bg-blue-50 text-blue-700 px-3 py-2 rounded hover:bg-blue-100 font-medium text-sm"
              >
                Zamówienia
              </Link>
              <Link
                to={`/admin/restaurant/${r.id}/tables`}
                className="bg-purple-50 text-purple-700 px-3 py-2 rounded hover:bg-purple-100 font-medium text-sm"
              >
                Stoliki / QR
              </Link>
              <Link
                to={`/admin/restaurant/${r.id}/menu`}
                className="bg-green-50 text-green-700 px-3 py-2 rounded hover:bg-green-100 font-medium text-sm"
              >
                Edytuj Menu
              </Link>
              <button
                onClick={() => onDelete(r.id)}
                className="bg-red-50 text-red-600 px-3 py-2 rounded hover:bg-red-100 p-2"
              >
                <Trash2 size={18} />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
