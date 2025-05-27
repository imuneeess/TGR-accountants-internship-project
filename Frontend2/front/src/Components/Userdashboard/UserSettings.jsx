import React, { useEffect, useState } from "react";
import axios from "axios";
import "./UserSettings.css";

const UserSettings = () => {
  const token = localStorage.getItem("userToken");
  const email = localStorage.getItem("userEmail");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/users/me?email=${email}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        setFirstName(res.data.firstName || "");
        setLastName(res.data.lastName || "");
      })
      .catch(() => setMessage("Error loading user data"));
  }, [email, token]);

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    try {
      await axios.put(
        "http://localhost:8080/api/users/update-profile",
        { email, firstName, lastName },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setMessage("Profile updated successfully");
    } catch {
      setMessage("Error updating profile");
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      return setMessage("New passwords do not match");
    }
    try {
      await axios.post(
        "http://localhost:8080/api/users/change-password",
        { email, currentPassword, newPassword },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setMessage("Password changed successfully");
    } catch {
      setMessage("Error changing password");
    }
  };

  return (
    <div className="settings-container">
      <h2>Profile Settings</h2>
      <form onSubmit={handleUpdateProfile} className="form-box">
        <input
          type="text"
          placeholder="First Name"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Last Name"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        <button type="submit">Update Profile</button>
      </form>

      <h3>Change Password</h3>
      <form onSubmit={handlePasswordChange} className="form-box">
        <input
          type="password"
          placeholder="Current Password"
          value={currentPassword}
          onChange={(e) => setCurrentPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="New Password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="Confirm New Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <button type="submit">Change Password</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
};

export default UserSettings;
