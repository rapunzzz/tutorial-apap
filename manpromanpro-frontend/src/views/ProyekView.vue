<script setup lang="ts">
import { onMounted } from 'vue'
import { DataTable } from 'simple-datatables'
import VButton from '@/components/VButton.vue'
import { useProjectStore } from '@/stores/project'
import VDeleteButton from '@/components/VDeleteButton.vue'
const projectStore = useProjectStore()

onMounted( async () =>{
    await projectStore.getProjects()
    
    if (
        document.getElementById('default-table') && typeof DataTable != 'undefined'
    ) {
        new DataTable('#default-table', {
            searchable: false,
        })
    }
})
</script>

<template>
    <main class="flex items-center justify-center w-full h-full">
        <div v-if="projectStore.loading" class="message-layer">
            <span class="animate-pulse font-bold text-4xl">Fetching data...</span>
        </div>
        <div class="px-12 py-20 w-full" v-else>
            <div v-if="!projectStore.error" class="flex flex-col gap-6">
                <RouterLink to="/proyek/add">
                    <VButton class="add-button">Buat Proyek Baru</VButton>
                </RouterLink>
                <table id="default-table">
                    <thead>
                    <tr>
                        <th>
                            <span class="flex items-center"> Nama </span>
                        </th>
                        <th data-type="date" data-format="YYYY/DD/MM">
                            <span class="flex items-center"> Developer </span> 
                        </th>
                        <th>
                            <span class="flex items-center"> Status </span>
                        </th>
                        <th>
                            <span class="flex items-center"> Aksi </span>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="project in projectStore.projects" :key="project.id">
                        <td class="font-medium text-gray-900 whitespace-nowrap dark:text-white">
                            {{ project.nama }}
                        </td>
                        <td>{{ project.developer.nama }}</td>
                        <td>{{ project.status }}</td>
                        <td class="flex gap-1">
                            <RouterLink :to="`/proyek/${project.id}`" class="w-full">
                                <VButton class="detail-button">Lihat</VButton>
                            </RouterLink>
                            <RouterLink :to="`/proyek/${project.id}/edit`" class="w-full">
                                <VButton class="edit-button">Edit</VButton>
                            </RouterLink>
                            <VDeleteButton :projectId="project.id" />
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div v-else class="message-layer">
                <span class="text-xl">{{ projectStore.error }}</span>
            </div>
        </div>
    </main>
</template>

<style scoped>
    .message-layer{
        @apply w-full h-screen flex items-center justify-center
    }

    .add-button{
        @apply bg-green-600 hover:bg-green-800 text-white
    }

    .detail-button{
        @apply bg-cyan-600 hover:bg-cyan-800 text-white
    }

    .edit-button{
        @apply bg-amber-600 hover:bg-amber-800 text-white
    }
</style>