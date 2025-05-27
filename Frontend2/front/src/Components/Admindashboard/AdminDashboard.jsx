// AdminDashboard.jsx
import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AdminDashboard.css";
import { Pie, Line, Radar } from "react-chartjs-2";
import "chart.js/auto";

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalAccountants: 0,
    validatedDays: 0,
    nonValidatedDays: 0,
  });

  const [dailyValidations, setDailyValidations] = useState([]);

  const token = localStorage.getItem("userToken");

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [accRes, todayRes] = await Promise.all([
          axios.get("http://localhost:8080/api/users/accountants", {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get("http://localhost:8080/api/accounting-days/status/today", {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);

        const accountants = accRes.data || [];
        const todayStatus = todayRes.data || [];

        const totalAccountants = accountants.length;
        const validatedDays = todayStatus.filter((s) => s.validated).length;
        const nonValidatedDays = totalAccountants - validatedDays;

        setStats({ totalAccountants, validatedDays, nonValidatedDays });
      } catch (error) {
        console.error("Error fetching dashboard stats:", error);
      }
    };

    const fetchValidationTrend = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/accounting-days", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const allDays = res.data || [];

        const byDate = {};
        allDays.forEach((d) => {
          byDate[d.date] = byDate[d.date] || { total: 0, validated: 0 };
          byDate[d.date].total++;
          if (d.validated) byDate[d.date].validated++;
        });

        const dates = Object.keys(byDate).sort().slice(-7);
        const trend = dates.map((date) => ({
          date,
          value: byDate[date].validated,
        }));

        setDailyValidations(trend);
      } catch (err) {
        console.error("Failed to fetch validations by day", err);
      }
    };

    fetchStats();
    fetchValidationTrend();
  }, [token]);

  const validationPie = {
    labels: ["Validated", "Not Validated"],
    datasets: [
      {
        data: [stats.validatedDays, stats.nonValidatedDays],
        backgroundColor: ["#4CAF50", "#F44336"],
      },
    ],
  };

  const weeklyTrend = {
    labels: dailyValidations.map((d) => d.date),
    datasets: [
      {
        label: "Validated Entries (Last 7 Days)",
        data: dailyValidations.map((d) => d.value),
        backgroundColor: "#2196F3",
        borderColor: "#1976D2",
        fill: false,
        tension: 0.3,
      },
    ],
  };

  const radarStats = {
    labels: ["Total Accountants", "Validated", "Not Validated"],
    datasets: [
      {
        label: "Validation Overview",
        data: [stats.totalAccountants, stats.validatedDays, stats.nonValidatedDays],
        backgroundColor: "rgba(33, 150, 243, 0.4)",
        borderColor: "#2196F3",
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
          <h3>Validated Today</h3>
          <p>{stats.validatedDays}</p>
        </div>

        <div className="stat-card non-validated">
          <h3>Not Validated Today</h3>
          <p>{stats.nonValidatedDays}</p>
        </div>
      </div>

      <div className="dashboard-charts">
        <div className="chart-card">
          <h3>Validation Status</h3>
          <Pie data={validationPie} />
        </div>

        <div className="chart-card">
          <h3>Last 7 Days Trend</h3>
          <Line data={weeklyTrend} />
        </div>

        <div className="chart-card">
          <h3>Validation Overview</h3>
          <Radar data={radarStats} />
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
