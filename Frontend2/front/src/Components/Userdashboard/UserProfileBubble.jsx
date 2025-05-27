import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./UserProfileBubble.css";

const UserProfileBubble = () => {
  const token = localStorage.getItem("userToken");
  const email = localStorage.getItem("userEmail");
  const [user, setUser] = useState({ firstName: "", lastName: "" });
  const [dropdownVisible, setDropdownVisible] = useState(false);
  const timeoutRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/users/me?email=${email}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setUser(res.data))
      .catch((err) => console.error("Error fetching user profile", err));
  }, [email, token]);

  const initials = `${user.firstName?.charAt(0) || "U"}${user.lastName?.charAt(0) || "S"}`;

  const handleAvatarEnter = () => {
    clearTimeout(timeoutRef.current);
    setDropdownVisible(true);
  };

  const handleAvatarLeave = () => {
    timeoutRef.current = setTimeout(() => {
      setDropdownVisible(false);
    }, 500);
  };

  const handleDropdownEnter = () => {
    clearTimeout(timeoutRef.current);
  };

  const handleDropdownLeave = () => {
    setDropdownVisible(false);
  };

  const handleLogout = () => {
    localStorage.removeItem("userRole");
    localStorage.removeItem("userToken");
    navigate("/");
  };

  return (
    <div className="profile-container">
      <div
        className="profile-avatar"
        onMouseEnter={handleAvatarEnter}
        onMouseLeave={handleAvatarLeave}
      >
        {initials.toUpperCase()}
      </div>

      <div
        className={`profile-dropdown ${dropdownVisible ? "visible" : ""}`}
        onMouseEnter={handleDropdownEnter}
        onMouseLeave={handleDropdownLeave}
      >
        <p><strong>{user.firstName} {user.lastName}</strong></p>
        <p className="profile-email">{email}</p>
        <hr />
        <button onClick={() => navigate("/user/settings")}>⚙️ Settings</button>
        <button onClick={handleLogout} className="logout-btn">🚪 Logout</button>
      </div>
    </div>
  );
};

export default UserProfileBubble;
