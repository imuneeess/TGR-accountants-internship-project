import axios from "axios";

const API_BASE_URL = "http://localhost:8080"; // Adjust this to match your backend URL

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Define types for reports
export interface AccountingReport {
  branchName: string;
  submissionDate: string;
  isSubmitted: boolean;
  submittedBy: string;
}

// Fetch all accounting reports
export const getAccountingReports = async (): Promise<AccountingReport[]> => {
  try {
    const response = await api.get("/reports");
    return response.data;
  } catch (error) {
    console.error("Error fetching reports:", error);
    throw error;
  }
};

// Submit a new report
export const submitReport = async (reportData: AccountingReport): Promise<void> => {
  try {
    await api.post("/reports/submit", reportData);
  } catch (error) {
    console.error("Error submitting report:", error);
    throw error;
  }
};

export default api;
