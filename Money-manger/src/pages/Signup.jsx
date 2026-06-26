import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Signup_bg from "../assets/Signup_bg.jpg";
import Input from '../components/Input';
import { validEmail } from '../util/validation';
import axiosConfig from '../util/axiosConfig';
import { LoaderCircle } from 'lucide-react';
import { API_ENDPOINT } from '../util/apiEndpoints';

function Signup() {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [showActivationPopup, setShowActivationPopup] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async(e)=>{
    e.preventDefault();

    setIsLoading(true);

    //validation
    if(!fullName.trim()){
      setError("Please enter your full Name");
      setIsLoading(false);
      return;
    
    }
    if(!validEmail(email)){
      setError("Please enter valid email");
      setIsLoading(false);
      return;
    }
    if(!password.trim()){
      setError("Please enter your password");
      setIsLoading(false);
      return;
    }
    setError("");

    //signup api call

    try{
      const response = await axiosConfig.post(API_ENDPOINT.REGISTER, {
        fullName,
        email,
        password,
      })
      console.log("Signup response:", response);
      if (response.status === 201 || response.status === 200) {
        setShowActivationPopup(true);
        setFullName("");
        setEmail("");
        setPassword("");
      }
    }catch(err){
      console.error("Signup error:", err);
      const errorMsg = err.response?.data?.message || err.message || "Signup failed. Please try again.";
      setError(errorMsg);
    }finally{
      setIsLoading(false);
    }

  }

  return (
    <div className="h-screen w-full relative flex items-center justify-center overflow-hidden">

      {/* Background Image */}
      <img 
        src={Signup_bg}
        alt="Background"
        className="absolute inset-0 h-full w-full object-cover blur-sm"
      />

      {/* Signup Card */}
      <div className="absolute z-10 w-full max-w-lg px-6">
        <div className="bg-white bg-opacity-95 backdrop-blur-sm rounded-lg shadow-2xl p-8 max-h-[90vh] overflow-y-auto">
          
          <h3 className="text-2xl font-semibold text-black text-center mb-2">
            Create An Account
          </h3>

          <p className="text-sm text-slate-700 text-center mb-8">
            Start tracking your spending by joining with us!
          </p>

          <form onSubmit={handleSubmit} className="space-y-4">

            <div className="grid grid-cols-2 gap-4">
              <Input
                value={fullName}
                onChange={(e)=>setFullName(e.target.value)}
                label="Full Name"
                placeholder="Danny"
                type="text"
              />

              <Input
                value={email}
                onChange={(e)=>setEmail(e.target.value)}
                label="Email"
                placeholder="danny@gmail.com"
                type="text"
              />

              <div className="col-span-2">
                <Input
                  value={password}
                  onChange={(e)=>setPassword(e.target.value)}
                  label="Password"
                  placeholder="Enter password"
                  type="password"
                />
              </div>
            </div>

            {error && (
              <p className="text-red-800 text-sm text-center bg-red-50 p-2 rounded">
                {error}
              </p>
            )}

            <button disabled={isLoading} className={`bg-blue-600 text-white w-full flex item-center justify-center gap-2 ${isLoading ? "opacity-60 cursor-not-allowed" : ""} py-3 rounded-lg text-lg font-medium hover:bg-blue-700 transition`} type='submit'>
              {isLoading ? (
                <>
                <LoaderCircle className='animate-spin w-5 h-5' />
                Signing up...
                </>
              ) : (
                "Sign Up"
              )}
            </button>

            <p className="text-sm text-slate-800 text-center mt-6">
              Already have an account?{" "}
              <Link to="/login" className="font-medium text-blue-600 underline hover:text-blue-800 transition-colors">
                Login
              </Link>
            </p>

          </form>

        </div>
      </div>

      {showActivationPopup && (
        <div className="absolute inset-0 z-20 flex items-center justify-center bg-black/50 px-4">
          <div className="w-full max-w-md rounded-xl bg-white p-6 shadow-2xl text-center">
            <h4 className="text-xl font-semibold text-slate-900 mb-2">Signup Successful</h4>
            <p className="text-slate-700 mb-5">
              Your profile is created. Activation link is sent to your email.
            </p>
            <div className="flex items-center justify-center gap-3">
              <button
                type="button"
                onClick={() => setShowActivationPopup(false)}
                className="px-4 py-2 rounded-md border border-slate-300 text-slate-700 hover:bg-slate-100"
              >
                Close
              </button>
              <button
                type="button"
                onClick={() => navigate('/login')}
                className="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700"
              >
                Go to Login
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Signup;
