import React, { useState } from 'react';
import { useNavigate, Link } from "react-router-dom";
import './LoginForm.css';
import { FaUserAlt, FaLock } from "react-icons/fa";
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import 'swiper/css/pagination';
import { Pagination, Autoplay } from 'swiper/modules';
import axios from 'axios';
import logo from '../Assets/image2.png';
import illu1 from '../Assets/illu1.png';
import illu2 from '../Assets/illu2.png';   

const LoginForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:8080/api/auth/login", {
        email,
        password
      }, {
        withCredentials: true
      });

      const { token, role } = response.data;

      localStorage.setItem("userToken", token);
      localStorage.setItem("userRole", role);
      localStorage.setItem("userEmail", email);

      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      if (role === "ADMIN") {
        navigate("/dashboard");
      } else if (role === "ACCOUNTANT") {
        navigate("/user/home");
      } else {
        alert("Unknown user role!");
      }

    } catch (error) {
      console.error("Login error:", error);
      alert("Invalid credentials. Please try again.");
    }
  };

  return (
    <>
      <img src={logo} alt="Logo" className="logo" />

      <div className="container">
        <div className="left-section">
          <Swiper
            modules={[Pagination, Autoplay]}
            pagination={{ clickable: true }}
            autoplay={{ delay: 3000 }}
          >
            <SwiperSlide>
              <div className="slide">
                <img src={illu1} alt="Validation of Jira Tickets" />
                <h2>Validation of Jira Tickets</h2>
                <p>Streamline your workflow by tracking and validating Jira tickets efficiently.</p>
              </div>
            </SwiperSlide>
            <SwiperSlide>
              <div className="slide">
                <img src={illu2} alt="Follow-up on Data" />
                <h2>Follow-up on Old Validation Days</h2>
                <p>Keep track of historical validation data to ensure accurate reporting.</p>
              </div>
            </SwiperSlide>
          </Swiper>
        </div>

        <div className='right-section'>
          <div className="wrapper">
            <form onSubmit={handleLogin}>
              <h1>Login</h1>
              <div className="input-box">
                <input 
                  type="email" 
                  placeholder='Email' 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required 
                />
                <FaUserAlt className='icon'/>
              </div>
              <div className="input-box">
                <input 
                  type="password" 
                  placeholder='Password' 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required 
                />
                <FaLock className='icon' />
              </div>
              <div className="remember-forgot">
                <label><input type="checkbox" />Remember me</label>
                <Link to="/forgot-password">Forgot password?</Link>
              </div>
              <button type='submit'>Login</button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default LoginForm;