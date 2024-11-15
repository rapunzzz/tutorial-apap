<script setup lang="ts">
import { reactive } from 'vue'
import { useProjectStore } from '@/stores/project'
import VProyekForm from '@/components/VProyekForm.vue'
import type { ProjectRequestInterface } from '@/interfaces/project.interface'

const projectStore = useProjectStore()

const projectModel = reactive({
    nama: "",
    deskripsi: "",
    tanggalMulai: "",
    tanggalSelesai: "",
    status: "",
    developer: "",
})

const addProject = async (bodyRequst: ProjectRequestInterface) => await projectStore.addProject(bodyRequst)
</script>

<template>
    <main class="w-full h-screen flex justify-center items-center bg-gray-400/30">
        <div class="w-[60%] flex flex-col gap-2 divide-y-2 bg-white drop-shadow-xl p-6 rounded-xl">
            <div class="w-full flex justify-between">
                <h1 class="text-green-600 font-bold text-xl">Tambah Proyek</h1>
            </div>
            <VProyekForm :projectModel="projectModel" :action="addProject"/>
        </div>
    </main>
</template>