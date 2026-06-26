import React, { useState } from "react";
import Input from "./Input";
import EmojiPickerPopUp from "./EmojiPickerPopUp";
import { toast } from "react-hot-toast";

function AddCategoryForm({ onAddCategory }) {

  const [category, setCategory] = useState({
    name: "",
    type: "income",
    icon: ""
  });

  const categoryTypeOptions = [
    { value: "income", label: "Income" },
    { value: "expense", label: "Expense" }
  ];

  const handleChange = (key, value) => {
    setCategory({ ...category, [key]: value });
  };

  const handleSubmit = () => {
    if (!category.name.trim()) {
      toast.error("Category name is required");
      return;
    }

    onAddCategory(category);  // send to parent

    // Reset form
    setCategory({
      name: "",
      type: "income",
      icon: ""
    });
  };

  return (
    <div className="p-4">

      <EmojiPickerPopUp
        icon={category.icon}
        onSelect={(icon) => handleChange("icon", icon)}
      />

      <Input
        label="Category Name"
        value={category.name}
        onChange={(e) => handleChange("name", e.target.value)}
        placeholder="e.g., Salary, Groceries"
        type="text"
      />

      <Input
        label="Category Type"
        value={category.type}
        onChange={(e) => handleChange("type", e.target.value)}
        isSelect={true}
        options={categoryTypeOptions}
      />

      <button
        type="button"
        onClick={handleSubmit}
        className="mt-4 bg-purple-600 text-white w-full py-3 rounded-lg text-lg font-medium hover:bg-purple-700 transition"
      >
        Save Category
      </button>

    </div>
  );
}

export default AddCategoryForm;
