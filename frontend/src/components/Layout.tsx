import { Outlet, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { LogOut, Utensils } from "lucide-react";

export const Layout = () => {
  const { user, logout, isAdmin, isManager } = useAuth();

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <Link
                to="/"
                className="flex items-center gap-2 text-indigo-600 font-bold text-xl"
              >
                <Utensils className="h-6 w-6" />
                <span>Platera</span>
              </Link>
              <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
                <Link
                  to="/"
                  className="text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 border-transparent hover:border-indigo-500 text-sm font-medium"
                >
                  Restauracje
                </Link>
                {user && (
                  <Link
                    to="/my-orders"
                    className="text-gray-500 hover:text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium"
                  >
                    Moje Zamówienia
                  </Link>
                )}
                {(isAdmin || isManager) && (
                  <Link
                    to="/admin"
                    className="text-red-600 hover:text-red-800 inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium"
                  >
                    Panel Admina
                  </Link>
                )}
              </div>
            </div>
            <div className="flex items-center">
              {user ? (
                <div className="flex items-center gap-4">
                  <span className="text-sm text-gray-500">{user.sub}</span>
                  <button
                    onClick={logout}
                    className="text-gray-500 hover:text-gray-700"
                  >
                    <LogOut className="h-5 w-5" />
                  </button>
                </div>
              ) : (
                <div className="flex gap-4">
                  <Link
                    to="/login"
                    className="text-indigo-600 hover:text-indigo-500 font-medium"
                  >
                    Zaloguj
                  </Link>
                  <Link
                    to="/register"
                    className="bg-indigo-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-indigo-700"
                  >
                    Rejestracja
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <Outlet />
      </main>
    </div>
  );
};
