import React, { useEffect, useState } from "react";
import Dashboard from "../components/Dashboard";
import useUser from "../hooks/useUser";
import { Plus } from "lucide-react";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINT } from "../util/apiEndpoints";
import { toast } from "react-hot-toast";
import ExpenseList from "../components/ExpenseList";
import Model from "../components/Model";
import AddExpenseForm from "../components/AddExpenseForm";

function Expense() {

  useUser();

  const [expenseData, setExpenseData] = useState([]);
  const [categories, setCategories] = useState([]);
  const [categoriesLoading, setCategoriesLoading] = useState(true);
  const [openAddExpenseModal, setOpenAddExpenseModal] = useState(false);

  const fetchExpense = async () => {
    try {
      const response = await axiosConfig.get(API_ENDPOINT.GET_ALL_EXPENSE);
      if (response.status === 200) {
        setExpenseData(response.data);
      }
    } catch (error) {
      toast.error("Failed to fetch expenses");
    }
  };

  useEffect(() => {
    fetchExpense();
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      setCategoriesLoading(true);
      const response = await axiosConfig.get(API_ENDPOINT.GET_ALL_CATEGORIES);
      if (response.status === 200) {
        setCategories(response.data);
      }
    } catch (error) {
      toast.error("Failed to fetch categories");
    } finally {
      setCategoriesLoading(false);
    }
  };

  const handleAddExpense = async (expense) => {
    try {
      await axiosConfig.post(API_ENDPOINT.CREATE_EXPENSE, expense);
      toast.success("Expense added");
      fetchExpense();
      setOpenAddExpenseModal(false);
    } catch (error) {
      toast.error("Failed to add expense");
    }
  };

  const handleDeleteExpense = async (id) => {
    try {
      await axiosConfig.delete(API_ENDPOINT.DELETE_EXPENSE + id);
      toast.success("Expense deleted");
      fetchExpense();
    } catch (error) {
      toast.error("Failed to delete expense");
    }
  };

  return (
    <Dashboard activeMenu="Expense">
      <div className="my-5 mx-auto">

        <div className="flex justify-between mb-5">
          <h2 className="text-2xl font-semibold">All Expenses</h2>

          <button
            type="button"
            className="flex add-btn items-center gap-1"
            onClick={() => setOpenAddExpenseModal(true)}
          >
            <Plus size={15} />
            Add Expense
          </button>
        </div>

        <ExpenseList expenses={expenseData} onDelete={handleDeleteExpense} />

        <Model
          isOpen={openAddExpenseModal}
          onClose={() => setOpenAddExpenseModal(false)}
          title="Add New Expense"
        >
          <AddExpenseForm onAddExpense={handleAddExpense} categories={categories} categoriesLoading={categoriesLoading} />
        </Model>

      </div>
    </Dashboard>
  );
}

export default Expense;
