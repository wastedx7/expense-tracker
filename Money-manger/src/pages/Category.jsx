import React, { useState, useEffect } from 'react';
import Dashboard from '../components/Dashboard';
import useUser from '../hooks/useUser';
import { Plus } from 'lucide-react';
import axiosConfig from '../util/axiosConfig';
import { API_ENDPOINT } from '../util/apiEndpoints';
import CategoryList from '../components/CategoryList';
import { toast } from 'react-hot-toast';
import Model from '../components/Model';
import AddCategoryForm from '../components/AddCategoryForm';

function Category() {

  useUser();

  const [loading, setLoading] = useState(false);
  const [categoryData, setCategoryData] = useState([]);
  const [openAddCategoryModel, setOpenAddCategoryModel] = useState(false);

  // Fetch categories
  const fetchCategoryDetail = async () => {
    try {
      const response = await axiosConfig.get(API_ENDPOINT.GET_ALL_CATEGORIES);
      if (response.status === 200) {
        setCategoryData(response.data);
      }
    } catch (error) {
      toast.error("Unable to load categories");
    }
  };

  useEffect(() => {
    fetchCategoryDetail();
  }, []);

  // Save category
  const handleAddCategory = async (category) => {
    try {
      const response = await axiosConfig.post(API_ENDPOINT.SAVE_CATEGORIES, category);
      toast.success("Category added successfully!");

      fetchCategoryDetail();   // refresh list
      setOpenAddCategoryModel(false);

    } catch (error) {
      toast.error("Failed to add category");
    }
  };

  return (
    <Dashboard activeMenu="Category">
      <div className="my-5 mx-auto">

        <div className="flex justify-between mb-5">
          <h2 className="text-2xl font-semibold">All Categories</h2>
          <button 
            type="button"
            className="flex add-btn items-center gap-1"
            onClick={() => setOpenAddCategoryModel(true)}
          >
            <Plus size={15} />
            Add Category
          </button>
        </div>

        <CategoryList categories={categoryData} />

        {/* Add Category Modal */}
        <Model
          isOpen={openAddCategoryModel}
          onClose={() => setOpenAddCategoryModel(false)}
          title="Add New Category"
        >
          <AddCategoryForm onAddCategory={handleAddCategory} />
        </Model>

      </div>
    </Dashboard>
  );
}

export default Category;
