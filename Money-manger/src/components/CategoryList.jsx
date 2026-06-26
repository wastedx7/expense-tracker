import React from 'react'
import { Layers2, Pencil } from 'lucide-react'

function CategoryList({ categories, onEditCategory, onUpdateCategory }) {
  return (
    <div className="card p-4">

      <div className="flex items-center justify-between mb-4">
        <h4 className="text-lg font-semibold">Category Sources</h4>
      </div>

      {/* Category list */}
      {categories.length === 0 ? (
        <p className="text-gray-500">
          No category is added yet. Add some to get started.
        </p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {categories.map((category) => (
            <div
              key={category.id}
              className="group relative flex items-center gap-3 p-3 rounded-lg hover:bg-gray-100/60"
            >
              {/* Icon */}
              <div className="w-12 h-12 flex items-center justify-center text-xl text-gray-800 bg-gray-100 rounded-full">
                {category.icon ? (
                  <img
                    src={category.icon}
                    alt={category.name}
                    className="h-5 w-5"
                  />
                ) : (
                  <Layers2 className="text-purple-600" size={24} />
                )}
              </div>

              {/* Category details */}
              <div>
                <h4 className="font-medium">{category.name}</h4>
                <p className="text-sm text-gray-500">{category.type}</p>
              </div>

              {/* Action Button */}
              <button
                type="button"
                className="absolute right-3 opacity-0 group-hover:opacity-100 transition"
                onClick={() => onEditCategory(category)}
              >
                <Pencil className="text-gray-600" size={18} />
              </button>
            </div>
          ))}
        </div>
      )}

    </div>
  )
}

export default CategoryList
