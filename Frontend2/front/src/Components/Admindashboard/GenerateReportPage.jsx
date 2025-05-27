import React, { useState } from "react";
import "./GenerateReportPage.css";
import { saveAs } from "file-saver";
import axios from "axios";

const GenerateReportPage = () => {
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");

  const generateExcelReport = async () => {
    if (!fromDate || !toDate) {
      alert("Please select both 'from' and 'to' dates.");
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/api/reports/range?from=${fromDate}&to=${toDate}`,
        {
          responseType: "blob",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("userToken")}`,
          },
        }
      );

      const blob = new Blob([response.data], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });

      saveAs(blob, `Accounting_Report_${fromDate}_to_${toDate}.xlsx`);
    } catch (error) {
      console.error("Failed to generate report:", error);
      alert("An error occurred while generating the report.");
    }
  };

  return (
    <div className="generate-report-container">
      <h2 className="title">Generate Report by Date Range</h2>

      <div className="input-container">
        <label>From:</label>
        <input type="date" value={fromDate} onChange={(e) => setFromDate(e.target.value)} />
        <label>To:</label>
        <input type="date" value={toDate} onChange={(e) => setToDate(e.target.value)} />
      </div>

      <button className="generate-btn" onClick={generateExcelReport}>
        Generate Report
      </button>
    </div>
  );
};

export default GenerateReportPage;
