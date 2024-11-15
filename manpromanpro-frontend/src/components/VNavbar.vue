<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router'

const route = useRoute()

const getLinkClass = (path: string) => route.path === path ? 'text-green-600' : 'text-black hover:text-green-600'
</script>

<template>
    <header class="fixed flex items-center w-full gap-4 px-3 py-4 bg-white">
        <RouterLink to="/" class="text-xl font-bold text-green-600">ManproManpro</RouterLink>
        <nav class="flex gap-6">
            <RouterLink to="/proyek" :class="getLinkClass('/proyek')">Proyek</RouterLink>
        </nav>
    </header>
</template>