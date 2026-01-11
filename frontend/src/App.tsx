import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import { Layout } from "./components/Layout";
import { RestaurantList } from "./pages/RestaurantList";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { RestaurantDetails } from "./pages/RestaurantDetails";
import { MyOrders } from "./pages/MyOrders";
import { AdminDashboard } from "./pages/AdminDashboard";
import { ManagerOrders } from "./pages/ManagerOrders";
import { ManagerTables } from "./pages/ManagerTables";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { ManagerMenu } from "./pages/ManagerMenu";

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<Layout />}>
                <Route index element={<RestaurantList />} />
                <Route path="restaurant/:id" element={<RestaurantDetails />} />
                <Route path="my-orders" element={<MyOrders />} />

                <Route path="admin" element={<AdminDashboard />} />
                <Route
                  path="admin/restaurant/:restaurantId"
                  element={<ManagerOrders />}
                />
                <Route
                  path="admin/restaurant/:restaurantId/tables"
                  element={<ManagerTables />}
                />
              </Route>
              <Route
                path="admin/restaurant/:restaurantId/menu"
                element={<ManagerMenu />}
              />
            </Route>

            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </BrowserRouter>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;
