import React, { useState, useEffect } from 'react';
import { customerAPI } from '../api';

function Dashboard() {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({ name: '', email: '' });
  const [selectedCustomer, setSelectedCustomer] = useState(null);

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await customerAPI.getAll();
      setCustomers(response.data);
    } catch (err) {
      setError('Failed to load customers: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.email) {
      setError('Please fill in all fields');
      return;
    }

    try {
      await customerAPI.create(formData);
      setFormData({ name: '', email: '' });
      fetchCustomers();
      setError('');
    } catch (err) {
      setError('Failed to create customer: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleSelectCustomer = (customer) => {
    setSelectedCustomer(customer);
    localStorage.setItem('selectedCustomerId', customer.id);
  };

  return (
    <div className="container">
      <h1 className="page-title">Dashboard</h1>
      
      {error && <div className="error">{error}</div>}

      <div className="card">
        <h2>Create New Customer</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Customer Name</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="Enter customer name"
            />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              placeholder="Enter email"
            />
          </div>
          <button type="submit">Create Customer</button>
        </form>
      </div>

      <div className="card">
        <h2>Customers</h2>
        {loading ? (
          <div className="loading">Loading customers...</div>
        ) : customers.length > 0 ? (
          <div className="grid">
            {customers.map((customer) => (
              <div
                key={customer.id}
                className="product-card"
                onClick={() => handleSelectCustomer(customer)}
                style={{
                  border: selectedCustomer?.id === customer.id ? '2px solid #007bff' : '1px solid #ddd',
                  backgroundColor: selectedCustomer?.id === customer.id ? '#f0f8ff' : 'white',
                }}
              >
                <div className="product-image" style={{ fontSize: '32px' }}>👤</div>
                <div className="product-name">{customer.name}</div>
                <div className="product-category">{customer.email}</div>
                <div style={{ fontSize: '12px', color: '#666', marginTop: '10px' }}>
                  ID: {customer.id}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="no-data">No customers found. Create one above!</div>
        )}
      </div>

      {selectedCustomer && (
        <div className="card" style={{ backgroundColor: '#e7f3ff', borderLeft: '4px solid #007bff' }}>
          <h3>Selected Customer</h3>
          <p><strong>ID:</strong> {selectedCustomer.id}</p>
          <p><strong>Name:</strong> {selectedCustomer.name}</p>
          <p><strong>Email:</strong> {selectedCustomer.email}</p>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
