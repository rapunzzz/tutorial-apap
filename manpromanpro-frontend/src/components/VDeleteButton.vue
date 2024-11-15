<script setup lang="ts">
 
import VButton from '@/components/VButton.vue'
import { useProjectStore } from '@/stores/project';
 
const projectStore = useProjectStore();
 
const{ projectId } = defineProps({
    projectId: {
        type: String,
        required: true
    }
})
 
 
const deleteProject = async () => {
    console.log("Clicked")
    await projectStore.deleteProject(projectId)
}

</script>
 
<template>
    <VButton @click="deleteProject" class='delete-button'>Hapus</VButton>
</template>
 
<style scoped>
    .delete-button {
        @apply bg-rose-600 hover:bg-rose-800 text-white
    }
</style>