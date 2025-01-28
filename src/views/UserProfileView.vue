<template>
    <div class="user-profile-view">
      <UserProfile />
    </div>
  </template>
  
  <script>
  import UserProfile from '/src/components/User/UserProfile.vue';
  
  export default {
    name: 'UserProfileView',
    components: {
      UserProfile,
    },
  };
  </script>
  
  <style scoped>
  .user-profile-view {
    padding: 2rem;
  }
  </style>
  