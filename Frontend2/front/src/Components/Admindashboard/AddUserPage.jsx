import React, { useState } from "react";
import axios from "axios";
import './AddUserPage.css';  // Import the CSS file

const AddUserPage = () => {
  const [email, setEmail] = useState("");
  const [generatedPassword, setGeneratedPassword] = useState("");
  const [statusMessage, setStatusMessage] = useState("");

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Call the backend API to create the accountant
      const response = await axios.post("http://localhost:8080/admin/create-accountant", { email });

      // Extract password and email from the response
      const { email: createdEmail, password } = response.data;

      // Set the generated password in state to display
      setGeneratedPassword(password);
      setStatusMessage(`Accountant created successfully! Email: ${createdEmail}`);

    } catch (error) {
      setStatusMessage("Failed to create accountant. Please try again.");
    }
  };

  return (
    <div className="add-user-container">  {/* Add this class to the outer div */}
      <h1>➕ Add New User</h1>
      <p>Register new users and assign roles. The password is auto-generated.</p>

      {/* Display success or error message */}
      {statusMessage && <p>{statusMessage}</p>}

      {/* Form to add a new user */}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={handleEmailChange}
            required
            placeholder="Enter accountant's email"
          />
        </div>
        <button type="submit">Create Accountant</button>
      </form>

      {/* Display the generated password if the account is created */}
      {generatedPassword && (
        <div className="password-container">
          <h3>Generated Password:</h3>
          <p>{generatedPassword}</p>
        </div>
      )}

      <p>
        After the account is created, the accountant can manually set their
        notification email.
      </p>
    </div>
  );
};

export default AddUserPage;
