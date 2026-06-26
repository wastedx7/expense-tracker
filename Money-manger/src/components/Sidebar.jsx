import React, { useContext } from "react";
import { AppContext } from "../context/AppContext";
import { User } from "lucide-react";
import { SIDE_BAR_DATA } from "../util/assets";
import {useNavigate} from "react-router-dom";

function Sidebar({activeMenu}) {
  const { user } = useContext(AppContext);
  const navigate = useNavigate();

  return (
    <div className="w-64 h-[calc(100vh-61px)] bg-white border border-gray-200/50 p-5 sticky top-[61px] z-20">

      <div className="flex flex-col items-center justify-center gap-3 mt-3 mb-7">

        {user ? (
          user.profileImgUrl ? (
            <img
              src={user.profileImgUrl}
              alt="profile"
              className="w-20 h-20 bg-slate-400 rounded-full"
            />
          ) : (
            <User className="w-20 h-20 text-purple-600" />
          )
        ) : null}

        <h5 className="text-gray-950 font-medium leading-6">
          {user?.fullName || ""}
        </h5>

      </div>

      {SIDE_BAR_DATA.map((item, index) => (
        <button
          type="button"
          onClick={() => navigate(item.path)}
          key={`menu_${index}`}
          className={`
                    w-full cursor-pointer text-[15px] py-3 px-6 rounded-lg mb-3 flex items-center gap-4 transition
                    ${activeMenu === item.label 
                    ? "bg-purple-800 text-white" 
                    : "text-gray-800 hover:bg-gray-100"}
`}
        >
          <item.icon className="text-xl" />
          <span>{item.label}</span>
        </button>
      ))}
      
    </div>
  );
}

export default Sidebar;
