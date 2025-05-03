import React, { useState } from "react";
import axios from "axios";

const AddEmail = () => {
  const [emailInput, setEmailInput] = useState("");
  const userEmail = localStorage.getItem("userEmail");
  const token = localStorage.getItem("userToken");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await axios.post(
        "http://localhost:8080/api/users/save-notification-email",
        {
          email: userEmail,
          notificationEmail: emailInput,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("✅ Notification email saved successfully!");
    } catch (err) {
      console.error("❌ Error saving notification email:", err);
      alert("Failed to save. Check console.");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "500px" }}>
      <h2>📧 Add Notification Email</h2>
      <p>
        Enter your personal email to receive reminders if you forget to validate your{" "}
        <b>journée comptable</b>.
      </p>

      <form onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Enter your notification email"
          value={emailInput}
          onChange={(e) => setEmailInput(e.target.value)}
          required
          style={{
            padding: "12px",
            borderRadius: "6px",
            border: "1px solid #ccc",
            width: "100%",
            marginTop: "10px",
            marginBottom: "20px",
            fontSize: "16px",
          }}
        />
        <button
          type="submit"
          style={{
            background: "#194f8a",
            color: "white",
            padding: "10px 20px",
            border: "none",
            borderRadius: "6px",
            cursor: "pointer",
            fontWeight: "bold",
          }}
        >
          Save Email
        </button>
      </form>
    </div>
  );
};

export default AddEmail;
