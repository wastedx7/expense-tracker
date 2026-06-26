import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";

function Input({ label, value, onChange, placeholder, type, isSelect = false, options = [] }) {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="mb-4">

      <label className="text-[13px] text-slate-800 block mb-1">
        {label}
      </label>

      <div className="relative">

        {/* Select Input */}
        {isSelect ? (
          <select
            className="w-full bg-white border border-gray-300 rounded-md py-2 px-3 text-gray-700 focus:border-blue-500"
            value={value}
            onChange={onChange}
          >
            <option value="">-- Select --</option>

            {options.map((opt) => (
              <option key={opt.value} value={opt.value}>
                {opt.label}
              </option>
            ))}
          </select>
        ) : (
          /* Normal Text Input */
          <input
            className="w-full bg-white border border-gray-300 rounded-md py-2 px-3 pr-10 text-gray-700 focus:border-blue-500"
            type={type === "password" ? (showPassword ? "text" : "password") : type}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
          />
        )}

        {/* Password Icon */}
        {type === "password" && !isSelect && (
          <span
            className="absolute right-3 top-1/2 -translate-y-1/2 cursor-pointer"
            onClick={() => setShowPassword(!showPassword)}
          >
            {showPassword ? (
              <EyeOff size={20} className="text-blue-600" />
            ) : (
              <Eye size={20} className="text-blue-600" />
            )}
          </span>
        )}

      </div>
    </div>
  );
}

export default Input;
