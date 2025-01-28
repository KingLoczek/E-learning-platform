<template>
  <footer>
  <div class="footer-container">
  <div class="logo">
      <img src="/src/assets/logo.png" alt="Sigma Portal Logo">      
  </div>

  <nav class="nav">
    <RouterLink to="/" class="link">Home</RouterLink>
    <RouterLink to="/about-us" class="link">About us</RouterLink>
    <RouterLink to="/faq" class="link">FAQ</RouterLink>
  </nav>

  <div class="contact">
      <p><i class="fas fa-phone"></i> +48 251 221 235</p>
      <p><i class="fas fa-envelope"></i> sigmaportal@gmail.com</p>
      <p><i class="fas fa-map-marker-alt"></i> Wrocław, ul Zygmunta</p>
  </div>
  </div>
  </footer>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style scoped>
  footer {
  background-color: #171717; 
  color: #ffffff; 
  padding: 20px 40px;
  }

  .footer-container {
  display: flex;
  justify-content: space-between; 
  align-items: center;
  flex-wrap: wrap;
  gap: 20px; 
  }

  .nav {
  display: flex;
  gap: 40px; 
  }

  .nav a {
  color: #ffffff;
  text-decoration: none; 
  font-size: 1.5em;
  transition: color 0.3s ease;
  }

  .nav a:hover {
  color: #ffc107; 
  }

  /* Kontakt */
  .footer-contact p {
  display: flex;
  align-items: center;
  margin: 5px 0;
  font-size: 14px;
  color: #c1c1c1; /* Szary tekst */
  }

  .contact i {
  margin-right: 10px; /* Odstęp między ikoną a tekstem */
  font-size: 16px;
  color: #ffc107; /* Złote ikonki */
  }

  /* Responsywność */
  @media (max-width: 768px) {
  .container {
      flex-direction: column; /* Ustawia elementy w kolumnie */
      align-items: flex-start;
  }

  .nav {
      margin: 10px 0;
      gap: 20px; /* Mniejsze odstępy w wersji mobilnej */
  }
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

</style>