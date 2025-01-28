<template>
  <div id="app">
    <Navbar />
    <router-view></router-view>
    <Footer/>
  </div>
</template>

<script>
import Navbar from './components/Shared/Navbar.vue';
import Footer from './components/Shared/Footer.vue';

export default {
  name: 'App',
  components: {
    Navbar, 
    Footer,
  },
};
</script>

<style>
body{margin:0}
#app {
  background-color: #222;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  display: grid;
  grid-template-rows: auto 1fr auto;
  min-height: 100vh; 
}
</style>
