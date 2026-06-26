import React from "react";
import { Trash, Layers2 } from "lucide-react";

function IncomeList({ incomes, onDelete }) {
  return (
    <div className="card p-4">

      <h4 className="text-lg font-semibold mb-4">Income Records</h4>

      {incomes.length === 0 ? (
        <p className="text-gray-500">No incomes added yet.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">

          {incomes.map((income) => (
            <div 
              key={income.id} 
              className="group relative p-4 rounded-lg border border-gray-200 bg-white shadow-sm hover:shadow-md transition">
              
              {/* Icon / emoji */}
              <div className="w-12 h-12 flex items-center justify-center text-xl bg-gray-100 rounded-full mb-3">
                {income.categoryIcon ? (
                  <img src={income.categoryIcon} alt="icon" className="h-5 w-5" />
                ) : (
                  <Layers2 className="text-purple-600" size={24} />
                )}
              </div>

              {/* Income details */}
              <p className="text-xl font-bold text-gray-900">₹ {income.amount}</p>

              <p className="text-gray-700 mt-1 font-medium">
                {income.categoryName}
              </p>

              <p className="text-sm text-gray-500 mt-1">
                {income.description || "No description"}
              </p>

              <p className="text-xs text-gray-400 mt-1">
                {income.date}
              </p>

              {/* Delete Button */}
              <button
                type="button"
                onClick={() => onDelete(income.id)}
                className="absolute top-3 right-3 opacity-0 group-hover:opacity-100 transition bg-red-50 hover:bg-red-100 text-red-600 p-2 rounded-full"
              >
                <Trash size={16} />
              </button>

            </div>
          ))}

        </div>
      )}

    </div>
  );
}

export default IncomeList;
