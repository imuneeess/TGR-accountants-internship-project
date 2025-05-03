import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AccountantsList.css";

const AccountantsList = () => {
  const [accountants, setAccountants] = useState([]);
  const [todayStatus, setTodayStatus] = useState([]);

  useEffect(() => {
    const fetchAccountants = async () => {
      try {
        const token = localStorage.getItem("userToken");

        // Fetch the list of accountants
        const accountantsResponse = await axios.get("http://localhost:8080/api/users/accountants", {
          headers: { Authorization: `Bearer ${token}` },
        });

        setAccountants(accountantsResponse.data);

        // Fetch today's status
        const todayResponse = await axios.get("http://localhost:8080/api/accounting-days/status/today", {
          headers: { Authorization: `Bearer ${token}` },
        });

        setTodayStatus(todayResponse.data);

      } catch (error) {
        console.error("Error fetching accountants:", error);
      }
    };

    fetchAccountants();
  }, []);

  return (
    <div className="accountants-container">
      <h1 className="table-title">Accountants List</h1>
      <table className="accountants-table">
        <thead>
          <tr>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {accountants.map((accountant, index) => {
            const todayDay = todayStatus.find((status) => status.email === accountant.email);
            const statusText = todayDay && todayDay.validated ? "Validated" : "Not Validated";
            return (
              <tr key={index}>
                <td>{accountant.email}</td>
                <td>{accountant.role}</td>
                <td className={statusText === "Validated" ? "validated" : "not-validated"}>
                  {statusText}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default AccountantsList;
