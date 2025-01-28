<template>
    <div class="edit-course">
      <h1>{{ course.name }}</h1>
      <p class="description">{{ course.description }}</p>
  
      <div class="course-content">
        <!-- Left Column: Content (Materials, Assignments) -->
        <div class="content-column">
          <div v-if="selectedCourse.topics && selectedCourse.topics.length > 0">
            <div v-for="topic in selectedCourse.topics" :key="topic.id" class="topic-card">
              <!-- Move buttons to the top of each topic -->
              <div class="topic-header">
                <button @click="showEditTopicForm(topic)">Edit Topic</button>
                <!-- <button @click="deleteTopic(topic.id)">Delete Topic</button> -->
              </div>
              <div @click="toggleTopicVisibility(topic)" class="topic-header">
                <h3>{{ topic.title }}</h3>
                <span class="toggle-icon" :class="{ open: topic.isOpen }">▼</span>
              </div>
              <div v-show="topic.isOpen" class="topic-content">
                <h4 >{{ topic.description }}</h4>
                <div v-if="topic.materials && topic.materials.length > 0" class="materials-section">
                    <h5 class="section-title">Materials</h5>
                    <div v-for="material in topic.materials" :key="material.id" class="material-card">
                    <h6 class="material-name">{{ material.name }}</h6>
                    <p class="material-content">{{ material.content }}</p>
                    <div v-if="material.files && material.files.length > 0" class="files-section">
                        <h6>Files:</h6>
                        <ul class="files-list">
                        <li v-for="file in material.files" :key="file.id" class="file-item">
                            <span>{{ file.filename }}</span>
                            <button @click="downloadFile(file.id)">Download</button>
                        </li>
                        </ul>
                    </div>
                    <div class="action-buttons">
                        <button @click="showEditMaterialsForm(material)">Edit Material</button>
                        <!--<button @click="deleteMaterial(material.id)">Delete Material</button>-->
                    </div>
                    </div>
                </div>

                <!-- Assignments Section -->
                <div v-if="topic.assignments && topic.assignments.length > 0" class="assignments-section">
                    <h5 class="section-title">Assignments</h5>
                    <div v-for="assignment in topic.assignments" :key="assignment.id" class="assignment-card">
                    <h6 class="assignment-name">{{ assignment.name }}</h6>
                    <p class="assignment-date">Due: {{ formatDate(assignment.due_date) }}</p>
                    <p class="assignment-date">Close: {{ formatDate(assignment.close_date) }}</p>
                    <div v-if="assignment.files.length > 0" class="files-section">
                        <h6>Files:</h6>
                        <ul class="files-list">
                        <li v-for="file in assignment.files" :key="file.id" class="file-item">
                            <span>{{ file.filename }}</span>
                            <button @click="downloadFile(file.id)">Download</button>
                        </li>
                        </ul>
                    </div>
                    <div class="action-buttons">
                        <button @click="showGradingForm(assignment)">Grade Assignment</button>
                        <button @click="showEditAssignmentsForm(assignment)">Edit Assignment</button>
                        <!--<button @click="deleteAssignment(assignment.id)">Delete Assignment</button>-->
                    </div>
                    </div>
                </div>
              </div>
            </div>
          </div>
            <button @click="isAddTopicVisible = true">Add Topic</button>
            <button @click="isAddMaterialVisible = true">Add Material</button>
            <button @click="isAddAssignmentVisible = true">Add Assignment</button>
        </div>
  
        <!-- Right Column: Edit Forms (Topic, Materials, Assignments) -->
        <div class="edit-forms-column">
          <div v-if="isEditTopicVisible" class="edit-form">
            <h3>Edit Topic</h3>
            <input v-model="editTopicData.title" placeholder="Title" />
            <textarea v-model="editTopicData.description" placeholder="Description"></textarea>
            <button @click="editTopic(editTopicData)">Save</button>
            <button @click="hideEditTopicForm">Cancel</button>
          </div>
  
          <div v-if="isEditMaterialsVisible" class="edit-form">
            <h3>Edit Materials</h3>
            <input v-model="editMaterialData.name" placeholder="Material Name" />
            <textarea v-model="editMaterialData.content" placeholder="Content"></textarea>
            <input type="file" @change="handleFileUpload($event, 'material')" multiple />
            <button @click="editMaterial(editMaterialData)">Save</button>
            <button @click="hideEditMaterialsForm">Cancel</button>
          </div>
  
          <div v-if="isEditAssignmentsVisible" class="edit-form">
            <h3>Edit Assignment</h3>
            <input v-model="editAssignmentData.name" placeholder="Assignment Name" />
            <textarea v-model="editAssignmentData.content" placeholder="Content"></textarea>
            <input v-model="editAssignmentData.due_date" type="datetime-local" />
            <input v-model="editAssignmentData.close_date" type="datetime-local" />
            <input type="file" @change="handleFileUpload($event, 'assignment')" multiple />
            <button @click="editAssignment(editAssignmentData)">Save</button>
            <button @click="hideEditAssignmentsForm">Cancel</button>
          </div>
          <div v-if="isAddTopicVisible" class="edit-form">
            <h3>Add Topic</h3>
            <input v-model="newTopicData.title" placeholder="Title" />
            <textarea v-model="newTopicData.description" placeholder="Description"></textarea>
            <button @click="addTopic">Add</button>
            <button @click="isAddTopicVisible = false">Cancel</button>
        </div>

        <div v-if="isAddMaterialVisible" class="edit-form">
            <h3>Add Material</h3>
            <h4>Select the topic</h4>
            <select v-model="newMaterialData.topicId">
            <option value="" disabled selected>Pick a Topic</option>
            <option v-for="topic in selectedCourse.topics" :key="topic.id" :value="topic.id">{{ topic.title }}</option>
            </select>
            <input v-model="newMaterialData.name" placeholder="Material Name" />
            <textarea v-model="newMaterialData.content" placeholder="Content"></textarea>
            <input type="file" @change="handleFileUpload($event, 'material')" multiple />
            <button @click="addMaterial">Add</button>
            <button @click="isAddMaterialVisible = false">Cancel</button>
        </div>

        <div v-if="isAddAssignmentVisible" class="edit-form">
            <h3>Add Assignment</h3>
            <h4>Select the topic</h4>
            <select v-model="newAssignmentData.topicId">
            <option value="" disabled selected>Pick a Topic</option>
            <option v-for="topic in selectedCourse.topics" :key="topic.id" :value="topic.id">{{ topic.title }}</option>
            </select>
            <input v-model="newAssignmentData.name" placeholder="Assignment Name" />
            <textarea v-model="newAssignmentData.content" placeholder="Content"></textarea>
            <input v-model="newAssignmentData.due_date" type="datetime-local" />
            <input v-model="newAssignmentData.close_date" type="datetime-local" />
            <input type="file" @change="handleFileUpload($event, 'assignment')" multiple />
            <button @click="addAssignment">Add</button>
            <button @click="isAddAssignmentVisible = false">Cancel</button>
        </div>
        <div v-if="gradingAssignment" class="edit-form grading-form-container">
            <div class="grading-form">
              <h4>Grading Assignment: {{ gradingAssignment.name }}</h4>

              <!-- Select do wyboru studentów -->
              <div class="select-student">
                <label for="studentSelect">Select Student:</label>
                <select v-model="selectedStudentId" id="studentSelect" @change="selectSubmissionById">
                  <option value="" disabled>Select a student</option>
                  <option v-for="submission in gradingAssignment.submissions" :key="submission.id" :value="submission.id">
                    {{ submission.student.first_name }} {{ submission.student.last_name }}
                  </option>
                </select>
              </div>

              <!-- Szczegóły submission dla wybranego studenta -->
              <div v-if="selectedSubmission" class="submission">
                <!-- <p>Student: {{ selectedSubmission.student.first_name }} {{ selectedSubmission.student.last_name }}</p> -->
                <p>Files Submitted:</p>
                <ul class="files-list">
                  <li v-for="file in selectedSubmission.fileIds" :key="file.id" class="file-item">
                    <a :href="'/api/file/' + file.id + '/download/'" target="_blank">
                      {{ file.filename }}
                    </a> 
                    <span> (Submitted At: {{ formatDate(selectedSubmission.submittedAt) }}) </span>
                  </li>
                </ul>

                <!-- Grade Input (2-5) -->
                <div class="grade-input">
                  <label for="grade">Grade (2-5):</label>
                  <select v-model="selectedSubmission.grade" id="grade" :disabled="isGradeDisabled(selectedSubmission)">
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                  </select>
                </div>

                <!-- Feedback Input -->
                <div class="feedback-input">
                  <label for="feedback">Feedback:</label>
                  <textarea v-model="selectedSubmission.feedback" id="feedback" placeholder="Enter feedback for the student"></textarea>
                </div>

                <!-- Submit or Edit Grade -->
                <button @click="submitGrade(selectedSubmission)">
                  {{ selectedSubmission.grades.length > 0 ? 'Edit Grade' : 'Submit Grade' }}
                </button>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    name: 'EditCourse',
    props: {
      courseId: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        course: null,
        selectedCourse: {
          topics: [],
        },
        isEditTopicVisible: false,
        isEditMaterialsVisible: false,
        isEditAssignmentsVisible: false,
        isAddTopicVisible: false,
        isAddMaterialVisible: false,
        isAddAssignmentVisible: false,
        gradingAssignment: null,
        selectedSubmission: null,
        editTopicData: {
          title: '',
          description: '',
        },
        editMaterialData: {
          name: '',
          content: '',
        },
        editAssignmentData: {
          name: '',
          content: '',
          due_date: '',
          close_date: '',
        },
        newTopicData: {
        title: '',
        description: '',
        },
        newMaterialData: {
        name: '',
        content: '',
        topicId: null,
        },
        newAssignmentData: {
        name: '',
        content: '',
        due_date: '',
        close_date: '',
        topicId: null,
        },
      };
    },
    async created() {
      try {
        const courseResponse = await axios.get(`/api/course/${this.courseId}`);
        this.course = courseResponse.data;
        await this.fetchTopics(this.course.id);
      } catch (error) {
        console.error('Error fetching course details for editing:', error);
      }
    },
    methods: {
      async fetchTopics(courseId) {
        try {
            const response = await axios.get(`/api/course/${courseId}/topics`);
            const topicPromises = response.data.map(async (topicId) => {
            const topicResponse = await axios.get(`/api/topic/${topicId}`);
            const topicData = topicResponse.data;

            // Pobieranie materiałów
            const materialsResponse = await axios.get(`/api/topic/${topicId}/materials`);
            const materialsPromises = materialsResponse.data.map(async (materialId) => {
                const materialResponse = await axios.get(`/api/material/${materialId}`);
                const materialData = materialResponse.data;

                // Pobieranie plików przypisanych do materiału po ID
                const filesResponse = await axios.get(`/api/material/${materialId}/files`);
                const filesIds = filesResponse.data;  // Lista ID plików przypisanych do materiału
                
                // Pobieranie szczegółów plików
                const files = await Promise.all(
                filesIds.map(async (fileId) => {
                    const fileResponse = await axios.get(`/api/file/${fileId}`);
                    return fileResponse.data; // Zwraca szczegóły pliku
                })
                );

                materialData.files = files;  // Dodanie plików do materiału
                return materialData;
            });
            topicData.materials = await Promise.all(materialsPromises);

            // Pobieranie zadań
            const assignmentsResponse = await axios.get(`/api/topic/${topicId}/assignments`);
            const assignmentsPromises = assignmentsResponse.data.map(async (assignmentId) => {
                const assignmentResponse = await axios.get(`/api/assignment/${assignmentId}`);
                const assignmentData = assignmentResponse.data;

                // Pobieranie plików przypisanych do zadania po ID
                const filesResponse = await axios.get(`/api/assignment/${assignmentId}/files`);
                const filesIds = filesResponse.data;  // Lista ID plików przypisanych do zadania
                
                // Pobieranie szczegółów plików
                const files = await Promise.all(
                filesIds.map(async (fileId) => {
                    const fileResponse = await axios.get(`/api/file/${fileId}`);
                    return fileResponse.data; // Zwraca szczegóły pliku
                })
                );

                assignmentData.files = files;  // Dodanie plików do zadania
                return assignmentData;
            });
            topicData.assignments = await Promise.all(assignmentsPromises);

            return topicData;
            });

            const topics = await Promise.all(topicPromises);
            this.selectedCourse.topics = topics;
        } catch (error) {
            console.error('Error fetching topics:', error);
        }
      },
  
      showEditTopicForm(topic) {
            this.isEditTopicVisible = true;
            this.editTopicData = { ...topic };
        },
        hideEditTopicForm() {
            this.isEditTopicVisible = false;
        },

        // Modyfikacja metody, aby przekazywała dane materiału
        showEditMaterialsForm(material) {
            this.isEditMaterialsVisible = true;
            this.editMaterialData = { ...material };  // przekazujemy cały materiał, w tym id
        },
        hideEditMaterialsForm() {
            this.isEditMaterialsVisible = false;
        },

        // Modyfikacja metody, aby przekazywała dane zadania
        showEditAssignmentsForm(assignment) {
            this.isEditAssignmentsVisible = true;
            this.editAssignmentData = { ...assignment };  // przekazujemy całe zadanie, w tym id
        },
        hideEditAssignmentsForm() {
            this.isEditAssignmentsVisible = false;
        },
        async addTopic() {
          try {
              console.log(this.course.id);
              const response = await axios.post(`/api/topic/`, {
                  title: this.newTopicData.title,
                  description: this.newTopicData.description,
                  courseId: this.course.id  // Przesyłanie courseId do API
              });
              // Dodanie nowego tematu do listy tematów kursu
              this.selectedCourse.topics.push(response.data);
              // Reset formularza
              this.newTopicData = { title: '', description: '' };
              this.isAddTopicVisible = false;
          } catch (error) {
              console.error('Error adding topic:', error);
          }
      },
      async handleFileUpload(event, type) {
          const files = event.target.files;
          if (!files.length) {
              return;
          }

          const file = files[0]; // Pobierz pierwszy plik (lub obsłuż wszystkie pliki)
          const reader = new FileReader();

          reader.onloadend = async () => {
              // Zakodowanie pliku na Base64
              const base64File = reader.result.split(',')[1]; // Zmieniamy z danych URL na czysty Base64

              try {
                  // 1. Przesyłanie pliku w formacie Base64
                  const uploadResponse = await axios.post("/api/file/", {
                      filename: file.name,
                      type: file.type, //.split('/')[1].toUpperCase(),  // Określamy typ na podstawie typu MIME
                      content: base64File,
                  });

                  // 2. Otrzymywanie ID pliku po przesłaniu
                  const fileId = uploadResponse.data.id;

                  // 3. Dodanie pliku do zadania lub materiału
                  if (type === 'material') {
                      this.newMaterialData.files = [fileId]; // Przypisanie ID do materiału
                  } else if (type === 'assignment') {
                      this.newAssignmentData.files = [fileId]; // Przypisanie ID do zadania
                  }

              } catch (error) {
                  console.error(`Błąd podczas przesyłania pliku do ${type}:`, error);
              }
          };

          // Odczytaj plik jako Base64
          reader.readAsDataURL(file);
      },

        async addMaterial() {
            try {
            const response = await axios.post(`/api/material/`, {
                name: this.newMaterialData.name,
                content: this.newMaterialData.content,
                files: this.newMaterialData.files,
                topic_id: this.newMaterialData.topicId,
            });
            const topic = this.selectedCourse.topics.find(t => t.id === this.newMaterialData.topicId);
            topic.materials.push(response.data);
            this.newMaterialData = { name: '', content: '', topicId: null }; // Reset formularza
            this.isAddMaterialVisible = false;
            } catch (error) {
            console.error('Error adding material:', error);
            }
        },

        async addAssignment() {
            try {
            const response = await axios.post(`/api/assignment/`, {
                name: this.newAssignmentData.name,
                content: this.newAssignmentData.content,
                due_date: new Date(this.newAssignmentData.due_date).getTime(),
                close_date: new Date(this.newAssignmentData.close_date).getTime(),
                files: this.newAssignmentData.files,
                topic_id: this.newAssignmentData.topicId,
            });
            const topic = this.selectedCourse.topics.find(t => t.id === this.newAssignmentData.topicId);
            topic.assignments.push(response.data);
            this.newAssignmentData = { name: '', content: '', due_date: '', close_date: '', topicId: null }; // Reset formularza
            this.isAddAssignmentVisible = false;
            } catch (error) {
            console.error('Error adding assignment:', error);
            }
        },
      async editTopic(topic) {
        try {
            const response = await axios.patch(`/api/topic/${topic.id}`, topic);
            this.selectedCourse.topics = this.selectedCourse.topics.map(t => (t.id === topic.id ? response.data : t));
        } catch (error) {
            console.error("Error editing topic:", error);
        }
        },
        async deleteTopic(topicId) {
        try {
            await axios.delete(`/api/topic/${topicId}`);
            this.selectedCourse.topics = this.selectedCourse.topics.filter(topic => topic.id !== topicId);
        } catch (error) {
            console.error("Error deleting topic:", error);
        }
        },
        async editMaterial(material) {
            try {
                // Przekształcenie tablicy plików na tablicę tylko z ID plików
                const fileIds = material.files.map(file => file.id);

                // Ustawienie danych do formularza edycji
                this.editMaterialData = {
                    ...material,
                    files: fileIds, // Dodanie ID plików do formularza
                };

                // Aktualizacja materiału w wybranym kursie
                const response = await axios.patch(`/api/material/${material.id}`, {
                    ...material,
                    files: fileIds, // Wysyłamy tylko identyfikatory plików
                });

                this.selectedCourse.topics.forEach(topic => {
                    topic.materials = topic.materials.map(m => (m.id === material.id ? response.data : m));
                });
            } catch (error) {
                console.error("Error editing material:", error);
            }
        },
        async deleteMaterial(materialId) {
        try {
            await axios.delete(`/api/material/${materialId}`);
            this.selectedCourse.topics.forEach(topic => {
            topic.materials = topic.materials.filter(material => material.id !== materialId);
            });
        } catch (error) {
            console.error("Error deleting material:", error);
        }
        },
        async editAssignment(assignment) {
          try {
              // Konwertowanie daty due_date i close_date na timestamp w milisekundach
              const dueDateTimestamp = new Date(assignment.due_date).toISOString();
              const closeDateTimestamp = new Date(assignment.close_date).toISOString();

              // Ustawiamy przekształcone daty
              assignment.due_date = dueDateTimestamp;
              assignment.close_date = closeDateTimestamp;

              // Przekształcenie tablicy plików na tablicę tylko z ID plików
              const fileIds = assignment.files.map(file => file.id);

              // Wysyłamy zaktualizowane dane z tylko identyfikatorami plików
              const response = await axios.patch(`/api/assignment/${assignment.id}`, {
                  ...assignment,
                  files: fileIds, // Wysyłamy tylko identyfikatory plików
              });

              // Aktualizacja zadania w wybranym kursie
              this.selectedCourse.topics.forEach(topic => {
                  topic.assignments = topic.assignments.map(a => (a.id === assignment.id ? response.data : a));
              });
          } catch (error) {
              console.error("Error editing assignment:", error);
          }
      },
        async deleteAssignment(assignmentId) {
        try {
            await axios.delete(`/api/assignment/${assignmentId}`);
            this.selectedCourse.topics.forEach(topic => {
            topic.assignments = topic.assignments.filter(a => a.id !== assignmentId);
            });
        } catch (error) {
            console.error("Error deleting assignment:", error);
        }
        },
  
      async downloadFile(fileId) {
        try {
          const response = await axios.get(`/api/file/${fileId}/download/`, {
            responseType: 'blob',
          });
  
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'file');
          document.body.appendChild(link);
          link.click();
          link.remove();
        } catch (error) {
          console.error('Error downloading file:', error);
        }
      },
      // Show grading form for the selected assignment
    async showGradingForm(assignment) {
        try {
            const response = await axios.get(`/api/assignment/${assignment.id}/grading`);
            console.log("Grading data:", response.data);
            this.gradingAssignment = {
                ...assignment,
                submissions: response.data.submissions
            };
        } catch (error) {
            console.error('Error fetching grading data:', error);
        }
    },
    selectSubmission(submission) {
        this.selectedSubmission = submission;
        console.log(this.selectSubmission);
    },
    selectSubmissionById() {
    // Znajdź submission na podstawie ID wybranego studenta
    this.selectedSubmission = this.gradingAssignment.submissions.find(
      (submission) => submission.id === this.selectedStudentId
    );
  },
  isGradeDisabled(submission) {
    // Logika do sprawdzania, czy ocena jest zablokowana
    return submission.grades.length > 0;
  },
  formatDate(date) {
    // Funkcja formatująca datę
    return new Date(date*1000).toLocaleString();
  },
  async submitGrade(submission) {
    // Funkcja do wysyłania lub edytowania oceny
    try {
      const userResponse = await axios.get('/api/user/me');
      const instructorId = userResponse.data.id;  // ID zalogowanego użytkownika
      const payload = {
        grade: submission.grade,
        feedback: submission.feedback,
        instructorId: instructorId,  // Ustawienie ID instruktora
        submission: submission.id,
      };
      console.log(payload);
      await axios.post(`/api/grade/`, payload);
      // Aktualizacja danych lokalnych
      this.gradingAssignment.submissions = this.gradingAssignment.submissions.map(sub => 
                sub.id === submission.id ? { ...sub, grade: submission.grade, feedback: submission.feedback } : sub
      );
      alert("Grade submitted successfully!");
    } catch (error) {
      console.error("Error submitting grade:", error);
    }
  },
      toggleTopicVisibility(topic) {
        topic.isOpen = !topic.isOpen;
      },
    },
  };
  </script>
  
  <style scoped>
  .edit-course {
    padding: 2rem;
    background-color: #2C2C2C;
    border-radius: 12px;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    margin-bottom: 2rem;
  }
  
  .edit-course h1 {
    color: #FFC107;
    font-size: 2rem;
    margin-bottom: 1rem;
  }
  
  .description {
    font-size: 1.1rem;
    color: #E0E0E0;
    margin-bottom: 2rem;
  }
  
  /* Layout for two columns */
  .course-content {
    display: grid;
    grid-template-columns: 3fr 1.5fr;
    gap: 2rem;
  }
  
  /* Left Column: Content (Topics, Materials, Assignments) */
  .content-column {
    padding-right: 1rem;
  }
  
  /* Topic Cards */
  .topic-card {
    background: #424242;
    border: 1px solid #3C3C3C;
    border-radius: 10px;
    padding: 1.5rem;
    margin-bottom: 1.5rem;
    transition: transform 0.2s, box-shadow 0.2s;
  }
  
  .topic-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .topic-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer;
    font-weight: bold;
    font-size: 1.2rem;
    color: #FFFFFF;
  }
  
  .toggle-icon {
    font-size: 1.5rem;
    transition: transform 0.3s ease;
  }
  
  .toggle-icon.open {
    transform: rotate(180deg);
  }
  
  .topic-content {
    margin-top: 1rem;
    padding-left: 1.5rem;
    border-left: 3px solid #FFC107;
  }
  
  /* Materials and Assignments */
  .materials-section, .assignments-section {
    margin-top: 1rem;
  }
  
  .material-item, .assignment-item {
    margin-bottom: 1rem;
    font-size: 1rem;
    color: #CCCCCC;
    border-bottom: 1px solid #3C3C3C;
    padding-bottom: 0.5rem;
  }
  
  .files-list {
    margin-top: 0.5rem;
  }
  
  .files-list ul {
    list-style: none;
    padding-left: 0;
  }
  
  .files-list li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
  }
  
  .files-list button {
    background: #FFC107;
    color: #2C2C2C;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 5px;
    cursor: pointer;
    transition: background 0.3s;
  }
  
  .files-list button:hover {
    background: #FFB300;
  }
  
  /* Buttons for Editing */
  button {
    background: #FFC107;
    color: #2C2C2C;
    padding: 0.8rem 1.5rem;
    margin: .5rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 1rem;
    transition: background 0.3s, transform 0.2s;
  }
  
  button:hover {
    background: #FFB300;
    transform: translateY(-3px);
  }
  
  button:active {
    transform: translateY(0);
  }
  
  /* Edit Forms */
  .edit-form {
    background: #3C3C3C;
    border-radius: 10px;
    padding: 1.5rem;
    margin-top: 1rem;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .edit-form input, .edit-form textarea, .edit-form select {
    width: 80%;
    background: #2C2C2C;
    color: #FFFFFF;
    border: 1px solid #555555;
    padding: 0.8rem;
    margin-bottom: 1rem;
    border-radius: 5px;
    font-size: 1rem;
  }
  
  .edit-form input:focus, .edit-form textarea:focus, .edit-form select:focus {
    border-color: #FFC107;
    outline: none;
  }
  
  .edit-form button {
    width: auto;
    margin-top: 1rem;
  }
  
  .edit-form button:first-of-type {
    margin-right: 1rem;
  }
  .topic-content {
  margin-top: 1rem;
  padding: 1.5rem;
  background-color: #333333;
  border-radius: 8px;
}

p {color:white;}
/* Section Titles */
.section-title {
  font-size: 1.3rem;
  color: #FFC107;
  margin-bottom: 1rem;
  border-bottom: 2px solid #FFC107;
  padding-bottom: 0.5rem;
}

/* Material and Assignment Cards */
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

/* Card Headers */
.material-name, .assignment-name {
  font-size: 1.2rem;
  color: #FFFFFF;
  font-weight: bold;
}

.assignment-date {
  font-size: 1rem;
  color: #CCCCCC;
  margin-top: 0.5rem;
}

/* Files Section */
.files-section {
  margin-top: 0.5rem;
}

.file-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.file-item button {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
}

/* Action Buttons */
.action-buttons {
  margin-top: 1rem;
}

.action-buttons button {
  margin-right: 0.5rem;
}
h4, h3{color:white;}

/* Grading Form Styles */
.grading-form-container {
  background: #3C3C3C;
  border-radius: 10px;
  padding: 1.5rem;
  margin-top: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.grading-form h4 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #FFC107;
}

.submission-item {
  margin-bottom: 1.5rem;
}

.submission-item p {
  font-size: 1.1rem;
  color: #FFFFFF;
}

.submission-item .files-list {
  margin-top: 0.5rem;
}

.submission-item .files-list .file-item {
  font-size: 1rem;
  color: #CCCCCC;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.submission-item textarea {
  width: 100%;
  background: #2C2C2C;
  color: #FFFFFF;
  border: 1px solid #555555;
  padding: 0.8rem;
  margin-bottom: 1rem;
  border-radius: 5px;
  font-size: 1rem;
}

.submission-item textarea:focus {
  border-color: #FFC107;
  outline: none;
}

.submission-item button {
  background: #FFC107;
  color: #2C2C2C;
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.3s, transform 0.2s;
}

.submission-item button:hover {
  background: #FFB300;
  transform: translateY(-3px);
}

.submission-item button:active {
  transform: translateY(0);
}

.feedback-input{
  width: 80%;
}
.submission{
  display: flex;
    flex-direction: column;
    align-items: center;
}
  </style>
  
