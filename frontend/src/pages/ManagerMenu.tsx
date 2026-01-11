import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import api from "../api/axios";
import { Trash2, Plus, Utensils } from "lucide-react";

interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
}

interface Category {
  id: number;
  name: string;
  items: MenuItem[];
}

export const ManagerMenu = () => {
  const { restaurantId } = useParams<{ restaurantId: string }>();
  const [menu, setMenu] = useState<Category[]>([]);
  const { register: regCat, handleSubmit: subCat, reset: resetCat } = useForm();

  const [activeCatId, setActiveCatId] = useState<number | null>(null);
  const {
    register: regItem,
    handleSubmit: subItem,
    reset: resetItem,
  } = useForm();

  const fetchMenu = () => {
    api
      .get(`/restaurants/${restaurantId}/menu`)
      .then((res) => setMenu(res.data))
      .catch(console.error);
  };

  useEffect(() => {
    fetchMenu();
  }, [restaurantId]);

  const onAddCategory = async (data: any) => {
    await api.post(`/restaurants/${restaurantId}/menu/categories`, data);
    resetCat();
    fetchMenu();
  };

  const onDeleteCategory = async (id: number) => {
    if (confirm("Usunąć kategorię?")) {
      // Use restaurantId in the path to target the correct restaurant
      await api.delete(`/restaurants/${restaurantId}/menu/categories/${id}`);
      fetchMenu();
    }
  };

  const onAddItem = async (data: any) => {
    if (!activeCatId) return;
    // Use restaurantId and categoryId in the path when creating an item
    await api.post(
      `/restaurants/${restaurantId}/menu/categories/${activeCatId}/items`,
      data
    );
    resetItem();
    setActiveCatId(null);
    fetchMenu();
  };

  const onDeleteItem = async (id: number) => {
    if (confirm("Usunąć danie?")) {
      // Include restaurantId in the deletion path
      await api.delete(`/restaurants/${restaurantId}/menu/items/${id}`);
      fetchMenu();
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-3xl font-bold mb-8 text-gray-800 flex items-center gap-2">
        <Utensils /> Edycja Menu
      </h1>

      <div className="bg-gray-100 p-4 rounded-lg mb-8 flex gap-2 items-center">
        <span className="font-bold text-gray-700">Nowa Kategoria:</span>
        <form onSubmit={subCat(onAddCategory)} className="flex gap-2 flex-1">
          <input
            {...regCat("name")}
            placeholder="np. Desery"
            className="flex-1 p-2 rounded border"
            required
          />
          <button className="bg-indigo-600 text-white px-4 rounded hover:bg-indigo-700">
            Dodaj
          </button>
        </form>
      </div>

      <div className="space-y-8">
        {menu.map((cat) => (
          <div
            key={cat.id}
            className="border border-gray-200 rounded-lg overflow-hidden bg-white shadow-sm"
          >
            <div className="bg-gray-50 px-6 py-3 flex justify-between items-center border-b">
              <h2 className="text-xl font-bold text-gray-800">{cat.name}</h2>
              <button
                onClick={() => onDeleteCategory(cat.id)}
                className="text-red-500 hover:text-red-700 p-1"
              >
                <Trash2 size={18} />
              </button>
            </div>
            <div className="p-6">
              <ul className="space-y-3 mb-6">
                {cat.items.map((item) => (
                  <li
                    key={item.id}
                    className="flex justify-between items-center border-b pb-2"
                  >
                    <div>
                      <div className="font-semibold">{item.name}</div>
                      <div className="text-sm text-gray-500">
                        {item.description}
                      </div>
                    </div>
                    <div className="flex items-center gap-4">
                      <span className="font-bold text-indigo-600">
                        {item.price} PLN
                      </span>
                      <button
                        onClick={() => onDeleteItem(item.id)}
                        className="text-red-400 hover:text-red-600"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </li>
                ))}
              </ul>
              {activeCatId === cat.id ? (
                <form
                  onSubmit={subItem(onAddItem)}
                  className="bg-indigo-50 p-4 rounded-lg"
                >
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-2 mb-2">
                    <input
                      {...regItem("name")}
                      placeholder="Nazwa"
                      className="p-2 border rounded"
                      required
                    />
                    <input
                      {...regItem("price")}
                      type="number"
                      step="0.01"
                      placeholder="Cena"
                      className="p-2 border rounded"
                      required
                    />
                    <input
                      {...regItem("description")}
                      placeholder="Opis"
                      className="p-2 border rounded"
                    />
                  </div>
                  <div className="flex gap-2">
                    <button className="bg-green-600 text-white px-3 py-1 rounded text-sm">
                      Zapisz
                    </button>
                    <button
                      type="button"
                      onClick={() => setActiveCatId(null)}
                      className="text-gray-500 text-sm"
                    >
                      Anuluj
                    </button>
                  </div>
                </form>
              ) : (
                <button
                  onClick={() => setActiveCatId(cat.id)}
                  className="text-indigo-600 text-sm font-bold flex items-center gap-1"
                >
                  <Plus size={16} /> Dodaj pozycję
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
