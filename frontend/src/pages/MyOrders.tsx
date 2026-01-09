import { useEffect, useState } from "react";
import api from "../api/axios";
import { FileText, ShoppingBag } from "lucide-react";

interface OrderItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

interface Order {
  id: number;
  createdAt: string;
  status: "NEW" | "IN_PROGRESS" | "READY" | "DELIVERED" | "PAID" | "CANCELLED";
  totalPrice: number;
  restaurant: {
    name: string;
  };
  items: OrderItem[];
}

const statusColors: Record<string, string> = {
  NEW: "bg-blue-100 text-blue-800",
  IN_PROGRESS: "bg-yellow-100 text-yellow-800",
  READY: "bg-green-100 text-green-800",
  DELIVERED: "bg-gray-100 text-gray-800",
  PAID: "bg-green-200 text-green-900",
  CANCELLED: "bg-red-100 text-red-800",
};

const statusLabels: Record<string, string> = {
  NEW: "Nowe",
  IN_PROGRESS: "W trakcie",
  READY: "Gotowe do odbioru",
  DELIVERED: "Dostarczone",
  PAID: "Opłacone",
  CANCELLED: "Anulowane",
};

export const MyOrders = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/orders/my")
      .then((res) => {
        const sorted = res.data.sort(
          (a: Order, b: Order) =>
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        setOrders(sorted);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  const downloadReceipt = async (orderId: number) => {
    try {
      const response = await api.get(`/orders/${orderId}/receipt`, {
        responseType: "blob",
      });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `paragon_${orderId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert("Nie udało się pobrać paragonu.");
    }
  };

  if (loading)
    return <div className="text-center mt-10">Ładowanie historii...</div>;

  return (
    <div className="max-w-3xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-gray-800 flex items-center gap-2">
        <ShoppingBag /> Moje Zamówienia
      </h1>

      {orders.length === 0 ? (
        <div className="text-center bg-white p-10 rounded-lg shadow">
          <p className="text-gray-500 mb-4">
            Nie masz jeszcze żadnych zamówień.
          </p>
        </div>
      ) : (
        <div className="space-y-6">
          {orders.map((order) => (
            <div
              key={order.id}
              className="bg-white rounded-lg shadow-md overflow-hidden border border-gray-100"
            >
              <div className="bg-gray-50 px-6 py-4 flex justify-between items-center border-b border-gray-200">
                <div>
                  <h3 className="font-bold text-lg text-gray-900">
                    {order.restaurant.name}
                  </h3>
                  <p className="text-xs text-gray-500">
                    {new Date(order.createdAt).toLocaleString()} | ID: #
                    {order.id}
                  </p>
                </div>
                <span
                  className={`px-3 py-1 rounded-full text-xs font-bold ${
                    statusColors[order.status] || "bg-gray-100"
                  }`}
                >
                  {statusLabels[order.status] || order.status}
                </span>
              </div>

              <div className="p-6">
                <ul className="mb-4 space-y-2">
                  {order.items.map((item) => (
                    <li
                      key={item.id}
                      className="flex justify-between text-sm text-gray-700"
                    >
                      <span>
                        {item.quantity}x {item.name}
                      </span>
                      <span>{(item.price * item.quantity).toFixed(2)} PLN</span>
                    </li>
                  ))}
                </ul>

                <div className="border-t pt-4 flex justify-between items-center">
                  <span className="font-bold text-lg">
                    Suma: {order.totalPrice.toFixed(2)} PLN
                  </span>

                  <button
                    onClick={() => downloadReceipt(order.id)}
                    className="flex items-center gap-1 text-sm text-indigo-600 hover:text-indigo-800 font-medium"
                  >
                    <FileText className="h-4 w-4" /> Pobierz Paragon
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
