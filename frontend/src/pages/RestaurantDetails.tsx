import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/axios";
import { useCart } from "../context/CartContext";
import { ShoppingCart, PlusCircle } from "lucide-react";

interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
}

interface MenuCategory {
  id: number;
  name: string;
  items: MenuItem[];
}

export const RestaurantDetails = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { addToCart, items, total, clearCart } = useCart();

  const [menu, setMenu] = useState<MenuCategory[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get(`/restaurants/${id}/menu`)
      .then((res) => {
        setMenu(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Błąd pobierania menu:", err);
        setLoading(false);
      });
  }, [id]);

  const handleOrder = async () => {
    try {
      const orderData = {
        restaurantId: Number(id),
        tableId: null,
        items: items.map((item) => ({
          menuItemId: item.menuItemId,
          quantity: item.quantity,
        })),
      };

      await api.post("/orders", orderData);
      alert("Zamówienie złożone pomyślnie!");
      clearCart();
      navigate("/my-orders");
    } catch (err) {
      console.error(err);
      alert("Błąd podczas składania zamówienia.");
    }
  };

  if (loading)
    return <div className="text-center mt-10">Ładowanie menu...</div>;

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <div className="lg:col-span-2">
        <h1 className="text-3xl font-bold mb-6 text-gray-800">Menu</h1>
        {menu.map((category) => (
          <div key={category.id} className="mb-8">
            <h2 className="text-2xl font-semibold mb-4 text-indigo-700 border-b pb-2">
              {category.name}
            </h2>
            <div className="grid gap-4">
              {category.items.map((item) => (
                <div
                  key={item.id}
                  className="bg-white p-4 rounded-lg shadow flex justify-between items-center hover:bg-gray-50"
                >
                  <div>
                    <h3 className="font-bold text-gray-900">{item.name}</h3>
                    <p className="text-sm text-gray-500">{item.description}</p>
                    <span className="text-indigo-600 font-bold mt-1 block">
                      {item.price} PLN
                    </span>
                  </div>
                  <button
                    onClick={() =>
                      addToCart(
                        {
                          menuItemId: item.id,
                          name: item.name,
                          price: item.price,
                          quantity: 1,
                        },
                        Number(id)
                      )
                    }
                    className="text-indigo-600 hover:text-indigo-800"
                  >
                    <PlusCircle className="h-8 w-8" />
                  </button>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>

      <div className="lg:col-span-1">
        <div className="bg-white p-6 rounded-lg shadow-lg sticky top-24">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-bold flex items-center gap-2">
              <ShoppingCart /> Twój Koszyk
            </h2>
            {items.length > 0 && (
              <button
                onClick={clearCart}
                className="text-xs text-red-500 hover:text-red-700"
              >
                Wyczyść
              </button>
            )}
          </div>

          {items.length === 0 ? (
            <p className="text-gray-500 text-center py-8">Koszyk jest pusty.</p>
          ) : (
            <>
              <ul className="divide-y divide-gray-200 mb-4">
                {items.map((item) => (
                  <li
                    key={item.menuItemId}
                    className="py-2 flex justify-between text-sm"
                  >
                    <span>
                      {item.quantity}x {item.name}
                    </span>
                    <span className="font-medium">
                      {(item.price * item.quantity).toFixed(2)} PLN
                    </span>
                  </li>
                ))}
              </ul>
              <div className="border-t pt-4">
                <div className="flex justify-between font-bold text-lg mb-4">
                  <span>Suma:</span>
                  <span>{total.toFixed(2)} PLN</span>
                </div>
                <button
                  onClick={handleOrder}
                  className="w-full bg-green-600 text-white py-3 rounded-lg font-bold hover:bg-green-700 transition-colors"
                >
                  ZAMÓW I ZAPŁAĆ
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};
