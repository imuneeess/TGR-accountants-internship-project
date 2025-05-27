import React, { useState } from "react";
import axios from "axios";
import './AddUserPage.css';

const AddUserPage = () => {
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("ACCOUNTANT");
  const [generatedPassword, setGeneratedPassword] = useState("");
  const [statusMessage, setStatusMessage] = useState("");

  const handleEmailChange = (e) => setEmail(e.target.value);
  const handleRoleChange = (e) => setRole(e.target.value);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("userToken"); // ✅ Get JWT token from localStorage

      const response = await axios.post(
        "http://localhost:8080/admin/create-user",
        { email, role },
        {
          headers: {
            Authorization: `Bearer ${token}`, // ✅ Include token in request headers
          },
        }
      );

      const { email: createdEmail, password } = response.data;
      setGeneratedPassword(password);
      setStatusMessage(`✅ User created! Email: ${createdEmail}`);
    } catch (error) {
      setStatusMessage("❌ Failed to create user. Please check the role or token.");
    }
  };

  return (
    <div className="add-user-container">
      <h1>➕ Add New User</h1>
      <p>Register new users and assign roles. The password is auto-generated.</p>

      {statusMessage && <p>{statusMessage}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={handleEmailChange}
            required
            placeholder="Enter email"
          />
        </div>

        <div>
          <label>Role:</label>
          <select value={role} onChange={handleRoleChange}>
            <option value="ACCOUNTANT">Accountant</option>
            <option value="ADMIN">Admin</option>
          </select>
        </div>

        <button type="submit">Create User</button>
      </form>

      {generatedPassword && (
        <div className="password-container">
          <h3>Generated Password:</h3>
          <p>{generatedPassword}</p>
        </div>
      )}

      <p>After the account is created, the user can manually set their notification email.</p>
    </div>
  );
};

export default AddUserPage;
