import React, { useState } from 'react';
import axios from 'axios';
import './LoginForm.css';
import logo from '../Assets/image2.png';
import illu1 from '../Assets/illu1.png'; // same image used in login

const ForgotPasswordForm = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8080/api/auth/forgot-password', email, {
        headers: { 'Content-Type': 'text/plain' }
      });
      setMessage(res.data);
    } catch (err) {
      setMessage(err.response?.data || 'Something went wrong.');
    }
  };

  return (
    <>
      <img src={logo} alt="Logo" className="logo" />
      <div className="container">
        <div className="left-section">
          <div className="slide">
            <img src={illu1} alt="Validation" />
            <h2>Secure Access</h2>
            <p>Request a new password securely and get back to work.</p>
          </div>
        </div>

        <div className="right-section">
          <div className="wrapper">
            <form onSubmit={handleSubmit}>
              <h1>Forgot Password</h1>
              <div className="input-box">
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <button type="submit">Send New Password</button>
              {message && <p style={{ marginTop: '10px' }}>{message}</p>}
              <p style={{ marginTop: '20px' }}>
                <a href="/">Back to Login</a>
              </p>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default ForgotPasswordForm;
