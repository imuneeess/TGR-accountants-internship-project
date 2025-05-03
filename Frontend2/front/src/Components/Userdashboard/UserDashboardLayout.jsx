import React from "react";
import { Outlet } from "react-router-dom";
import UserSidebar from "./UserSidebar";
import "../Admindashboard/AdminDashboard.css";

const UserDashboardLayout = () => {
  return (
    <div className="dashboard-container">
      <UserSidebar />
      <div className="dashboard-content">
        <Outlet />
      </div>
    </div>
  );
};

export default UserDashboardLayout;
