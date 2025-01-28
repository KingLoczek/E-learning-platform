<template>
    <section class="hero">
      <div class="hero-text">
        <h1>Sigma Portal</h1>
        <p>
          Platforma edukacyjna oferująca starannie opracowane kursy online, które pomagają rozwijać umiejętności w różnych dziedzinach. Nasze programy tworzone są przez ekspertów, co zapewnia najwyższą jakość nauczania.
        </p>
        <p>
          Dzięki interaktywnym modułom, praktycznym zadaniom i wsparciu instruktorów, możesz elastycznie dostosować naukę do swojego harmonogramu. Niezależnie od tego, czy jesteś w domu, w pracy czy w drodze, masz stały dostęp do materiałów.  
        </p>
        <p>SigmaPortal ma kursy dla każdego — od rozwoju zawodowego po nowe hobby, pomagając pogłębić Twoją wiedzę.</p>
        <button @click="redirectToCourses">Ucz się</button>
      </div>
      <div class="hero-image">
        <img src="/src/assets/learning.jpg" alt="Sigma Portal">
      </div>
    </section>
  </template>
  
  <script>
  export default {
    name: "HeroSection",
    methods: {
      redirectToCourses() {
        const isLoggedIn = !!localStorage.getItem('authToken');  // Sprawdzenie, czy użytkownik jest zalogowany
        if (isLoggedIn) {
          this.$router.push('/courses');  // Przekierowanie na stronę kursów
        } else {
          this.$router.push('/login');  // Przekierowanie na stronę logowania
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .hero {
    display: flex;
    flex-wrap: wrap;
    padding: 2em;
    background-color: #222;
  }
  
  .hero-text {
    flex: 1;
    padding: 2em;
  }
  
  .hero-text h1 {
    color: #C3A834;
    font-size: 2em;
  }
  
  .hero-text p {
    margin: 1em 0;
    line-height: 1.5;
    font-size: 1.2em;
    text-align: justify;
  }
  
  .hero-text button {
    background-color:  #C3A834;
    color: black;
    border: none;
    padding: 1em 1.5em;
    font-size: 1em;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .hero-text button:hover {
    background-color: #e6c200;
  }
  
  .hero-image img {
    max-width: 100%;
    height: auto;
    margin: 2em;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
  </style>
  