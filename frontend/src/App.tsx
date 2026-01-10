import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { Layout } from "./components/Layout";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { RestaurantList } from "./pages/RestaurantList";
import { CartProvider } from "./context/CartContext";
import { RestaurantDetails } from "./pages/RestaurantDetails.tsx";
import { MyOrders } from "./pages/MyOrders";
import { AdminDashboard } from "./pages/AdminDashboard";
import { ManagerOrders } from "./pages/ManagerOrders";

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Layout />}>
              <Route index element={<RestaurantList />} />
              <Route path="login" element={<Login />} />
              <Route path="register" element={<Register />} />
              <Route path="restaurant/:id" element={<RestaurantDetails />} />
              <Route path="my-orders" element={<MyOrders />} />

              <Route path="admin" element={<AdminDashboard />} />
              <Route
                path="admin/restaurant/:restaurantId"
                element={<ManagerOrders />}
              />
            </Route>
          </Routes>
        </BrowserRouter>
      </CartProvider>
    </AuthProvider>
  );
}
export default App;
