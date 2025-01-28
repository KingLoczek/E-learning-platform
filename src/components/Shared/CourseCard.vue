<template>
    <div class="course-card">
      <h3>{{ course.name }}</h3>
      <p>{{ course.description }}</p>
      <!--<p class="status"> {{course.status_type}} </p>-->
      <button @click="viewDetails">View Details</button>
    </div>
  </template>
  
  <script>
  export default {
    name: 'CourseCard',
    props: {
      course: {
        type: Object,
        required: true,
      },
    },
    methods: {
      viewDetails() {
        this.$emit('view-details', this.course.id);
      },
    },
  };
  </script>
  
  <style scoped>
  /* Karta kursu */
  .course-card {
    background-color: #1e1e1e;
    color: #ffffff;
    border: 1px solid #2b2b2b;
    border-radius: 8px;
    padding: 1.5rem;
    text-align: center;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }
  
  .course-card h3 {
    font-size: 1.5rem;
    color: #ffc107;
    margin-bottom: 0.5rem;
  }
  
  .course-card p {
    font-size: 1rem;
    color: #cccccc;
    margin: 0.5rem 0;
  }
  
  .course-card .status {
    font-weight: bold;
    color: #4caf50; /* Kolor statusu, można dostosować */
  }
  
  .course-card button {
    background-color: #007BFF;
    color: white;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    cursor: pointer;
    font-size: 1rem;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  .course-card button:hover {
    background-color: #0056b3;
    transform: translateY(-2px);
  }
  
  .course-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.3);
  }
  
  </style>
