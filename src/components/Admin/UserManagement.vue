<template>
  <div class="user-management">
    <h2 class="title">User Management</h2>
    <div class="search-container">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Search by name, email, or user type"
        class="search-input"
      />
    </div>
    <div v-if="filteredUsers.length > 0">
      <ul class="user-list">
        <li v-for="user in filteredUsers" :key="user.id" class="user-item">
          <div class="user-info">
            <span>{{ user.first_name }} {{ user.last_name }} ({{ user.email }}) - <span :class="user.user_type">{{ user.user_type }}</span></span>
          </div>
          <!-- Opcje dla admina: zmiana user_type i usunięcie użytkownika -->
          <div class="actions">
            <button @click="changeUserType(user.id)" :disabled="isChanging" class="action-button change-button">Change User Type</button>
            <button @click="deleteUser(user.id)" :disabled="isDeleting" class="action-button delete-button">Delete User</button>
          </div>
        </li>
      </ul>
    </div>
    <div v-else>
      <p class="no-users">No users found.</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "UserManagement",
  data() {
    return {
      users: [],  // Przechowuje listę użytkowników
      isChanging: false,  // Flaga informująca, czy trwa zmiana user_type
      isDeleting: false,  // Flaga informująca, czy trwa usuwanie użytkownika
      searchQuery: "",  // Wyszukiwane zapytanie
    };
  },
  computed: {
    // Filtruje użytkowników na podstawie wyszukiwanego zapytania
    filteredUsers() {
      return this.users.filter(user => {
        const query = this.searchQuery.toLowerCase();
        return (
          user.first_name.toLowerCase().includes(query) ||
          user.last_name.toLowerCase().includes(query) ||
          user.email.toLowerCase().includes(query) ||
          user.user_type.toLowerCase().includes(query)
        );
      });
    }
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get("/api/user/");
        this.users = response.data;
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },
    
    async changeUserType(userId) {
      const newType = prompt("Enter new user type (student/instructor):");
      if (newType && (newType === "student" || newType === "instructor")) {
        try {
          this.isChanging = true;
          await axios.patch(`/api/user/${userId}`, { user_type: newType });
          this.fetchUsers(); // Odświeżamy listę użytkowników
        } catch (error) {
          console.error("Error changing user type:", error);
        } finally {
          this.isChanging = false;
        }
      } else {
        alert("Invalid user type.");
      }
    },

    async deleteUser(userId) {
      const confirmDelete = confirm("Are you sure you want to delete this user?");
      if (confirmDelete) {
        try {
          this.isDeleting = true;
          await axios.delete(`/api/user/${userId}`);
          this.fetchUsers(); // Odświeżamy listę użytkowników po usunięciu
        } catch (error) {
          console.error("Error deleting user:", error);
        } finally {
          this.isDeleting = false;
        }
      }
    },
  },
  mounted() {
    this.fetchUsers(); // Ładujemy użytkowników po załadowaniu komponentu
  },
};
</script>

<style scoped>
.user-management {
  padding: 2rem;
  background-color: #1e1e1e;
  border-radius: 10px;
  width: 80%;
  margin: 2rem auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 2rem;
  color: #ffc107;
  text-align: center;
  margin-bottom: 1.5rem;
}

.search-container {
  margin-bottom: 1rem;
  text-align: center;
}

.search-input {
  padding: 0.5rem;
  width: 50%;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
}

.user-list {
  list-style-type: none;
  padding: 0;
}

.user-item {
  background-color: #333;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  font-size: 1.1rem;
  color: #ffff;
}

.user-info span {
  font-weight: 600;
}

.user-info span.student {
  color: #28a745;  /* Green for student */
}

.user-info span.instructor {
  color: #007bff;  /* Blue for instructor */
}

.actions {
  display: flex;
  gap: 1rem;
}

.action-button {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.change-button {
  background-color: #ffc107;
  color: white;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.action-button:hover {
  transform: translateY(-2px);
}

.change-button:hover {
  background-color: #e0a800;
}

.delete-button:hover {
  background-color: #c82333;
}

.no-users {
  text-align: center;
  font-size: 1.2rem;
  color: #888;
}

.search-container {
  margin-bottom: 1.5rem;
  display: flex;
  justify-content: center;
}

.search-input {
  padding: 0.8rem 1.2rem;
  width: 60%;
  border: 1px solid #ccc;
  border-radius: 30px;
  font-size: 1.1rem;
  background-color: #2c2c2c;
  color: #fff;
  transition: all 0.3s ease;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.search-input::placeholder {
  color: #bbb;
}

.search-input:focus {
  outline: none;
  border-color: #ffc107;
  background-color: #333;
}

.search-input:hover {
  border-color: #ff8a00;
}

.search-input:focus::placeholder {
  color: #fff;
}

</style>
