import { X } from 'lucide-react';
import React from 'react';

function Modal({ children, onClose, isOpen, title }) {
  if (!isOpen) return null;

  return (
    <div className="inset-0 fixed z-50 overflow-hidden bg-black/40 flex items-center justify-center w-full h-full backdrop-blur-sm">
      
      <div className="relative w-full max-w-2xl max-h-[90vh] p-4">
        
        {/* Modal Box */}
        <div className="relative bg-white rounded-xl shadow-2xl border border-gray-100">

          {/* Header */}
          <div className="flex items-center justify-between p-5 md:p-6 border-b border-gray-100 rounded-t-xl">
            <h3 className="text-xl font-semibold text-gray-800">
              {title}
            </h3>

            <button
              onClick={onClose}
              type="button"
              className="inline-flex items-center justify-center bg-gray-50 text-gray-500 
                        hover:bg-gray-100 hover:text-gray-700 rounded-full w-9 h-9
                        transition-colors duration-200 cursor-pointer
                        focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              <X className="w-4 h-4" size={50}/>
            </button>
          </div>

          {/* Modal body */}
          <div className="p-5 md:p-6 text-gray-700">
            {children}
          </div>

        </div>
      </div>
    </div>
  );
}

export default Modal;
