import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";
import { MapPin, UtensilsCrossed } from "lucide-react";

interface Restaurant {
  id: number;
  name: string;
  description: string;
  city: string;
  address: string;
}

export const RestaurantList = () => {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/restaurants")
      .then((res) => {
        console.log("Co przyszło z backendu?", res.data);

        if (Array.isArray(res.data)) {
          setRestaurants(res.data);
        } else if (res.data.content && Array.isArray(res.data.content)) {
          setRestaurants(res.data.content);
        } else {
          console.error("Otrzymano dziwne dane (nie tablicę):", res.data);
          setRestaurants([]);
        }
        setLoading(false);
      })
      .catch((err) => {
        console.error("Błąd pobierania restauracji:", err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div className="text-center mt-10">Ładowanie restauracji...</div>;
  }

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-8">
        Dostępne Restauracje
      </h1>

      {restaurants.length === 0 ? (
        <div className="text-center text-gray-500 mt-10">
          Brak restauracji w bazie. Dodaj jakąś przez Panel Admina lub w bazie
          danych.
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {restaurants?.map((restaurant) => (
            <div
              key={restaurant.id}
              className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
            >
              <div className="h-48 bg-gray-200 flex items-center justify-center">
                <UtensilsCrossed className="h-12 w-12 text-gray-400" />
              </div>

              <div className="p-6">
                <h2 className="text-xl font-semibold text-gray-900 mb-2">
                  {restaurant.name}
                </h2>
                <p className="text-gray-600 text-sm mb-4 line-clamp-2">
                  {restaurant.description}
                </p>

                <div className="flex items-center text-gray-500 text-sm mb-4">
                  <MapPin className="h-4 w-4 mr-1" />
                  <span>
                    {restaurant.city}, {restaurant.address}
                  </span>
                </div>

                <Link
                  to={`/restaurant/${restaurant.id}`}
                  className="block w-full text-center bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 transition-colors"
                >
                  Zobacz Menu
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
