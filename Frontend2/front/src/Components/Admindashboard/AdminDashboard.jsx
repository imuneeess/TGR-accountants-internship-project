import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AdminDashboard.css";
import { Pie, Bar, Line, Doughnut, Radar } from "react-chartjs-2";
import "chart.js/auto";

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalAccountants: 0,
    validatedDays: 0,
    nonValidatedDays: 0,
  });
  const [activeUsers, setActiveUsers] = useState(5);

  const token = localStorage.getItem("userToken");

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/admin/stats", {
          headers: { Authorization: `Bearer ${token}` },
        });

        // Set stats in the component
        const validatedCount = response.data.validatedDays || 0;  // Ensure correct data from backend
        const nonValidatedCount = response.data.nonValidatedDays || 0;  // Ensure correct data from backend
        const totalAccountants = response.data.totalAccountants || 0;

        setStats({
          totalAccountants,
          validatedDays: validatedCount,
          nonValidatedDays: nonValidatedCount,
        });
      } catch (error) {
        console.error("Error fetching stats:", error);
      }
    };

    fetchStats();
  }, [token]);

  useEffect(() => {
    const interval = setInterval(() => {
      setActiveUsers((prev) => prev + Math.floor(Math.random() * 3 - 1));
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  const validationData = {
    labels: ["Validated", "Non-Validated"],
    datasets: [
      {
        data: [stats.validatedDays, stats.nonValidatedDays],
        backgroundColor: ["#4CAF50", "#E53935"],
      },
    ],
  };

  const validationTrendData = {
    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
    datasets: [
      {
        label: "Validations Over Time",
        data: [5, 15, 20, 30, 40, 50],
        borderColor: "#4CAF50",
        fill: false,
      },
    ],
  };

  const performanceRadar = {
    labels: ["Speed", "Accuracy", "Submissions", "Workload", "Completion Rate"],
    datasets: [
      {
        label: "Performance Metrics",
        data: [80, 70, 85, 60, 90],
        backgroundColor: "rgba(25,118,210,0.6)",
      },
    ],
  };

  const validationGrowthData = {
    labels: ["2021", "2022", "2023", "2024"],
    datasets: [
      {
        label: "Total Validations",
        data: [200, 300, 450, 600],
        backgroundColor: "#FF9800",
      },
    ],
  };

  const activeUsersHeatmap = {
    labels: ["Morning", "Afternoon", "Evening", "Night"],
    datasets: [
      {
        data: [30, 50, 40, 20],
        backgroundColor: ["#FFC107", "#03A9F4", "#8BC34A", "#FF5722"],
      },
    ],
  };

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Admin Dashboard</h1>

      <div className="dashboard-stats">
        <div className="stat-card">
          <h3>Total Accountants</h3>
          <p>{stats.totalAccountants}</p>
        </div>
        <div className="stat-card validated">
          <h3>Validated</h3>
          <p>{stats.validatedDays}</p>
        </div>
        <div className="stat-card non-validated">
          <h3>Non-Validated</h3>
          <p>{stats.nonValidatedDays}</p>
        </div>
        <div className="stat-card active-users">
          <h3>Active Users</h3>
          <p>{activeUsers}</p>
        </div>
      </div>

      <div className="dashboard-charts">
        <div className="chart-card">
          <h3>Validation Status</h3>
          <Pie data={validationData} />
        </div>
        <div className="chart-card">
          <h3>Validation Trend</h3>
          <Line data={validationTrendData} />
        </div>
        <div className="chart-card">
          <h3>Performance Metrics</h3>
          <Radar data={performanceRadar} />
        </div>
        <div className="chart-card">
          <h3>Yearly Validation Growth</h3>
          <Bar data={validationGrowthData} />
        </div>
        <div className="chart-card">
          <h3>Active Users Heatmap</h3>
          <Doughnut data={activeUsersHeatmap} />
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
