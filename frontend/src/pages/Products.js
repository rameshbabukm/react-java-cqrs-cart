import React, { useState, useEffect } from 'react';
import { categoryAPI, productAPI, cartAPI } from '../api';

function Products() {
  const [categories, setCategories] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [customerId, setCustomerId] = useState(null);
  const [showAddForm, setShowAddForm] = useState(false);
  const [newProduct, setNewProduct] = useState({
    name: '',
    description: '',
    price: '',
    quantity: '',
    categoryId: '',
  });

  useEffect(() => {
    const storedCustomerId = localStorage.getItem('selectedCustomerId');
    setCustomerId(storedCustomerId);
    fetchCategories();
    fetchProducts();
  }, []);

  useEffect(() => {
    if (selectedCategory !== 'all') {
      fetchProductsByCategory(selectedCategory);
    } else {
      fetchProducts();
    }
  }, [selectedCategory]);

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAll();
      setCategories(response.data);
    } catch (err) {
      console.error('Failed to load categories:', err);
    }
  };

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const response = await productAPI.getAll();
      setProducts(response.data);
    } catch (err) {
      setError('Failed to load products: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const fetchProductsByCategory = async (categoryId) => {
    setLoading(true);
    try {
      const response = await productAPI.getByCategoryId(categoryId);
      setProducts(response.data);
    } catch (err) {
      console.error('Failed to load products:', err);
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = async (product) => {
    if (!customerId) {
      setError('Please select a customer first from Dashboard');
      return;
    }

    try {
      await cartAPI.addItem(customerId, {
        productId: product.id,
        quantity: 1,
      });
      setError('');
      alert('Product added to cart!');
    } catch (err) {
      setError('Failed to add to cart: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleCreateProduct = async (e) => {
    e.preventDefault();
    if (!newProduct.name || !newProduct.price || !newProduct.quantity || !newProduct.categoryId) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      await productAPI.create({
        ...newProduct,
        price: parseFloat(newProduct.price),
        quantity: parseInt(newProduct.quantity),
        categoryId: parseInt(newProduct.categoryId),
      });
      setNewProduct({
        name: '',
        description: '',
        price: '',
        quantity: '',
        categoryId: '',
      });
      setShowAddForm(false);
      fetchProducts();
    } catch (err) {
      setError('Failed to create product: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="container">
      <h1 className="page-title">Products</h1>

      {error && <div className="error">{error}</div>}

      {!customerId && (
        <div className="error">⚠️ Please select a customer from Dashboard first</div>
      )}

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <label htmlFor="category-select">Filter by Category: </label>
            <select
              id="category-select"
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              style={{ marginLeft: '10px' }}
            >
              <option value="all">All Products</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {cat.name}
                </option>
              ))}
            </select>
          </div>
          <button onClick={() => setShowAddForm(!showAddForm)}>
            {showAddForm ? 'Cancel' : 'Add New Product'}
          </button>
        </div>
      </div>

      {showAddForm && (
        <div className="card">
          <h2>Add New Product</h2>
          <form onSubmit={handleCreateProduct}>
            <div className="form-group">
              <label>Product Name *</label>
              <input
                type="text"
                value={newProduct.name}
                onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
                placeholder="Enter product name"
                required
              />
            </div>
            <div className="form-group">
              <label>Description</label>
              <input
                type="text"
                value={newProduct.description}
                onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
                placeholder="Enter description"
              />
            </div>
            <div className="form-group">
              <label>Price *</label>
              <input
                type="number"
                step="0.01"
                value={newProduct.price}
                onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })}
                placeholder="Enter price"
                required
              />
            </div>
            <div className="form-group">
              <label>Quantity *</label>
              <input
                type="number"
                value={newProduct.quantity}
                onChange={(e) => setNewProduct({ ...newProduct, quantity: e.target.value })}
                placeholder="Enter quantity"
                required
              />
            </div>
            <div className="form-group">
              <label>Category *</label>
              <select
                value={newProduct.categoryId}
                onChange={(e) => setNewProduct({ ...newProduct, categoryId: e.target.value })}
                required
              >
                <option value="">Select Category</option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
            </div>
            <button type="submit">Create Product</button>
          </form>
        </div>
      )}

      <div>
        {loading ? (
          <div className="loading">Loading products...</div>
        ) : products.length > 0 ? (
          <div className="grid">
            {products.map((product) => (
              <div key={product.id} className="product-card">
                <div className="product-image">📦</div>
                <div className="product-name">{product.name}</div>
                <div className="product-category">{product.description || 'No description'}</div>
                <div className="product-price">${product.price.toFixed(2)}</div>
                <div style={{ fontSize: '12px', color: '#666', margin: '5px 0' }}>
                  Stock: {product.quantity}
                </div>
                <div className="action-buttons">
                  <button onClick={() => handleAddToCart(product)}>
                    Add to Cart
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="no-data">No products found</div>
        )}
      </div>
    </div>
  );
}

export default Products;
