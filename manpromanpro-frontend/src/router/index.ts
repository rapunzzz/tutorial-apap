import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProyekView from '../views/ProyekView.vue'
import CreateProyekView from '@/views/CreateProyekView.vue'
import UpdateProyekView from '@/views/UpdateProyekView.vue'
import DetailProyekView from '@/views/DetailProyekView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/proyek',
      name: 'proyek',
      component: ProyekView,
    },
    {
      path: '/proyek/add',
      name: 'tambah proyek',
      component: CreateProyekView
    },
    {
      path: '/proyek/:id',
      name: 'detail proyek',
      component: DetailProyekView
    },
    {
      path: '/proyek/:id/edit',
      name: 'ubah proyek',
      component: UpdateProyekView
    },
  ],
})

export default router
