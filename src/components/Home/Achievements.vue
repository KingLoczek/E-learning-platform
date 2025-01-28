<template>
    <section class="achievements">
      <h2>Nasze osiągnięcia</h2>
      <h3>Zobacz jak nasza platforma ciągle rośnie w siłe</h3>
      <div class="achievement-list">
        <div class="achievement">
          <i class="fa-solid fa-users fa-2xl" style="color: #000000;"></i>
          <p class="number">33000+</p>
          <p>Użytkowników</p>
        </div>
        <div class="achievement">
          <i class="fa-solid fa-book fa-2xl" style="color: #000000;"></i>
          <p class="number">2000+</p>
          <p>Kursów</p>
        </div>
        <div class="achievement">
          <i class="fa-solid fa-database fa-2xl" style="color: #000000;"></i>
          <p class="number">2TB</p>
          <p>Danych</p>
        </div>
      </div>
    </section>
  </template>
  
  <script>
  export default {
    name: "Achievements",
  };
  </script>
  
  <style scoped>
  .achievements {
    text-align: center;
    padding: 3em 3em 5em;
    background-color: #333;
  }
  
  .achievements h2 {
    color: #C3A834;
    font-size: 2em;
    margin-bottom: .5em;
  }
  
  .achievement-list {
    display: flex;
    justify-content: space-evenly;
    gap: 20px;
    flex-wrap: wrap;
  }
  
  .achievement {
    text-align: center;
    background-color: #D9D9D9;
    padding: 2em;
    width: 10em;
    height: auto;
    border-radius: 0.5em;
  }
  
  .number {
    font-size: 2.5em;
    font-weight: bold;
    color: #C3A834;
  }
  p {
    font-size: 1.5em;
    color: black;
  }
  </style>