<template>
  <div class="login-container">
    <h1>Login</h1>
    <form @submit.prevent="loginUser" class="form">
      <div class="form-group">
        <label>Email:</label>
        <input v-model="email" type="email" placeholder="example@gmail.com" required />
      </div>
      <div class="form-group">
        <label>Password:</label>
        <input v-model="password" type="password" placeholder="********" required />
      </div>
      <button type="submit" class="submit-btn">Login</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async loginUser() {
      try {
        const response = await axios.post('/api/user/login', {
          username: this.email,
          password: this.password,
        });

        if (response.status === 200) {
          console.log('Login successful');
          localStorage.setItem("authToken", response.data.token);

          // Pobierz dane użytkownika
          const userResponse = await axios.get('/api/user/me', {
            headers: {
              Authorization: `Bearer ${response.data.token}`,
            },
          });

          const user = userResponse.data;
          localStorage.setItem("userData", JSON.stringify(user));
          this.$root.isLoggedIn = true;
          // Przekierowanie w zależności od typu użytkownika
          if (user.user_type === "instructor") {
            this.$router.push('/teacher-panel');
          } else if (user.user_type === "student") {
            this.$router.push('/courses');
          } else if (user.user_type === "admin"){
            this.$router.push('/admin');
          } else {
            this.$router.push('/');
          }
        } else {
          console.error('Login failed');
        }
      } catch (error) {
        console.error('Login error:', error);
      }
    },
  },
};
</script>
<style scoped>
.login-container {
  max-width: 400px;
  margin: 3em auto;
  padding: 2em;
  background: #1e1e1e;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

h1 {
  color: #c3a834;
  text-align: center;
  margin-bottom: 1em;
}

.form-group {
  margin-bottom: 1.5em;
}

label {
  display: block;
  margin-bottom: 0.5em;
  color: #ffffff;
  font-weight: bold;
}

input {
  width: 90%;
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
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background: #e6c200;
}

@media (max-width: 480px) {
  .login-container {
    padding: 1.5em;
  }
}
</style>