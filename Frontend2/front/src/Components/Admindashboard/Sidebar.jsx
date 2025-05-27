import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { FaHome, FaUsers, FaFileAlt, FaUserPlus, FaSignOutAlt, FaBars, FaCog } from "react-icons/fa";
import "./Sidebar.css";

// ✅ Updated menuItems with Admin Settings
const menuItems = [
  { path: "/dashboard", icon: <FaHome />, label: "Dashboard" },
  { path: "/accountants", icon: <FaUsers />, label: "Accountants List" },
  { path: "/generate-report", icon: <FaFileAlt />, label: "Generate Report" },
  { path: "/add-user", icon: <FaUserPlus />, label: "Add User" },
  { path: "/settings", icon: <FaCog />, label: "Settings" }, // ✅ New settings entry
];

const Sidebar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [activeIndex, setActiveIndex] = useState(0);

  useEffect(() => {
    const current = menuItems.findIndex(item =>
      location.pathname.startsWith(item.path)
    );
    if (current !== -1) setActiveIndex(current);
  }, [location.pathname]);

  const handleNavigate = (index, path) => {
    setActiveIndex(index);
    navigate(path);
  };

  const handleLogout = () => {
    localStorage.removeItem("userRole");
    localStorage.removeItem("userToken");
    navigate("/");
  };

  return (
    <aside className="sidebar">
      <div className="inner">
        <div className="header">
          <FaBars className="menu-icon" />
          <h1>Admin Panel</h1>
        </div>

        <nav className="menu" style={{ "--top": `${activeIndex * 56}px` }}>
          {menuItems.map((item, index) => (
            <button
              key={item.path}
              className={activeIndex === index ? "active" : ""}
              onClick={() => handleNavigate(index, item.path)}
            >
              <span>{item.icon}</span>
              <p>{item.label}</p>
            </button>
          ))}
        </nav>

        <div className="logout-section">
          <button onClick={handleLogout}>
            <span><FaSignOutAlt /></span>
            <p>Logout</p>
          </button>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
