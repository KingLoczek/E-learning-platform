<template>
  <div id="main">
  <HeroSection />
  <Achievements />
  </div>
</template>

<script>
import Achievements from '/src/components/Home/Achievements.vue';
import HeroSection from '/src/components/Home/HeroSection.vue';

export default {
  components: {
    HeroSection,
    Achievements
  },
};
</script>
<style>
#main{
  color: #ffffff;
}
</style>