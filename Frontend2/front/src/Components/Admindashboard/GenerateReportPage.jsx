import React, { useState } from "react";
import "./GenerateReportPage.css";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import accountantsData from "../../Components/Admindashboard/accountantsData.json";
import { Pie, Bar } from "react-chartjs-2";
import "chart.js/auto";

const GenerateReportPage = () => {
  const [selectedDate, setSelectedDate] = useState("");

 
  const validationData = {
    labels: ["Validated", "Not Validated"],
    datasets: [
      {
        data: [
          accountantsData.filter((acc) => acc.validated).length,
          accountantsData.filter((acc) => !acc.validated).length,
        ],
        backgroundColor: ["#4CAF50", "#E53935"],
      },
    ],
  };

  const accountantsPerRegion = {
    labels: [...new Set(accountantsData.map((acc) => acc.REGION))], 
    datasets: [
      {
        label: "Number of Accountants",
        data: accountantsData.reduce((acc, item) => {
          acc[item.REGION] = (acc[item.REGION] || 0) + 1;
          return acc;
        }, {}),
        backgroundColor: "#1976D2",
      },
    ],
  };

  
  const generateExcelReport = () => {
    if (!selectedDate) {
      alert("Please select a date before generating the report.");
      return;
    }

    const ws = XLSX.utils.json_to_sheet(accountantsData);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Accountants List");

    
    const excelBuffer = XLSX.write(wb, { bookType: "xlsx", type: "array" });
    const file = new Blob([excelBuffer], { type: "application/octet-stream" });
    saveAs(file, `Report_${selectedDate}.xlsx`);
  };

  return (
    <div className="generate-report-container">
      <h2 className="title">Generate Report</h2>

      <div className="input-container">
        <label>Select Date:</label>
        <input
          type="date"
          value={selectedDate}
          onChange={(e) => setSelectedDate(e.target.value)}
        />
      </div>

      <button className="generate-btn" onClick={generateExcelReport}>
        Generate Report
      </button>

      <div className="chart-container">
        <div className="chart-box">
          <h3>Validation Status</h3>
          <Pie data={validationData} />
        </div>
        <div className="chart-box">
          <h3>Accountants Per Region</h3>
          <Bar data={accountantsPerRegion} />
        </div>
      </div>
    </div>
  );
};

export default GenerateReportPage;
