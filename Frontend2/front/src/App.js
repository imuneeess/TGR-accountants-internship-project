import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./Components/LoginForm/LoginForm";
import ForgotPasswordForm from "./Components/LoginForm/ForgotPasswordForm"; // ✅ Import
import DashboardLayout from "./Components/Admindashboard/DashboardLayout";
import AdminDashboard from "./Components/Admindashboard/AdminDashboard";
import AccountantsList from "./Components/Admindashboard/AccountantsList";
import GenerateReportPage from "./Components/Admindashboard/GenerateReportPage";
import AddUserPage from "./Components/Admindashboard/AddUserPage";
import UserDashboardLayout from "./Components/Userdashboard/UserDashboardLayout";
import UserHome from "./Components/Userdashboard/UserHome";
import AddEmail from "./Components/Userdashboard/AddEmail";
import SettingsPage from "./Components/Userdashboard/SettingsPage";
import AdminSettings from "./Components/Admindashboard/AdminSettings";


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route path="/forgot-password" element={<ForgotPasswordForm />} /> {/* ✅ New route */}

        {/* Admin Panel */}
        <Route path="/*" element={<DashboardLayout />}>
          <Route path="dashboard" element={<AdminDashboard />} />
          <Route path="accountants" element={<AccountantsList />} />
          <Route path="generate-report" element={<GenerateReportPage />} />
          <Route path="add-user" element={<AddUserPage />} />
          <Route path="settings" element={<AdminSettings />} /> {/* ✅ Add this */}


        </Route>

        {/* User Panel */}
        <Route path="/user/*" element={<UserDashboardLayout />}>
          <Route path="home" element={<UserHome />} />
          <Route path="add-email" element={<AddEmail />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;