import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";

const UserHome = () => {
  const [currentTime, setCurrentTime] = useState(new Date());
  const [days, setDays] = useState([]);
  const [selectedDay, setSelectedDay] = useState(null);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("userToken");
  const email = localStorage.getItem("userEmail");

  const itemsPerPage = 10;
  const [currentPage, setCurrentPage] = useState(1);
  const totalPages = Math.ceil(days.length / itemsPerPage);
  const paginatedDays = days.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  useEffect(() => {
    const timer = setInterval(() => setCurrentTime(new Date()), 1000);
    return () => clearInterval(timer);
  }, []);

  const fetchDays = useCallback(async () => {
    setLoading(true);
    try {
      const response = await axios.get(`http://localhost:8080/api/accounting-report/${email}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("📦 Backend days fetched:", response.data);
      setDays(response.data || []);
    } catch (error) {
      console.error("❌ Failed to fetch accounting days", error);
      alert("Failed to load data. Check console for details.");
    }
    setLoading(false);
  }, [token, email]);

  useEffect(() => {
    if (token && email) {
      fetchDays();
    }
  }, [fetchDays, token, email]);

  const handleSelectDay = (day) => setSelectedDay(day.date);

  const handleValidate = async () => {
    if (!selectedDay) {
      return alert("Please select a day to validate.");
    }
  
    try {
      const response = await axios.post(
        "http://localhost:8080/api/accounting-days/validate", // ✅ Correct endpoint
        {},
        {
          params: { email, date: selectedDay },
          headers: { Authorization: `Bearer ${token}` },
        }
      );
  
      console.log("✅ Validation response:", response.data);
  
      if (response.data?.validated) {
        setDays((prev) =>
          prev.map((d) => (d.date === selectedDay ? { ...d, validated: true } : d))
        );
        alert(`✅ ${selectedDay} validated successfully.`);
      } else {
        alert(`⚠️ Failed to validate ${selectedDay}.`);
      }
  
      setSelectedDay(null);
    } catch (err) {
      console.error("❌ Error validating day:", err);
      alert("Error validating day. Check console for details.");
    }
  };
  

  return (
    <div style={{ padding: "20px" }}>
      <h1>👋 Welcome!</h1>
      <h3>🕒 Current Time</h3>
      <p style={{ fontSize: "18px" }}>
        {currentTime.toLocaleDateString()} – {currentTime.toLocaleTimeString()}
      </p>

      <h3>📅 Your Journée Comptable</h3>

      {loading ? (
        <p>Loading days...</p>
      ) : days.length === 0 ? (
        <p style={{ color: "gray" }}>No data available for your account.</p>
      ) : (
        <>
          <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "10px" }}>
            <thead>
              <tr style={{ background: "#194f8a", color: "white" }}>
                <th style={{ padding: "10px" }}>Date</th>
                <th style={{ padding: "10px" }}>Status</th>
                <th style={{ padding: "10px" }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {paginatedDays.map((day) => (
                <tr
                  key={day.date}
                  style={{
                    backgroundColor: selectedDay === day.date ? "#e0e7ff" : "white",
                    cursor: "pointer",
                  }}
                  onClick={() => handleSelectDay(day)}
                >
                  <td style={{ padding: "10px" }}>{day.date}</td>
                  <td style={{ padding: "10px", color: day.validated ? "green" : "red" }}>
                    {day.validated ? "✅ Validated" : "❌ Not Validated"}
                  </td>
                  <td style={{ padding: "10px" }}>
                    {selectedDay === day.date ? "Selected" : ""}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div style={{ marginTop: "20px", display: "flex", justifyContent: "space-between" }}>
            <button
              onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
              disabled={currentPage === 1}
            >
              ◀ Previous
            </button>
            <span>Page {currentPage} of {totalPages}</span>
            <button
              onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
              disabled={currentPage === totalPages}
            >
              Next ▶
            </button>
          </div>
        </>
      )}

      <div style={{ marginTop: "30px" }}>
        <button
          onClick={handleValidate}
          style={{
            background: "#194f8a",
            color: "white",
            padding: "12px 25px",
            border: "none",
            borderRadius: "6px",
            fontWeight: "bold",
            fontSize: "16px",
            cursor: "pointer",
          }}
        >
          ✅ Validate Selected Day
        </button>
      </div>
    </div>
  );
};

export default UserHome;
