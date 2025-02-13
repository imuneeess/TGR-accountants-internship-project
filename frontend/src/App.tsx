import React, { useEffect, useState } from "react";
import { getAccountingReports, submitReport, AccountingReport } from "./services/api";
import "./App.css";

const App: React.FC = () => {
  const [reports, setReports] = useState<AccountingReport[]>([]);
  const [branchName, setBranchName] = useState("");
  const [submittedBy, setSubmittedBy] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    setLoading(true);
    try {
      const data = await getAccountingReports();
      setReports(data);
    } catch (error) {
      setError("Failed to load reports");
    }
    setLoading(false);
  };

  const handleSubmitReport = async () => {
    if (!branchName || !submittedBy) {
      setError("Branch Name and Submitted By are required");
      return;
    }
    setLoading(true);
    try {
      const newReport: AccountingReport = {
        branchName,
        submissionDate: new Date().toISOString(),
        isSubmitted: true,
        submittedBy,
      };
      await submitReport(newReport);
      setSuccessMessage("Report submitted successfully!");
      fetchReports(); // Refresh the reports list
    } catch (error) {
      setError("Error submitting report");
    }
    setLoading(false);
  };

  return (
    <div className="container">
      <h1>Accounting Reports</h1>
      <div className="form-container">
        <input
          type="text"
          placeholder="Branch Name"
          value={branchName}
          onChange={(e) => setBranchName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Submitted By (Email)"
          value={submittedBy}
          onChange={(e) => setSubmittedBy(e.target.value)}
        />
        <button onClick={handleSubmitReport} disabled={loading}>
          {loading ? "Submitting..." : "Submit a Report"}
        </button>
      </div>
      {error && <p className="error-message">{error}</p>}
      {successMessage && <p className="success-message">{successMessage}</p>}

      {loading ? (
        <p>Loading reports...</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Branch Name</th>
              <th>Submitted By</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((report, index) => (
              <tr key={index}>
                <td>{report.branchName}</td>
                <td>{report.submittedBy}</td>
                <td>{report.isSubmitted ? "✅ Submitted" : "❌ Pending"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default App;
