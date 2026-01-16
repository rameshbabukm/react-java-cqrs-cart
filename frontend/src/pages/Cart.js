import React, { useState, useEffect } from 'react';
import { cartAPI } from '../api';

function Cart() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [customerId, setCustomerId] = useState(null);

  useEffect(() => {
    const storedCustomerId = localStorage.getItem('selectedCustomerId');
    setCustomerId(storedCustomerId);
    if (storedCustomerId) {
      fetchCart(storedCustomerId);
    }
  }, []);

  const fetchCart = async (cId) => {
    setLoading(true);
    setError('');
    try {
      const response = await cartAPI.getByCustomerId(cId);
      setCart(response.data);
    } catch (err) {
      if (err.response?.status === 404) {
        setCart(null);
      } else {
        setError('Failed to load cart: ' + (err.response?.data?.message || err.message));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveItem = async (productId) => {
    if (!customerId) return;
    try {
      await cartAPI.removeItem(customerId, productId);
      fetchCart(customerId);
    } catch (err) {
      setError('Failed to remove item: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleUpdateQuantity = async (productId, newQuantity) => {
    if (newQuantity <= 0) {
      handleRemoveItem(productId);
      return;
    }
    if (!customerId) return;
    try {
      await cartAPI.updateItem(customerId, productId, { quantity: newQuantity });
      fetchCart(customerId);
    } catch (err) {
      setError('Failed to update quantity: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleCheckout = async () => {
    if (!customerId) return;
    try {
      const response = await cartAPI.checkout(customerId);
      alert('Checkout successful! Order ID: ' + response.data.id);
      setCart(null);
      fetchCart(customerId);
    } catch (err) {
      setError('Failed to checkout: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="container">
      <h1 className="page-title">Shopping Cart</h1>

      {error && <div className="error">{error}</div>}

      {!customerId && (
        <div className="error">⚠️ Please select a customer from Dashboard first</div>
      )}

      {loading ? (
        <div className="loading">Loading cart...</div>
      ) : !cart || !cart.items || cart.items.length === 0 ? (
        <div className="no-data">Your cart is empty</div>
      ) : (
        <>
          <table className="cart-table">
            <thead>
              <tr>
                <th>Product Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {cart.items.map((item) => (
                <tr key={item.productId}>
                  <td>{item.productName}</td>
                  <td>${item.price.toFixed(2)}</td>
                  <td>
                    <input
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={(e) =>
                        handleUpdateQuantity(item.productId, parseInt(e.target.value))
                      }
                      style={{ width: '60px' }}
                    />
                  </td>
                  <td>${(item.price * item.quantity).toFixed(2)}</td>
                  <td>
                    <button
                      onClick={() => handleRemoveItem(item.productId)}
                      style={{ backgroundColor: '#dc3545' }}
                    >
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="cart-summary">
            <div className="summary-row">
              <span>Subtotal:</span>
              <span>${cart.totalPrice.toFixed(2)}</span>
            </div>
            <div className="summary-row" style={{ borderBottom: 'none' }}>
              <span>Tax (0%):</span>
              <span>$0.00</span>
            </div>
            <div className="summary-total">
              Total: ${cart.totalPrice.toFixed(2)}
            </div>
            <button
              onClick={handleCheckout}
              style={{
                width: '100%',
                marginTop: '20px',
                backgroundColor: '#28a745',
                padding: '12px',
              }}
            >
              Proceed to Checkout
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default Cart;
