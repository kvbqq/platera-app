import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";

interface Restaurant {
  id: number;
  name: string;
  city: string;
  address: string;
}

export const AdminDashboard = () => {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);

  useEffect(() => {
    api
      .get("/restaurants")
      .then((res) => {
        const data = Array.isArray(res.data)
          ? res.data
          : res.data.content || [];
        setRestaurants(data);
      })
      .catch((err) => console.error(err));
  }, []);

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-gray-800">
        Panel Zarządzania
      </h1>
      <p className="mb-6 text-gray-600">
        Wybierz restaurację, aby zarządzać zamówieniami:
      </p>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {restaurants.map((r) => (
          <Link
            key={r.id}
            to={`/admin/restaurant/${r.id}`}
            className="block bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow border-l-4 border-indigo-500"
          >
            <div className="flex items-center gap-4">
              <div className="bg-indigo-100 p-3 rounded-full text-indigo-600">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                >
                  <path d="m2 7 4.41-4.41A2 2 0 0 1 7.83 2h8.34a2 2 0 0 1 1.42.59L22 7" />
                  <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8" />
                  <path d="M15 22v-4a2 2 0 0 0-2-2h-2a2 2 0 0 0-2 2v4" />
                  <path d="M2 7h20" />
                  <path d="M22 7v3a2 2 0 0 1-2 2v0a2.7 2.7 0 0 1-1.59-.63.7.7 0 0 0-.82 0A2.7 2.7 0 0 1 16 12a2.7 2.7 0 0 1-1.59-.63.7.7 0 0 0-.82 0A2.7 2.7 0 0 1 12 12a2.7 2.7 0 0 1-1.59-.63.7.7 0 0 0-.82 0A2.7 2.7 0 0 1 8 12a2.7 2.7 0 0 1-1.59-.63.7.7 0 0 0-.82 0A2.7 2.7 0 0 1 4 12v0a2 2 0 0 1-2-2V7" />
                </svg>
              </div>
              <div>
                <h2 className="text-xl font-bold text-gray-900">{r.name}</h2>
                <p className="text-sm text-gray-500">
                  {r.city}, {r.address}
                </p>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
};
