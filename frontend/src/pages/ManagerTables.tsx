import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import api from "../api/axios";
import { QrCode, Trash2, Plus } from "lucide-react";

interface Table {
  id: number;
  label: string;
  capacity: number;
}

export const ManagerTables = () => {
  const { restaurantId } = useParams<{ restaurantId: string }>();
  const [tables, setTables] = useState<Table[]>([]);
  const { register, handleSubmit, reset } = useForm();

  const fetchTables = () => {
    api
      .get(`/restaurants/${restaurantId}/tables`)
      .then((res) => setTables(res.data))
      .catch(console.error);
  };

  useEffect(() => {
    fetchTables();
  }, [restaurantId]);

  const onAddTable = async (data: any) => {
    try {
      await api.post(`/restaurants/${restaurantId}/tables`, data);
      reset();
      fetchTables();
    } catch (err) {
      alert("Błąd dodawania stolika");
    }
  };

  const onDeleteTable = async (id: number) => {
    if (!confirm("Usunąć stolik?")) return;
    try {
      await api.delete(`/restaurants/tables/${id}`);
      fetchTables();
    } catch (err) {
      alert("Nie można usunąć (może ma rezerwacje?)");
    }
  };

  const downloadQr = async (table: Table) => {
    try {
      const response = await api.get(`/restaurants/tables/${table.id}/qr`, {
        responseType: "blob",
      });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `QR_Stolik_${table.label}.png`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert("Błąd pobierania QR. Czy backend ma QrCodeService?");
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">
        Zarządzanie Stolikami
      </h1>

      <div className="bg-white p-6 rounded-lg shadow mb-8">
        <h2 className="text-lg font-semibold mb-4">Dodaj nowy stolik</h2>
        <form
          onSubmit={handleSubmit(onAddTable)}
          className="flex gap-4 items-end"
        >
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Numer/Nazwa
            </label>
            <input
              {...register("label", { required: true })}
              className="mt-1 block w-full border border-gray-300 rounded-md p-2"
              placeholder="np. 12"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Ilość miejsc
            </label>
            <input
              {...register("capacity", { required: true })}
              type="number"
              className="mt-1 block w-full border border-gray-300 rounded-md p-2"
              placeholder="4"
            />
          </div>
          <button
            type="submit"
            className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 flex items-center gap-2"
          >
            <Plus className="h-4 w-4" /> Dodaj
          </button>
        </form>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {tables.map((table) => (
          <div
            key={table.id}
            className="bg-white p-4 rounded-lg shadow border border-gray-200 flex justify-between items-center"
          >
            <div>
              <h3 className="font-bold text-lg">Stolik: {table.label}</h3>
              <p className="text-gray-500 text-sm">Miejsc: {table.capacity}</p>
            </div>
            <div className="flex gap-2">
              <button
                onClick={() => downloadQr(table)}
                className="text-indigo-600 hover:bg-indigo-50 p-2 rounded"
                title="Pobierz QR"
              >
                <QrCode />
              </button>
              <button
                onClick={() => onDeleteTable(table.id)}
                className="text-red-600 hover:bg-red-50 p-2 rounded"
                title="Usuń"
              >
                <Trash2 />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
