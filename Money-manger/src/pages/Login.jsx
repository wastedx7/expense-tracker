import React, { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import Login_bg from "../assets/Login_bg.jpg";
import Input from "../components/Input";
import { AppContext } from "../context/AppContext";
import { LoaderCircle } from "lucide-react";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINT } from "../util/apiEndpoints";
import { validEmail } from "../util/validation";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const { setUser } = useContext(AppContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!validEmail(email)) {
      setError("Please enter a valid email.");
      return;
    }

    if (!password.trim()) {
      setError("Please enter your password.");
      return;
    }

    setIsLoading(true);

    try {
      const response = await axiosConfig.post(API_ENDPOINT.LOGIN, {
        email,
        password,
      });

      const { token, user } = response.data;

      if (token) {
        localStorage.setItem("token", token);
        setUser(user);
        navigate("/dashboard");
      }
    } catch (err) {
      console.error("Login failed:", err);
      setError(err.response?.data?.message || "Invalid email or password");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="h-screen w-full relative flex items-center justify-center overflow-hidden">
      <img
        src={Login_bg}
        alt="Background"
        className="absolute inset-0 h-full w-full object-cover blur-sm"
      />

      <div className="absolute z-10 w-full max-w-lg px-6">
        <div className="bg-white bg-opacity-95 backdrop-blur-sm rounded-lg shadow-2xl p-8 max-h-[90vh] overflow-y-auto">
          <h3 className="text-2xl font-semibold text-center mb-2 text-black">
            Login
          </h3>

          <p className="text-sm text-slate-700 text-center mb-6">
            Welcome back! Please login to continue.
          </p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Email"
              value={email}
              onChange={(e)=>setEmail(e.target.value)}
              placeholder="example@gmail.com"
              type="text"
            />

            <Input
              label="Password"
              value={password}
              onChange={(e)=>setPassword(e.target.value)}
              placeholder="Enter password"
              type="password"
            />

            {error && (
              <p className="text-red-800 text-sm text-center bg-red-50 p-2 rounded">
                {error}
              </p>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className={`bg-blue-600 text-white flex items-center justify-center gap-2 ${
                isLoading ? "opacity-60 cursor-not-allowed" : ""
              } w-full py-3 rounded-lg text-lg font-medium hover:bg-blue-700 transition`}
            >
              {isLoading ? (
                <>
                  <LoaderCircle className="animate-spin w-5 h-5" />
                  Logging in...
                </>
              ) : (
                "Login"
              )}
            </button>

            <p className="text-sm text-slate-800 text-center mt-6">
              Don’t have an account?{" "}
              <Link
                to="/signup"
                className="font-medium text-blue-600 underline hover:text-blue-800"
              >
                Sign Up
              </Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
