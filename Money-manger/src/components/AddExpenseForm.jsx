import React, { useState } from "react";
import Input from "./Input";
import { toast } from "react-hot-toast";

function AddExpenseForm({ onAddExpense, categories = [], categoriesLoading = false }) {

  const [expense, setExpense] = useState({
    amount: "",
    description: "",
    date: "",
    categoryId: "",
  });

  const expenseCategories = categories.filter(
    (cat) => (cat.type || "").toLowerCase() === "expense"
  );
  const selectableCategories = expenseCategories.length > 0 ? expenseCategories : categories;
  const hasCategories = selectableCategories.length > 0;

  // Update state on change
  const handleChange = (key, value) => {
    setExpense({ ...expense, [key]: value });
  };

  // Save Expense
  const handleSubmit = () => {
    if (categoriesLoading) {
      toast.error("Please wait, loading categories...");
      return;
    }

    if (!hasCategories) {
      toast.error("No expense category found. Please add expense category first.");
      return;
    }

    if (!expense.amount || !expense.categoryId) {
      toast.error("Amount and Category are required");
      return;
    }

    onAddExpense(expense); // send to parent

    // Reset form
    setExpense({
      amount: "",
      description: "",
      date: "",
      categoryId: "",
    });
  };

  return (
    <div className="p-4 space-y-4">

      <Input
        label="Amount"
        value={expense.amount}
        type="number"
        placeholder="Enter amount"
        onChange={(e) => handleChange("amount", e.target.value)}
      />

      <Input
        label="Description"
        value={expense.description}
        type="text"
        placeholder="Optional"
        onChange={(e) => handleChange("description", e.target.value)}
      />

      <Input
        label="Date"
        value={expense.date}
        type="date"
        onChange={(e) => handleChange("date", e.target.value)}
      />

      <Input
        label="Category"
        isSelect={true}
        value={expense.categoryId}
        onChange={(e) => handleChange("categoryId", e.target.value)}
        options={selectableCategories.map((cat) => ({
          value: cat.id,
          label: cat.name,
        }))}
      />

      {categoriesLoading && (
        <p className="text-sm text-gray-600 mt-1">
          Loading categories...
        </p>
      )}

      {!categoriesLoading && !hasCategories && (
        <p className="text-sm text-red-600 mt-1">
          No expense categories available. Add one in Category page.
        </p>
      )}

      {/* ✅ Save Expense Button */}
      <button
        type="button"
        onClick={handleSubmit}
        disabled={categoriesLoading}
        className={`mt-4 text-white w-full py-3 rounded-lg text-lg font-medium transition ${
          categoriesLoading
            ? "bg-purple-400 cursor-not-allowed"
            : "bg-purple-600 hover:bg-red-700"
        }`}
      >
        {categoriesLoading ? "Loading..." : "Save Expense"}
      </button>

    </div>
  );
}

export default AddExpenseForm;
