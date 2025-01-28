import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import CourseDetail from '@/components/Student/CourseDetail.vue';
import CourseList from '@/components/Student/CourseList.vue';
import RegisterView from '../views/RegisterView.vue';
import UserProfileView from '../views/UserProfileView.vue';
import UserManagement from '@/components/Admin/UserManagement.vue';
import TeacherPanel from '@/views/TeacherPanel.vue';
import EditCourse from '@/components/Instructor/EditCourse.vue';

// Zabezpieczenie przed dostępem do stron bez zalogowania
function requireAuth(to, from, next) {
  const isLoggedIn = !!localStorage.getItem('authToken');
  if (isLoggedIn) {
    next();
  } else {
    next('/login');
  }
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/register', name: 'register', component: RegisterView },
    { path: '/teacher-panel', name: 'teacherpanel', component: TeacherPanel, beforeEnter: requireAuth },
    { path: '/profile', name: 'profile', component: UserProfileView, beforeEnter: requireAuth },
    { path: '/courses', name: 'courses', component: CourseList },
    { path: '/course/:id', name: 'course', component: CourseDetail, props: true },
    { path: '/editcourse/:courseId', name: 'editcourse', component: EditCourse, props:true},
    { path: '/admin', component: UserManagement, beforeEnter: requireAuth },
  ],
});

export default router;
