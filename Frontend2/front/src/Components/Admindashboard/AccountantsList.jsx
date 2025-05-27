import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AccountantsList.css";

const AccountantsList = () => {
  const [accountants, setAccountants] = useState([]);
  const [todayStatus, setTodayStatus] = useState([]);

  const token = localStorage.getItem("userToken");

  useEffect(() => {
    const fetchAccountants = async () => {
      try {
        const accountantsResponse = await axios.get("http://localhost:8080/api/users/accountants", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setAccountants(accountantsResponse.data);

        const todayResponse = await axios.get("http://localhost:8080/api/accounting-days/status/today", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTodayStatus(todayResponse.data);
      } catch (error) {
        console.error("Error fetching accountants:", error);
      }
    };

    fetchAccountants();
  }, [token]);

  const toggleAccountStatus = async (email) => {
    try {
      await axios.put(`http://localhost:8080/api/users/toggle-status?email=${email}`, {}, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setAccountants(prev =>
        prev.map(acc =>
          acc.email === email ? { ...acc, enabled: !acc.enabled } : acc
        )
      );
    } catch (error) {
      console.error("Failed to toggle status:", error);
    }
  };

  return (
    <div className="accountants-container">
      <h1 className="table-title">Accountants List</h1>
      <table className="accountants-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Account</th>
          </tr>
        </thead>
        <tbody>
          {accountants.map((accountant, index) => {
            const todayDay = todayStatus.find((s) => s.email === accountant.email);
            const statusText = todayDay?.validated ? "Validated" : "Not Validated";

            return (
              <tr key={index}>
                <td>{accountant.firstName} {accountant.lastName}</td>
                <td>{accountant.email}</td>
                <td>{accountant.role}</td>
                <td className={statusText === "Validated" ? "validated" : "not-validated"}>
                  {statusText}
                </td>
                <td>
                  <button
                    onClick={() => toggleAccountStatus(accountant.email)}
                    className={accountant.enabled ? "enabled-btn" : "disabled-btn"}
                  >
                    {accountant.enabled ? "Enabled" : "Disabled"}
                  </button>
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
