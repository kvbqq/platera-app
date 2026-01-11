import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";
import { jwtDecode } from "jwt-decode";

interface User {
  sub: string;
  auth: string;
  exp: number;
}

interface AuthContextType {
  token: string | null;
  user: User | null;
  login: (token: string) => void;
  logout: () => void;
  isAdmin: boolean;
  isManager: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("token")
  );
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    if (token) {
      try {
        const decoded = jwtDecode<User>(token);
        if (decoded.exp * 1000 < Date.now()) {
          logout();
        } else {
          setUser(decoded);
          localStorage.setItem("token", token);
        }
      } catch (e) {
        logout();
      }
    } else {
      localStorage.removeItem("token");
      setUser(null);
    }
  }, [token]);

  const login = (newToken: string) => {
    setToken(newToken);
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  const isAdmin = user?.auth?.includes("ADMIN") || false;
  const isManager = user?.auth?.includes("MANAGER") || false;

  return (
    <AuthContext.Provider
      value={{ token, user, login, logout, isAdmin, isManager }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
};
