import React from "react";
import { Outlet } from "react-router-dom";
import UserSidebar from "./UserSidebar";
import "../Admindashboard/AdminDashboard.css";
import UserProfileBubble from "../Userdashboard/UserProfileBubble"; // ✅ Import

const UserDashboardLayout = () => {
  return (
    <div className="dashboard-container" style={{ position: "relative" }}>
      <UserSidebar />
      <div className="dashboard-content">
        <UserProfileBubble /> {/* ✅ Add here */}
        <Outlet />
      </div>
    </div>
  );
};

export default UserDashboardLayout;
