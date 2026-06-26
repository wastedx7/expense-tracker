import React, { useEffect, useState } from "react";
import Dashboard from "../components/Dashboard";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINT } from "../util/apiEndpoints";
import useUser from "../hooks/useUser";
import { useNavigate } from "react-router-dom";

import { PieChart, Pie, Cell, ResponsiveContainer } from "recharts";
import { ArrowDownCircle, ArrowUpCircle } from "lucide-react";
import { toast } from "react-hot-toast";

function DashboardHome() {
  useUser();

  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [incomeList, setIncomeList] = useState([]);
  const [expenseList, setExpenseList] = useState([]);

  const totalIncome = incomeList.reduce((sum, item) => sum + item.amount, 0);
  const totalExpense = expenseList.reduce((sum, item) => sum + item.amount, 0);

  const chartData = [
    { name: "Income", value: totalIncome },
    { name: "Expense", value: totalExpense },
  ];

  const COLORS = ["#43A047", "#EF4444"];

  const fetchDashboardData = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      toast.error("Please login to continue");
      navigate("/login");
      return;
    }

    try {
      setLoading(true);

      const [incomeRes, expenseRes] = await Promise.all([
        axiosConfig.get(API_ENDPOINT.GET_ALL_INCOME),
        axiosConfig.get(API_ENDPOINT.GET_ALL_EXPENSE),
      ]);

      setIncomeList(incomeRes.data || []);
      setExpenseList(expenseRes.data || []);

    } catch (error) {
      toast.error("Failed to load dashboard data");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboardData();
  }, [navigate]);

  return (
    <Dashboard activeMenu="Dashboard">
      <h1 className="text-3xl font-semibold mb-6">Dashboard Overview</h1>

      {loading ? (
        <p className="text-lg text-gray-600">Loading...</p>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

          {/* PIE CHART */}
          <div className="bg-white shadow-md rounded-xl p-6 col-span-1 flex flex-col items-center">
            <h2 className="text-xl font-semibold mb-4">Income vs Expense</h2>

            <ResponsiveContainer width="100%" height={250}>
                <PieChart>
                <Pie
                 data={chartData}
                 cx="50%"
                  cy="50%"
                  innerRadius={40}
                  outerRadius={60}
                  paddingAngle={4}
                  dataKey="value"
                  labelLine={false}
                  label={({ percent }) =>
                `${(percent * 100).toFixed(1)}%`
                  }
                >
                  {chartData.map((_, i) => (
                    <Cell key={i} fill={COLORS[i]} />
                  ))}
                </Pie>
                </PieChart>
                </ResponsiveContainer>


            {/* Summary Labels */}
            <div className="flex justify-between gap-6 mt-4">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 bg-purple-600 rounded-full"></span>
                <p className="font-medium text-gray-700">
                  Income: ₹{totalIncome}
                </p>
              </div>

              <div className="flex items-center gap-2">
                <span className="w-3 h-3 bg-red-500 rounded-full"></span>
                <p className="font-medium text-gray-700">
                  Expense: ₹{totalExpense}
                </p>
              </div>
            </div>
          </div>

          {/* Latest Income */}
          <div className="bg-white shadow-md rounded-xl p-6">
            <h2 className="text-xl font-semibold mb-4">Latest Income</h2>

            <div className="space-y-4">
              {incomeList.slice(0, 5).map((i) => (
                <div
                  key={i.id}
                  className="flex justify-between items-center bg-green-50 border border-green-200 rounded-lg p-3"
                >
                  <div>
                    <p className="font-medium">{i.description}</p>
                    <p className="text-gray-500 text-sm">{i.date}</p>
                  </div>

                  <div className="flex items-center gap-2 text-green-600 font-semibold">
                    <ArrowUpCircle size={20} />
                    ₹{i.amount}
                  </div>
                </div>
              ))}

              {incomeList.length === 0 && (
                <p className="text-gray-500">No income recorded.</p>
              )}
            </div>
          </div>

          {/* Latest Expense */}
          <div className="bg-white shadow-md rounded-xl p-6">
            <h2 className="text-xl font-semibold mb-4">Latest Expense</h2>

            <div className="space-y-4">
              {expenseList.slice(0, 5).map((e) => (
                <div
                  key={e.id}
                  className="flex justify-between items-center bg-red-50 border border-red-200 rounded-lg p-3"
                >
                  <div>
                    <p className="font-medium">{e.description}</p>
                    <p className="text-gray-500 text-sm">{e.date}</p>
                  </div>

                  <div className="flex items-center gap-2 text-red-600 font-semibold">
                    <ArrowDownCircle size={20} />
                    ₹{e.amount}
                  </div>
                </div>
              ))}

              {expenseList.length === 0 && (
                <p className="text-gray-500">No expense recorded.</p>
              )}
            </div>
          </div>

        </div>
      )}
    </Dashboard>
  );
}

export default DashboardHome;
