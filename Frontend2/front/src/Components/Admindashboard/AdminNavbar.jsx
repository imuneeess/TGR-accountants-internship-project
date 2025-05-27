import React, { useEffect, useState } from 'react';
import './ProfileBubble.css';
import axios from 'axios';

const AdminProfileBubble = () => {
  const [profile, setProfile] = useState({ firstName: '', lastName: '', email: '' });
  const [dropdownOpen, setDropdownOpen] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem('userToken');
      const res = await axios.get('http://localhost:8080/admin/profile', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setProfile(res.data);
    };
    fetchProfile();
  }, []);

  const initials = `${profile.firstName?.[0] || ''}${profile.lastName?.[0] || ''}`.toUpperCase();

  return (
    <div className="profile-bubble-container">
      <div className="avatar" onClick={() => setDropdownOpen(!dropdownOpen)}>
        {initials || 'AD'}
      </div>
      {dropdownOpen && (
        <div className="dropdown">
          <p>{profile.firstName} {profile.lastName}</p>
          <p>{profile.email}</p>
          <a href="/admin/settings">⚙️ Settings</a>
          <button onClick={() => {
            localStorage.clear();
            window.location.href = '/';
          }}>🚪 Logout</button>
        </div>
      )}
    </div>
  );
};

export default AdminProfileBubble;
