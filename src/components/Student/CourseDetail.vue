<template>
  <div class="course-detail">
    <h1>{{ course.name }}</h1>
    <p class="course-description">{{ course.description }}</p>

    <!-- Instructor Section -->
    <div v-if="instructor">
      <p class="course-description"><strong>Instructor:</strong> {{ instructor.first_name }} {{ instructor.last_name }}</p>
    </div>

    <!-- Enrollment Section -->
    <div class="enrollment-section" v-if="!enrolled">
      <input
        v-model="accessKey"
        type="text"
        placeholder="Enter access key"
        class="access-key-input"
      />
    </div>
    <button @click="enroll" :disabled="!accessKey" v-if="!enrolled" class="enroll-button">Enroll</button>
    <p v-if="enrolled" class="enrolled-message">You are enrolled in this course!</p>

    <!-- Topics Section -->
    <div class="topics-list">
      <TopicDetail v-for="topic in topics" :key="topic.id" :topicId="topic.id" />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TopicDetail from './TopicList.vue';

export default {
  name: 'CourseDetail',
  components: { TopicDetail },
  data() {
    return {
      course: {},
      enrolled: false,
      topics: [],
      accessKey: '',
      loggedInUser: null,  // Zmienna do przechowywania informacji o aktualnie zalogowanym użytkowniku
      students: [],        // Zmienna do przechowywania listy studentów
      instructor: null,    // Zmienna do przechowywania danych instruktora
    };
  },
  methods: {
    fetchCourse() {
      axios.get(`/api/course/${this.$route.params.id}`)
        .then(response => {
          this.course = response.data;
          // Po pobraniu kursu, pobieramy dane instruktora
          this.fetchInstructor(this.course.instructor_id);
        })
        .catch(error => {
          console.error('Error fetching course:', error);
        });
    },
    fetchInstructor(instructorId) {
      axios.get(`/api/user/${instructorId}`)
        .then(response => {
          this.instructor = response.data;
        })
        .catch(error => {
          console.error('Error fetching instructor:', error);
        });
    },
    async fetchTopics() {
      try {
        const response = await axios.get(`/api/course/${this.$route.params.id}/topics`);
        this.topics = response.data.map(topicId => ({ id: topicId }));
      } catch (error) {
        console.error('Error fetching topics:', error);
      }
    },
    async fetchStudents() {
      try {
        const response = await axios.get(`/api/course/${this.$route.params.id}/students`);
        this.students = response.data;
        this.checkEnrollment();
      } catch (error) {
        console.error('Error fetching students:', error);
      }
    },
    checkEnrollment() {
      const loggedInUserId = this.loggedInUser ? this.loggedInUser.id : null;
      // console.log('Logged in user ID:', loggedInUserId);
      // console.log('Students in course:', this.students);

      // Sprawdzamy, czy student jest na liście zapisanych
      this.enrolled = this.students.map(student => student).includes(loggedInUserId);

      // console.log('Enrollment status:', this.enrolled);
    },

    async getLoggedInUser() {
      try {
        const response = await axios.get('/api/user/me');
        this.loggedInUser = response.data;
        this.fetchStudents();  // Pobranie studentów po uzyskaniu danych o zalogowanym użytkowniku
      } catch (error) {
        console.error('Error fetching logged-in user:', error);
      }
    },
    enroll() {
      const data = {
        access_key: this.accessKey,
      };
      axios.post(`/api/course/${this.$route.params.id}/enroll`, data)
        .then(() => {
          this.enrolled = true;
          this.fetchStudents(); // Zaktualizowanie stanu studentów po zapisie
        })
        .catch(error => {
          console.error('Error enrolling in course:', error);
        });
    },
  },
  watch: {
    enrolled(newVal) {
      // Kiedy użytkownik zapisze się na kurs, ukryj formularz zapisu
      if (newVal) {
        this.accessKey = ''; // Resetowanie kodu dostępu po zapisaniu
      }
    },
  },
  mounted() {
    this.getLoggedInUser().then(() => {
      this.fetchStudents(); // Wywołanie fetchStudents dopiero po załadowaniu użytkownika
    });
    this.fetchCourse();
    this.fetchTopics();
  },
};
</script>

<style scoped>
.course-detail {
  padding: 2rem;
  background-color: #1e1e1e;
  border-radius: 10px;
  margin: 2rem auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 80%;
}

.course-detail h1 {
  color: #ffc107;
  margin-bottom: 1rem;
}

.course-description {
  font-size: 1.1rem;
  margin-bottom: 1rem;
  color: #cccccc;
}

.enrollment-section {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.access-key-input {
  padding: 0.5rem;
  width: 60%;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
}

.enroll-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.enroll-button:hover {
  background-color: #0056b3;
}

.enrolled-message {
  color: #28a745;
  font-size: 1rem;
  margin-bottom: 2rem;
}

.topics-list {
  margin-top: 2rem;
}
</style>
