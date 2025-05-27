import { useState } from 'react';
import axios from 'axios';
import { FaUser, FaLock, FaEye, FaEyeSlash } from 'react-icons/fa';
import './SettingsPage.css';

const AdminSettings = () => {
  const [nameForm, setNameForm] = useState({ firstName: '', lastName: '' });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: ''
  });
  const [showAllPasswords, setShowAllPasswords] = useState(false);
  const [nameMsg, setNameMsg] = useState('');
  const [passwordMsg, setPasswordMsg] = useState('');

  const token = localStorage.getItem("userToken");
  const email = localStorage.getItem("userEmail");

  const validateStrongPassword = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?#&^_-])[A-Za-z\d@$!%*?#&^_-]{8,}$/;
    return regex.test(password);
  };  

  const handleNameUpdate = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.put("http://localhost:8080/api/users/update-name", {
        ...nameForm,
        email,
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setNameMsg(res.data);
    } catch (err) {
      setNameMsg(err.response?.data || "❌ Failed to update name.");
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    if (passwordForm.newPassword !== passwordForm.confirmNewPassword) {
      setPasswordMsg("❌ New passwords do not match.");
      return;
    }

    if (!validateStrongPassword(passwordForm.newPassword)) {
      setPasswordMsg("❌ Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.");
      return;
    }

    try {
      const res = await axios.put("http://localhost:8080/api/users/change-password", {
        ...passwordForm,
        email,
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setPasswordMsg(res.data);
    } catch (err) {
      setPasswordMsg(err.response?.data || "❌ Failed to change password.");
    }
  };

  return (
    <div className="settings-container">
      <div className="section-title"><FaUser /> Update Name</div>
      <form className="settings-form" onSubmit={handleNameUpdate}>
        <input
          placeholder="First Name"
          value={nameForm.firstName}
          onChange={(e) => setNameForm({ ...nameForm, firstName: e.target.value })}
          required
        />
        <input
          placeholder="Last Name"
          value={nameForm.lastName}
          onChange={(e) => setNameForm({ ...nameForm, lastName: e.target.value })}
          required
        />
        <button type="submit">Update Name</button>
        <p className="response-message">{nameMsg}</p>
      </form>

      <hr />

      <div className="section-title"><FaLock /> Change Password</div>
      <form className="settings-form" onSubmit={handlePasswordChange}>
        <div className="password-wrapper">
          <input
            type={showAllPasswords ? 'text' : 'password'}
            placeholder="Current Password"
            value={passwordForm.currentPassword}
            onChange={(e) => setPasswordForm({ ...passwordForm, currentPassword: e.target.value })}
            required
          />
          <span onClick={() => setShowAllPasswords(prev => !prev)}>
            {showAllPasswords ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>

        <div className="password-wrapper">
          <input
            type={showAllPasswords ? 'text' : 'password'}
            placeholder="New Password"
            value={passwordForm.newPassword}
            onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })}
            required
          />
          <span onClick={() => setShowAllPasswords(prev => !prev)}>
            {showAllPasswords ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>
        <p className="password-tip">🔐 At least 8 characters, 1 uppercase, 1 lowercase, 1 number, 1 special character</p>

        <div className="password-wrapper">
          <input
            type={showAllPasswords ? 'text' : 'password'}
            placeholder="Confirm New Password"
            value={passwordForm.confirmNewPassword}
            onChange={(e) => setPasswordForm({ ...passwordForm, confirmNewPassword: e.target.value })}
            required
          />
          <span onClick={() => setShowAllPasswords(prev => !prev)}>
            {showAllPasswords ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>

        <button type="submit">Change Password</button>
        <p className="response-message">{passwordMsg}</p>
      </form>
    </div>
  );
};

export default AdminSettings;
