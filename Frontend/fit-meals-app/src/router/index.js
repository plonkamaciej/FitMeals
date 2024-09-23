import { createRouter, createWebHistory } from 'vue-router';
import Diary from '@/components/Diary.vue';
import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { 
    path: '/diary', 
    component: Diary, 
    meta: { requiresAuth: true }  // Trasa chroniona
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, from, next) => {
//   const isAuthenticated = !!localStorage.getItem('authToken');
  
//   if (to.meta.requiresAuth && !isAuthenticated) {
//     next('/login');
//   } else {
//     next();
//   }
// });

export default router;
