<template>
  <div class="register-container">
    <h2>Register</h2>
    <form @submit.prevent="registerUser" class="form">
      <div class="form-group">
        <label for="email">Email:</label>
        <input v-model="form.email" type="email" id="email" required placeholder="example@gmail.com" />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input v-model="form.password" type="password" id="password" required placeholder="********" />
      </div>
      <div class="form-group">
        <label for="first_name">First Name:</label>
        <input v-model="form.first_name" type="text" id="first_name" required placeholder="John" />
      </div>
      <div class="form-group">
        <label for="last_name">Last Name:</label>
        <input v-model="form.last_name" type="text" id="last_name" required placeholder="Doe" />
      </div>
      <button type="submit" class="submit-btn">Register</button>
    </form>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>
<script>
  import axios from 'axios';
  
  export default {
    name: 'RegisterForm',
    data() {
      return {
        form: {
          email: '',
          password: '',
          first_name: '',
          last_name: '',
          user_type: 'student',
        },
        successMessage: '',
        errorMessage: '',
      };
    },
    methods: {
      async registerUser() {
        try {
          const response = await axios.post('/api/user/', this.form);
          this.successMessage = 'User registered successfully!';
          this.errorMessage = '';
        //   console.log(response.data);
        } catch (error) {
          this.successMessage = '';
          this.errorMessage = 'Error registering user.';
          console.error(error);
        }
      },
    },
  };
</script>
  
<style scoped>
  .register-container {
    max-width: 500px;
    margin: 3em auto;
    padding: 2em;
    background: #1e1e1e;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  }
  
  h2 {
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
    transition: background-color 0.3s;
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
    .register-container {
      padding: 1.5em;
    }
  }
</style>