<script setup lang="ts">
import { useProjectStore } from '@/stores/project'
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import type { ProjectInterface } from '@/interfaces/project.interface'
import { format } from 'date-fns'
import VButton from '@/components/VButton.vue'

const router = useRouter()

const route = useRoute()
const {id: projectId} = route.params

const projectStore = useProjectStore()

const project = ref(undefined as undefined | ProjectInterface)

const getProject = async () => {
    project.value = await projectStore.getProjectDetail(projectId as string)
}

onMounted(getProject)
</script>

<template>
    <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
      <div class="w-[60%] flex flex-col gap-2 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">
        <div class="w-full flex justify-between">
          <h1 class="text-green-600 font-bold text-xl">Detail Proyek</h1>
        </div>
        
        <div class="flex flex-col gap-2 py-4">
          <div class="flex flex-col gap-1 w-full">
            <span>Nama Proyek</span>
            <span class="text-xl font-bold">{{ project?.nama }}</span>
          </div>
          <div class="flex flex-col gap-1 w-full">
            <span>Deskripsi</span>
            <span class="text-xl font-bold">{{ project?.deskripsi }}</span>
          </div>
          <div class="flex gap-12 w-full">
            <div class="flex flex-col gap-1 w-1/3">
              <span>Tanggal Mulai</span>
              <span class="text-xl font-bold">
                {{ project ? format(new Date(project.tanggalMulai), 'EEEE, dd MMMM yyyy') : "-" }}
              </span>
            </div>
            <div class="flex flex-col gap-1 w-1/3">
              <span>Tanggal Selesai</span>
              <span class="text-xl font-bold">
                {{ project ? format(new Date(project.tanggalSelesai), 'EEEE, dd MMMM yyyy') : "-" }}
              </span>
            </div>
          </div>
          <div class="flex gap-12 w-full">
            <div class="flex flex-col gap-1 w-1/3">
              <span>Status</span>
              <span class="text-xl font-bold">{{ project?.status }}</span>
            </div>
            <div class="flex flex-col gap-1 w-1/3">
              <span>Developer</span>
              <span class="text-xl font-bold">{{ project?.developer.nama }}</span>
            </div>
          </div>
        </div>
        <div class="flex gap-2 py-2">
            <VButton @click="router.back()" class="bg-slate-600 hover:bg-slate-800 text-white">Kembali</VButton>
            <RouterLink :to="`/proyek/${projectId}/edit`" class="w-full">
                <VButton class="bg-amber-600 hover:bg-amber-800 text-white">Edit</VButton>
            </RouterLink>
        </div>
      </div>
    </main>
</template> 