import React, { useEffect, useContext } from "react";
import { AppContext } from "../context/AppContext";
import { useNavigate } from "react-router-dom";
import { API_ENDPOINT } from "../util/apiEndpoints";
import axiosConfig from "../util/axiosConfig";  // FIXED

function useUser() {
  const { user, setUser, clearUser } = useContext(AppContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (user) return; // FIXED

    const token = localStorage.getItem("token");
    if (!token) {
      clearUser?.();
      navigate("/login");
      return;
    }

    let isMounted = true;

    const fetchUserInfo = async () => {
      try {
        const response = await axiosConfig.get(API_ENDPOINT.GET_USER_INFO);

        if (isMounted && response.data) {
          
          setUser(response.data);
        }
      } catch (error) {
        console.log("Failed to fetch the user info");

        if (isMounted) {
          localStorage.removeItem("token");
          clearUser?.();
          navigate("/login");
        }
      }
    };

    fetchUserInfo();

    return () => {
      isMounted = false;
    };
  }, [user, setUser, clearUser, navigate]); // FIXED

  return user;  // FIXED
}

export default useUser;
