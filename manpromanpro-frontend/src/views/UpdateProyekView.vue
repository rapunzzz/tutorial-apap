<script setup lang="ts">
import { onMounted,reactive } from 'vue'
import { useProjectStore } from '@/stores/project'
import VProyekForm from '@/components/VProyekForm.vue'
import type { ProjectRequestInterface } from '@/interfaces/project.interface'
import { useRoute } from 'vue-router'

const route = useRoute()
const {id: projectId} = route.params

const projectStore = useProjectStore()

const projectModel = reactive({
    nama: "",
    deskripsi: "",
    tanggalMulai: "",
    tanggalSelesai: "",
    status: "",
    developer: "",
})

const updateProject = async (bodyRequst: ProjectRequestInterface) => await projectStore.updateProject(projectId as string, bodyRequst)
const getProject = async () => {
    if (typeof projectId == 'string') {
        const project = await projectStore.getProjectDetail(projectId)

        if (project) {
            projectModel.nama = project.nama
            projectModel.deskripsi = project.deskripsi
            projectModel.status = project.status
            projectModel.tanggalMulai = project.tanggalMulai
            projectModel.tanggalSelesai = project.tanggalSelesai
            projectModel.developer = project.developer.id.toString()
        }
    }
}

onMounted(getProject)
</script>

<template>
    <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
        <div class="w-[60%] flex flex-col gap-2 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">
            <div class="w-full flex justify-between">
                <h1 class="text-green-600 font-bold text-xl">Ubah Proyek</h1>
            </div>
            <VProyekForm :projectModel="projectModel" :action="updateProject"/>
        </div>
    </main>
</template>