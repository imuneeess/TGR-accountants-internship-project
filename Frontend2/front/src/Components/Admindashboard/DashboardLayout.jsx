import React from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";
import AdminProfileBubble from "./AdminProfileBubble"; // ✅ Import bubble
import "./AdminDashboard.css";

const DashboardLayout = () => {
  return (
    <div className="dashboard-container">
      <Sidebar />
      <div className="dashboard-content">
        <AdminProfileBubble /> {/* ✅ Add bubble to top-right */}
        <Outlet />
      </div>
    </div>
  );
};

export default DashboardLayout;
