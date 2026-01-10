import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";

interface OrderItem {
  id: number;
  name: string;
  quantity: number;
}

interface Order {
  id: number;
  createdAt: string;
  status: string;
  totalPrice: number;
  table: { label: string } | null;
  items: OrderItem[];
}

const NEXT_STATUS: Record<string, string> = {
  NEW: "IN_PROGRESS",
  IN_PROGRESS: "READY",
  READY: "DELIVERED",
  DELIVERED: "PAID",
  PAID: "COMPLETED",
};

const STATUS_LABELS: Record<string, string> = {
  NEW: "Nowe",
  IN_PROGRESS: "W kuchni",
  READY: "Do wydania",
  DELIVERED: "U klienta",
  PAID: "Opłacone",
  CANCELLED: "Anulowane",
};

export const ManagerOrders = () => {
  const { restaurantId } = useParams<{ restaurantId: string }>();
  const [orders, setOrders] = useState<Order[]>([]);

  const fetchOrders = () => {
    api
      .get(`/orders/restaurant/${restaurantId}`)
      .then((res) => {
        const sorted = res.data.sort((a: Order, b: Order) => b.id - a.id);
        setOrders(sorted);
      })
      .catch(console.error);
  };

  useEffect(() => {
    fetchOrders();
    const interval = setInterval(fetchOrders, 10000);
    return () => clearInterval(interval);
  }, [restaurantId]);

  const updateStatus = async (orderId: number, newStatus: string) => {
    try {
      await api.patch(`/orders/${orderId}/status`, `"${newStatus}"`, {
        headers: { "Content-Type": "application/json" },
      });
      fetchOrders();
    } catch (err) {
      console.error(err);
      alert("Błąd aktualizacji statusu");
    }
  };

  return (
    <div className="max-w-6xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Zarządzanie Zamówieniami</h1>

      <div className="overflow-x-auto bg-white rounded-lg shadow">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                ID / Czas
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Stolik
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Zamówienie
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Akcje
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {orders.map((order) => (
              <tr
                key={order.id}
                className={order.status === "NEW" ? "bg-blue-50" : ""}
              >
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  #{order.id}
                  <br />
                  <span className="text-gray-500 text-xs">
                    {new Date(order.createdAt).toLocaleTimeString([], {
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {order.table ? order.table.label : "Na wynos / App"}
                </td>
                <td className="px-6 py-4 text-sm text-gray-500">
                  <ul className="list-disc pl-4">
                    {order.items.map((item) => (
                      <li key={item.id}>
                        {item.quantity}x {item.name}
                      </li>
                    ))}
                  </ul>
                  <div className="font-bold mt-1">{order.totalPrice} PLN</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                    ${
                      order.status === "NEW"
                        ? "bg-blue-100 text-blue-800"
                        : order.status === "READY"
                        ? "bg-green-100 text-green-800"
                        : "bg-gray-100 text-gray-800"
                    }`}
                  >
                    {STATUS_LABELS[order.status] || order.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  {NEXT_STATUS[order.status] && (
                    <button
                      onClick={() =>
                        updateStatus(order.id, NEXT_STATUS[order.status])
                      }
                      className="text-indigo-600 hover:text-indigo-900 mr-4 font-bold"
                    >
                      {`ZMIEŃ NA: ${STATUS_LABELS[
                        NEXT_STATUS[order.status]
                      ]?.toUpperCase()}`}
                    </button>
                  )}
                  {order.status !== "CANCELLED" && order.status !== "PAID" && (
                    <button
                      onClick={() => updateStatus(order.id, "CANCELLED")}
                      className="text-red-600 hover:text-red-900"
                    >
                      Anuluj
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
