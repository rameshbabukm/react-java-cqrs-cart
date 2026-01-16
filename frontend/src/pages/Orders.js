import React, { useState, useEffect } from 'react';
import { orderAPI } from '../api';

function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [customerId, setCustomerId] = useState(null);

  useEffect(() => {
    const storedCustomerId = localStorage.getItem('selectedCustomerId');
    setCustomerId(storedCustomerId);
    if (storedCustomerId) {
      fetchOrders(storedCustomerId);
    }
  }, []);

  const fetchOrders = async (cId) => {
    setLoading(true);
    setError('');
    try {
      const response = await orderAPI.getByCustomerId(cId);
      setOrders(response.data);
    } catch (err) {
      setError('Failed to load orders: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1 className="page-title">My Orders</h1>

      {error && <div className="error">{error}</div>}

      {!customerId && (
        <div className="error">⚠️ Please select a customer from Dashboard first</div>
      )}

      {loading ? (
        <div className="loading">Loading orders...</div>
      ) : orders.length > 0 ? (
        <div>
          {orders.map((order) => (
            <div key={order.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                <div>
                  <h3>Order #{order.id}</h3>
                  <p style={{ color: '#666', margin: '5px 0' }}>
                    Order Date: {new Date(order.orderDate).toLocaleDateString()}
                  </p>
                </div>
                <div
                  style={{
                    padding: '10px 15px',
                    backgroundColor: '#d4edda',
                    color: '#155724',
                    borderRadius: '4px',
                    fontWeight: 'bold',
                  }}
                >
                  {order.status || 'PENDING'}
                </div>
              </div>

              <table className="cart-table" style={{ marginTop: '15px', marginBottom: '15px' }}>
                <thead>
                  <tr>
                    <th>Product Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Subtotal</th>
                  </tr>
                </thead>
                <tbody>
                  {order.items && order.items.map((item, idx) => (
                    <tr key={idx}>
                      <td>{item.productName}</td>
                      <td>${item.price.toFixed(2)}</td>
                      <td>{item.quantity}</td>
                      <td>${(item.price * item.quantity).toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>

              <div style={{ textAlign: 'right', fontSize: '18px', fontWeight: 'bold' }}>
                Total: ${order.totalPrice.toFixed(2)}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="no-data">No orders found</div>
      )}
    </div>
  );
}

export default Orders;
