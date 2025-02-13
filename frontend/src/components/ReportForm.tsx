import { useState } from "react";
import axios from "../services/api"; // Ensure this points to your API setup

const ReportForm = ({ onReportSubmitted }) => {
  const [branchName, setBranchName] = useState("");
  const [submissionDate, setSubmissionDate] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("/reports/submit", {
        branchName,
        submissionDate
      });
      console.log("Report Submitted:", response.data);
      onReportSubmitted(); // Refresh reports after submission
    } catch (error) {
      console.error("Error submitting report", error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Branch Name"
        value={branchName}
        onChange={(e) => setBranchName(e.target.value)}
        required
      />
      <input
        type="date"
        value={submissionDate}
        onChange={(e) => setSubmissionDate(e.target.value)}
        required
      />
      <button type="submit">Submit Report</button>
    </form>
  );
};

export default ReportForm;
