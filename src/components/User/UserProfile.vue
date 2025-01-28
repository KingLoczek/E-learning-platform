<template>
  <div class="user-profile-container">
    <h2>User Profile</h2>
    <div v-if="user" class="form-container">
      <form @submit.prevent="updateUserDetails" class="form">
        <div class="form-group">
          <label for="email">Email:</label>
          <input v-model="user.email" type="email" id="email" required />
        </div>
        <div class="form-group">
          <label for="first_name">First Name:</label>
          <input v-model="user.first_name" type="text" id="first_name" required />
        </div>
        <div class="form-group">
          <label for="last_name">Last Name:</label>
          <input v-model="user.last_name" type="text" id="last_name" required />
        </div>
        <button type="submit" class="submit-btn">Save Changes</button>
      </form>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
    <p v-else>Loading user data...</p>
  </div>
</template>

  <script>
  import axios from 'axios';
  
  export default {
    name: 'UserProfile',
    data() {
      return {
        user: null,
        successMessage: '',
        errorMessage: '',
      };
    },
    methods: {
      async fetchUserProfile() {
        try {
          const response = await axios.get('/api/user/me');
          this.user = response.data;
          this.errorMessage = '';
        } catch (error) {
          this.user = null;
          this.errorMessage = 'Error fetching user data.';
          console.error(error);
        }
      },
      async updateUserDetails() {
        try {
          const response = await axios.patch(`/api/user/${this.user.id}`, {
            email: this.user.email,
            first_name: this.user.first_name,
            last_name: this.user.last_name,
          });
          this.successMessage = 'Profile updated successfully!';
          this.errorMessage = '';
          console.log(response.data);
        } catch (error) {
          this.successMessage = '';
          this.errorMessage = 'Error updating profile.';
          console.error(error);
        }
      },
    },
    mounted() {
      this.fetchUserProfile();
    },
  };
  </script>

<style scoped>
.user-profile-container {
  max-width: 500px;
  margin: 3em auto;
  padding: 2em;
  background: #1e1e1e;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  color: #ffffff;
}

h2 {
  text-align: center;
  color: #c3a834;
  margin-bottom: 1em;
}

.form-container {
  margin-top: 1em;
}

.form-group {
  margin-bottom: 1.5em;
}

label {
  display: block;
  margin-bottom: 0.5em;
  font-weight: bold;
  color: #ffffff;
}

input {
  width: 80%;
  padding: 0.8em;
  border: 1px solid #444;
  border-radius: 4px;
  background: #2b2b2b;
  color: #ffffff;
  font-size: 1em;
}

input::placeholder {
  color: #757575;
}

.submit-btn {
  width: 50%;
  padding: 0.8em;
  background: #c3a834;
  border: none;
  border-radius: 4px;
  color: #000;
  font-size: 1.2em;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background: #e6c200;
}

.success-message {
  color: #4caf50;
  margin-top: 1em;
  text-align: center;
}

.error-message {
  color: #f44336;
  margin-top: 1em;
  text-align: center;
}

@media (max-width: 480px) {
  .user-profile-container {
    padding: 1.5em;
  }

  .submit-btn {
    font-size: 1em;
  }
}
</style>