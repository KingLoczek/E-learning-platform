<template>
  <nav>
    <div class="logo">
      <img src="/src/assets/logo.png" alt="Sigma Portal Logo">
    </div>
    <router-link to="/" class="link">Home</router-link>
    <router-link v-if="isLoggedIn" to="/courses" class="link">Courses</router-link>
    <router-link v-if="isLoggedIn && isInstructor" to="/teacher-panel" class="link">Teacher Panel</router-link>
    <router-link v-if="!isLoggedIn" to="/login" class="highlight">Login</router-link>
    <router-link v-if="!isLoggedIn" to="/register" class="highlight">Register</router-link>
    <router-link v-if="isLoggedIn" to="/profile" class="link">Profile</router-link>
    <button v-if="isLoggedIn" @click="logout" class="highlight">Logout</button>
  </nav>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Navbar',
  data() {
    return {
      isLoggedIn: !!localStorage.getItem('authToken'), // Sprawdzenie, czy użytkownik jest zalogowany
      isInstructor: false, // Zmienna do sprawdzenia, czy użytkownik jest instruktorem
    };
  },
  methods: {
    logout() {
      localStorage.removeItem('authToken');
      localStorage.removeItem('userData');
      this.isLoggedIn = false;
      this.isInstructor = false; // Resetujemy stan instruktora po wylogowaniu
      this.$router.push('/login'); // Przekierowanie do strony logowania po wylogowaniu
    },
    // Funkcja do pobrania danych o użytkowniku
    async fetchUserData() {
      try {
        const response = await axios.get('/api/user/me', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('authToken')}`,
          },
        });
        // Sprawdzamy, czy użytkownik jest instruktorem
        if (response.data.user_type === 'instructor') {
          this.isInstructor = true;
        }
      } catch (error) {
        console.error('Błąd podczas pobierania danych użytkownika:', error);
      }
    },
  },
  mounted() {
    if (this.isLoggedIn) {
      this.fetchUserData();
    }
  },
  watch: {
    '$route'(to, from) {
      this.isLoggedIn = !!localStorage.getItem('authToken'); // Aktualizacja stanu logowania po zmianie trasy
      if (this.isLoggedIn) {
        this.fetchUserData(); // Pobieramy dane użytkownika po każdej zmianie trasy
      }
    }
  }
};
</script>
  
  <style scoped>
  nav {
    display: flex;
    align-items: center;
    padding: 10px 20px;
    background-color: #171717;
  }
  
  nav a {
    margin: 0 10px;
    text-decoration: none;
    color: #2c3e50;
  }
  
  nav a:hover {
    color: #007bff;
  }
  
  .logo {
    color: #ffd700;
    width: 10em;
    font-weight: bold;
  }
  
  img {
    height: auto;
    width: 100%;
  }
  
  .link {
    margin: 0 10px;
    color: white;
    font-size: 1.2em;
    transition: color 0.3s;
  }
  
  .link:hover {
    color: #ffd700;
  }
  
  .highlight {
    font-weight: bold;
    color: #C3A834;
    font-size: 1.2em;
    background-color: #171717;
    border: none;
    cursor: pointer;
  }
  </style>
  