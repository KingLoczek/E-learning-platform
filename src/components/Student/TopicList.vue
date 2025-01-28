<template>
  <div class="topic-detail">
    <h2>{{ topic.title }}</h2>
    <p class="description">{{ topic.description }}</p>

    <!-- Materials Section -->
    <div class="materials-list">
      <h3 @click="toggleSection('materials')" class="section-title">
        Materials
        <span class="toggle-icon" :class="{ open: isSectionOpen('materials') }">▼</span>
      </h3>
      <div v-if="isSectionOpen('materials')" class="topic-content">
        <ul>
          <li v-for="material in topic.materials" :key="material.id" class="material-card">
            <h4 class="material-name">{{ material.name }}</h4>
            <p>{{ material.content }}</p>
          </li>
        </ul>
      </div>
    </div>

    <!-- Assignments Section -->
    <div class="assignments-list">
      <h3 @click="toggleSection('assignments')" class="section-title">
        Assignments
        <span class="toggle-icon" :class="{ open: isSectionOpen('assignments') }">▼</span>
      </h3>
      <div v-if="isSectionOpen('assignments')" class="topic-content">
        <ul>
          <li v-for="assignment in topic.assignments" :key="assignment.id" class="assignment-card">
            <h4 class="assignment-name">{{ assignment.name }}</h4>
            <p>{{ assignment.content }}</p>
            <p class="assignment-date">Due: {{ formatDate(assignment.due_date) }}</p>
            <p class="assignment-date">Close: {{ formatDate(assignment.close_date) }}</p>

            <!-- Files Section -->
            <div class="files-section" v-if="assignment.files.length">
              <h5>Files:</h5>
              <ul>
                <li v-for="file in assignment.files" :key="file.id" class="file-item">
                  <span>{{ file.filename }}</span>
                  <button @click="downloadFile(file.id)">Download</button>
                </li>
              </ul>
            </div>

            <!-- File upload form -->
            <div class="action-buttons">
              <h5>Upload a file:</h5>
              <input type="file" @change="handleFileUpload($event, assignment.id)" />
            </div>
            <!-- Sekcja oceny i plików -->
            <div v-if="assignment.grade || assignment.submittedFiles.length">
              <h5>Submission Details:</h5>
              <p>Grade: {{ assignment.grade || "Not graded yet" }}</p>
              <p>Feedback: {{ assignment.feedback || "No feedback" }}</p>

              <!-- <div v-if="assignment.submittedFiles.length">
                <h5>Submitted Files:</h5>
                <ul>
                  <li v-for="file in assignment.submittedFiles" :key="file">
                    {{ file }}
                  </li>
                </ul>
              </div> -->
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "TopicDetail",
  props: {
    topicId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      topic: {
        title: "",
        description: "",
        materials: [],
        assignments: [],
      },
      openSections: {},
    };
  },
  methods: {
    async fetchTopicDetails() {
  try {
    // Pobierz szczegóły tematu
    const topicResponse = await axios.get(`/api/topic/${this.topicId}`);
    this.topic.title = topicResponse.data.title;
    this.topic.description = topicResponse.data.description;

    // Pobierz materiały dla tematu
    const materialsIdsResponse = await axios.get(`/api/topic/${this.topicId}/materials`);
    const materials = await Promise.all(
      materialsIdsResponse.data.map(async (materialId) => {
        const materialResponse = await axios.get(`/api/material/${materialId}`);
        return materialResponse.data;
      })
    );
    this.topic.materials = materials;

    // Pobierz zadania dla tematu
    const assignmentsIdsResponse = await axios.get(`/api/topic/${this.topicId}/assignments`);
    const assignments = await Promise.all(
      assignmentsIdsResponse.data.map(async (assignmentId) => {
        const assignmentResponse = await axios.get(`/api/assignment/${assignmentId}`);
        const assignment = assignmentResponse.data;

        // Pobierz pliki związane z zadaniem
        const filesIdsResponse = await axios.get(`/api/assignment/${assignmentId}/files`);
        const fileIds = filesIdsResponse.data;
        const files = await Promise.all(
          fileIds.map(async (fileId) => {
            const fileResponse = await axios.get(`/api/file/${fileId}`);
            return fileResponse.data;
          })
        );
        assignment.files = files;

        // Pobierz ocenę użytkownika i pliki
        const gradeDetails = await this.fetchGrades(assignmentId);
        assignment.grade = gradeDetails.grade;
        assignment.feedback = gradeDetails.feedback;
        assignment.submittedFiles = gradeDetails.files;

        return assignment;
      })
    );

    this.topic.assignments = assignments;
  } catch (error) {
    console.error("Error fetching topic details:", error);
  }
},
    toggleSection(section) {
      this.openSections[section] = !this.openSections[section];
    },

    isSectionOpen(section) {
      return !!this.openSections[section];
    },

    async downloadFile(fileId) {
      try {
        const response = await axios.get(`/api/file/${fileId}/download/`, {
          responseType: "blob",
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "file");
        document.body.appendChild(link);
        link.click();
        link.remove();
      } catch (error) {
        console.error("Błąd podczas pobierania pliku:", error);
      }
    },

    async handleFileUpload(event, assignmentId) {
      const file = event.target.files[0];
      if (!file) {
        return;
      }

      const formData = new FormData();
      formData.append("file", file);

      try {
        const uploadResponse = await axios.post("/api/file/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        const fileId = uploadResponse.data.id;

        const submissionPayload = {
          assignment: assignmentId,
          files: [fileId],
        };
        await axios.post("/api/submission/", submissionPayload);

        console.log("Plik przesłany i przypisany do zadania!");
      } catch (error) {
        console.error("Błąd podczas przesyłania pliku:", error);
      }
    },
    async fetchGrades(assignmentId) {
      try {
        // Pobierz dane zalogowanego użytkownika
        const userResponse = await axios.get('/api/user/me');
        const userId = userResponse.data.id;

        // Pobierz dane ocen dla zadania
        const gradingResponse = await axios.get(`/api/assignment/${assignmentId}/grading`);
        const userSubmission = gradingResponse.data.submissions.find(submission => submission.student.id === userId);

        // Jeśli użytkownik ma przypisane zgłoszenie
        if (userSubmission) {
          const grade = userSubmission.grades.length > 0 ? userSubmission.grades[0] : null;

          // Pobierz szczegóły plików dla zgłoszenia
          const fileDetails = await Promise.all(
            userSubmission.fileIds.map(async (file) => {
              const fileResponse = await axios.get(`/api/file/${file.id}`);
              return fileResponse.data.filename;
            })
          );

          return {
            grade: grade ? grade.grade : null,
            feedback: grade ? grade.feedback : null,
            files: fileDetails,
          };
        }

        return { grade: null, feedback: null, files: [] }; // Brak zgłoszenia
      } catch (error) {
        console.error("Error fetching grades:", error);
        return { grade: null, feedback: null, files: [] };
      }
    },

    formatDate(timestamp) {
        if (!timestamp || typeof timestamp !== "number") return "Invalid date";
        try {
          const date = new Date(timestamp * 1000); // Konwersja z sekund na milisekundy
          if (isNaN(date.getTime())) throw new Error("Invalid timestamp");
          const day = date.getDate().toString().padStart(2, "0");
          const month = (date.getMonth() + 1).toString().padStart(2, "0");
          const year = date.getFullYear();
          const hours = date.getHours().toString().padStart(2, "0");
          const minutes = date.getMinutes().toString().padStart(2, "0");
          return `${day}-${month}-${year} ${hours}:${minutes}`;
        } catch (error) {
          console.error("Error formatting date:", error);
          return "Invalid date";
        }
      },

  },
  mounted() {
    if (this.topicId) {
      this.fetchTopicDetails();
    } else {
      console.error("Topic ID is undefined!");
    }
  },
};
</script>

<style scoped>
/* Ogólne style dla kontenera */
.topic-detail {
  padding: 2rem;
  background-color: #2C2C2C;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  margin-bottom: 2rem;
}

/* Tytuł sekcji */
.topic-detail h2 {
  font-size: 2rem;
  color: #FFD54F; /* Jaśniejszy żółty */
  margin-bottom: 1rem;
  border-bottom: 2px solid #FFD54F;
  padding-bottom: 0.5rem;
}

/* Akapit opisu */
.description {
  font-size: 1.1rem;
  color: #F5F5F5; /* Jasnoszary dla czytelności */
  margin-bottom: 2rem;
}

/* Styl dla sekcji harmonijki */
.section-title {
  font-size: 1.3rem;
  color: #FFD54F;
  margin-bottom: 1rem;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toggle-icon {
  font-size: 1.5rem;
  transition: transform 0.3s;
  color: #FFFFFF;
}

.toggle-icon.open {
  transform: rotate(180deg);
}

.topic-content {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #333333;
  border-radius: 8px;
}

/* Karty materiałów i zadań */
.material-card, .assignment-card {
  background: #424242;
  border: 1px solid #3C3C3C;
  border-radius: 10px;
  padding: 1rem;
  margin-bottom: 1rem;
  transition: transform 0.2s, box-shadow 0.2s;
}

.material-card:hover, .assignment-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.material-name, .assignment-name {
  color: #FFC107; /* Wyrazisty żółty dla nagłówków */
}

.material-card p, .assignment-card p {
  color: #E0E0E0; /* Jasnoszary dla treści */
}

/* Styl dla przycisków */
button {
  background: #FFC107;
  color: #2C2C2C;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #FFB300;
}

button:disabled {
  background-color: #555555;
  cursor: not-allowed;
}

/* Lista plików */
h5 {
  color: #FFD54F; /* Jaśniejszy żółty */
  margin-bottom: 0.5rem;
}

.file-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.file-item span {
  color: #E0E0E0; /* Jasnoszary */
}

.file-item button {
  background: #FFC107;
  color: #2C2C2C;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.file-item button:hover {
  background: #FFB300;
}

/* Daty */
.assignment-date {
  color: #CCCCCC; /* Nieco jaśniejszy szary */
  font-size: 0.9rem;
}

/* Inne drobne style */
.white {
  color: #FFFFFF;
}

ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}

</style>