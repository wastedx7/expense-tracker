import React from "react";
import { Trash, Layers2 } from "lucide-react";

function ExpenseList({ expenses, onDelete }) {
  return (
    <div className="card p-4">

      <h4 className="text-lg font-semibold mb-4">Expense Records</h4>

      {expenses.length === 0 ? (
        <p className="text-gray-500">No expenses added yet.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">

          {expenses.map((expense) => (
            <div 
              key={expense.id} 
              className="group relative p-4 rounded-lg border border-gray-200 bg-white shadow-sm hover:shadow-md transition">

              {/* Icon */}
              <div className="w-12 h-12 flex items-center justify-center text-xl bg-gray-100 rounded-full mb-3">
                {expense.categoryIcon ? (
                  <img src={expense.categoryIcon} alt="icon" className="h-5 w-5" />
                ) : (
                  <Layers2 className="text-red-600" size={24} />
                )}
              </div>

              {/* Expense Details */}
              <p className="text-xl font-bold text-gray-900">₹ {expense.amount}</p>

              <p className="text-gray-700 mt-1 font-medium">
                {expense.categoryName}
              </p>

              <p className="text-sm text-gray-500 mt-1">
                {expense.description || "No description"}
              </p>

              <p className="text-xs text-gray-400 mt-1">
                {expense.date}
              </p>

              {/* Delete btn */}
              <button
                type="button"
                onClick={() => onDelete(expense.id)}
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

export default ExpenseList;
