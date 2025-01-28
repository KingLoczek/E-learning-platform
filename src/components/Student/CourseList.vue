<template>
  <div>
    <h1>Available Courses</h1>
    <div class="search-container">
      <input 
        type="text" 
        v-model="searchQuery" 
        @input="filterCourses" 
        placeholder="Search for a course..." 
        class="search-input" 
      />
    </div>
    <div class="course-cards-container">
      <CourseCard 
        v-for="course in filteredCourses" 
        :key="course.id" 
        :course="course" 
        @view-details="goToCourseDetail" 
      />
    </div>
  </div>
</template>

<script>
import CourseCard from '/src/components/Shared/CourseCard.vue';
import axios from 'axios';

export default {
  name: 'CourseList',
  components: { CourseCard },
  data() {
    return {
      courses: [],
      searchQuery: '', // Zmienna dla wyszukiwania
      filteredCourses: [], // Przechowuje przefiltrowane kursy
    };
  },
  methods: {
    fetchCourses() {
      axios.get('/api/course/')
        .then(response => {
          this.courses = response.data;
          this.filteredCourses = this.courses; // Na początku wszystkie kursy są wyświetlane
        })
        .catch(error => {
          console.error('Error fetching courses:', error);
        });
    },
    goToCourseDetail(courseId) {
      this.$router.push(`/course/${courseId}`);
    },
    filterCourses() {
      if (this.searchQuery.trim() === '') {
        this.filteredCourses = this.courses; // Jeśli nie ma zapytania, wyświetlamy wszystkie kursy
      } else {
        this.filteredCourses = this.courses.filter(course =>
          course.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }
    },
  },
  mounted() {
    this.fetchCourses();
  },
};
</script>

<style>
h1 {
  color: #C3A834;
}

/* Wyszukiwanie */
.search-container {
  margin-bottom: 1.5rem;
  text-align: center;
}

.search-input {
  padding: 0.8rem 1.2rem;
  width: 50%;
  border-radius: 25px;
  border: 1px solid #ccc;
  font-size: 1rem;
  background-color: #2c2c2c;
  color: #fff;
  transition: all 0.3s ease;
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

/* Kontener dla kart */
.course-cards-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  margin: 2rem 0;
}

@media (max-width: 1024px) {
  .course-cards-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .course-cards-container {
    grid-template-columns: 1fr;
  }
}
</style>
