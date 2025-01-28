<template>
    <div class="register-view">
      <RegisterForm />
    </div>
  </template>
  
  <script>
  import RegisterForm from '/src/components/User/RegisterForm.vue';
  
  export default {
    name: 'RegisterView',
    components: {
      RegisterForm,
    },
  };
  </script>
  
  <style scoped>
  .register-view {
    padding: 2rem;
  }
  </style>
  