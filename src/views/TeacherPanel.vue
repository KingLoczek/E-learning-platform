<template>
  <div class="teacher-panel">
    <h1>Welcome to the Dashboard</h1>
    <div v-if="isInstructor">
      <h2>Your Courses</h2>
      <div class="search-container">
        <input type="text" class="search-input" placeholder="Search for courses..." v-model="searchQuery" />
      </div>
      <div class="course-cards-container">
        <div v-for="course in filteredCourses" :key="course.id" class="course-card">
          <router-link :to="{ name: 'editcourse', params: { courseId: course.id } }" class="course-btn">
            <h3>{{ course.name }}</h3>
            <p>{{ course.description }}</p>
            <!--<p><strong>Status:</strong> {{ course.status_type }}</p>-->
            <p><strong>Access Key:</strong> {{ course.access_key || "N/A" }}</p>
          </router-link>
          <button @click="startEditing(course)" class="toggle-form-btn">Edit Info</button>
        </div>
      </div>

      <!-- Create Course Button -->
      <button @click="toggleCourseForm" class="toggle-form-btn">
        {{ isCourseFormVisible ? 'Cancel' : 'Create New Course' }}
      </button>

      <!-- Course Form -->
      <div v-if="isCourseFormVisible">
        <form @submit.prevent="createCourse" class="create-course-form">
          <input v-model="newCourse.name" placeholder="Course Name" required class="input-field" />
          <textarea v-model="newCourse.description" placeholder="Course Description" required class="input-field"></textarea>
          <input v-model="newCourse.access_key" placeholder="Access Key" class="input-field"/>
          <select v-model="newCourse.status_type" required class="select-field">
            <option value="ONGOING">Ongoing</option>
            <option value="COMPLETED">Completed</option>
            <option value="ARCHIVED">Archived</option>
          </select>
          <button type="submit" class="submit-btn">Create Course</button>
        </form>
      </div>

      <!-- Edit Course Form -->
      <div v-if="isEditingCourse">
        <h3>Edit Course</h3>
        <form @submit.prevent="updateCourse" class="create-course-form">
          <input v-model="editingCourse.name" placeholder="Course Name" required class="input-field" />
          <textarea v-model="editingCourse.description" placeholder="Course Description" required class="input-field"></textarea>
          <input v-model="editingCourse.access_key" placeholder="Access Key" class="input-field"/>
          <select v-model="editingCourse.status_type" required class="select-field">
            <option value="ONGOING">Ongoing</option>
            <option value="COMPLETED">Completed</option>
            <option value="ARCHIVED">Archived</option>
          </select>
          <button type="submit" class="submit-btn">Update Course</button>
        </form>
      </div>
    </div>

    <div v-else>
      <p>You are not authorized to access this panel.</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "TeacherPanel",
  data() {
    return {
      isInstructor: false,
      courses: [],
      newCourse: {
        name: "",
        description: "",
        access_key: "",
        status_type: "ONGOING",
      },
      editingCourse: null, // Store the course currently being edited
      isCourseFormVisible: false,
      isEditingCourse: false, // To toggle edit form visibility
      searchQuery: "",
      userData: {},
    };
  },
  computed: {
    filteredCourses() {
      return this.courses.filter(course =>
        course.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }
  },
  methods: {
    async fetchCourses() {
      try {
        const response = await axios.get("/api/course/owned");
        this.courses = response.data.filter(course => course.instructor_id === this.userData.id);
      } catch (error) {
        console.error("Error fetching courses:", error);
      }
    },
    async createCourse() {
      try {
        const courseData = {
          ...this.newCourse,
          instructor_id: this.userData.id,
        };

        const response = await axios.post("/api/course/", courseData);
        this.courses.push(response.data);

        this.newCourse = {
          name: "",
          description: "",
          access_key: "",
          status_type: "ONGOING",
        };

        alert("Course created successfully!");
      } catch (error) {
        console.error("Error creating course:", error);
        alert("Failed to create course.");
      }
    },
    async updateCourse() {
      try {
        const updatedCourseData = { ...this.editingCourse };

        const response = await axios.patch(`/api/course/${this.editingCourse.id}`, updatedCourseData);
        
        // Update the courses list with the modified course
        const index = this.courses.findIndex(course => course.id === this.editingCourse.id);
        if (index !== -1) {
          this.courses[index] = response.data;
        }

        this.isEditingCourse = false;
        alert("Course updated successfully!");
      } catch (error) {
        console.error("Error updating course:", error);
        alert("Failed to update course.");
      }
    },
    startEditing(course) {
      this.editingCourse = { ...course }; // Clone the course to prevent direct mutation
      this.isEditingCourse = true;
    },
    toggleCourseForm() {
      this.isCourseFormVisible = !this.isCourseFormVisible;
    },
  },
  mounted() {
    const userData = JSON.parse(localStorage.getItem("userData"));
    if (userData && userData.user_type === "instructor") {
      this.isInstructor = true;
      this.userData = userData;
      this.fetchCourses();
    } else {
      console.warn("Unauthorized access to Teacher Panel");
      this.$router.push("/courses");
    }
  },
};
</script>

<style scoped>
h1 {
  color: #C3A834;
  text-align: center;
  margin-bottom: 2rem;
}
h2,h3{color:white;}

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
  margin: 0 auto;
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

/* Kontener dla kursów - karty */
.course-cards-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  margin: 2rem 0;
}

.course-card {
  background-color: #333;
  border-radius: 10px;
  padding: 1.5rem;
  color: #fff;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.course-card:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.course-btn {
  text-decoration: none;
  color: inherit;
}

/* Styl kursu */
.course-btn h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #C3A834;
}

.course-btn p {
  font-size: 1rem;
  margin: 0.5rem 0;
}

/* Przycisk do tworzenia kursu */
.toggle-form-btn {
  background-color: #C3A834;
  color: #fff;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 25px;
  cursor: pointer;
  margin-top: 1.5rem;
  transition: background-color 0.3s ease;
}

.toggle-form-btn:hover {
  background-color: #ff8a00;
}

/* Formularz tworzenia kursu */
.create-course-form {
  display: flex;
  flex-direction: column;
  margin-top: 2rem;
}

.input-field, .select-field {
  padding: 1rem;
  margin: 0.8rem 0;
  border-radius: 25px;
  border: 1px solid #ccc;
  background-color: #2c2c2c;
  color: #fff;
  font-size: 1rem;
}

.input-field:focus, .select-field:focus {
  outline: none;
  border-color: #ffc107;
  background-color: #333;
}

.submit-btn {
  background-color: #C3A834;
  color: #fff;
  border: none;
  padding: 1rem;
  border-radius: 25px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background-color: #ff8a00;
}

/* Responsywność */
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
