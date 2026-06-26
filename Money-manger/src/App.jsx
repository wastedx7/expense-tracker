import React from 'react'
import { HashRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AppContextProvider } from "./context/AppContext";


// Pages
import Home from './pages/Home'
import Income from './pages/Income'
import Expense from './pages/Expense'
import Category from './pages/Category'
import Filter from './pages/Filter'
import Login from './pages/Login'
import Signup from './pages/Signup'
import DashboardHome from './pages/DashoardHome';

// Toast (fix spelling)
import { Toaster } from 'react-hot-toast'

const ProtectedRoute = ({ children }) => {
  const hasToken = Boolean(localStorage.getItem("token"));
  return hasToken ? children : <Navigate to="/login" replace />;
};

function App() {
  return (
    <>
      <Toaster />

      <HashRouter>
      <AppContextProvider>
        <Routes>
          <Route path="/" element={<Root/>}/>
          {/**<Route path="/dashboard" element={<Home />} /> */}
          <Route path="/dashboard" element={<ProtectedRoute><DashboardHome /></ProtectedRoute>} />
          <Route path="/dashbord" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashoard" element={<Navigate to="/dashboard" replace />} />
          <Route path="/income" element={<ProtectedRoute><Income /></ProtectedRoute>} />
          <Route path="/expense" element={<ProtectedRoute><Expense /></ProtectedRoute>} />
          <Route path="/category" element={<ProtectedRoute><Category /></ProtectedRoute>} />
          <Route path="/filter" element={<ProtectedRoute><Filter /></ProtectedRoute>} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="*" element={<Root />} />
        </Routes>
        </AppContextProvider>
      </HashRouter>
    </>
  )
}
const Root = () => {
  const isAuthenticated = Boolean(localStorage.getItem("token"));
  return isAuthenticated ? (
    <Navigate to="/dashboard" replace></Navigate>
  ) : (
    <Navigate to="/login" replace></Navigate>
  );
}

export default App
