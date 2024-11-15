<script setup lang="ts">
import VInput from '@/components/VInput.vue'
import VSelect from '@/components//VSelect.vue'
import VTextArea from '@/components/VTextArea.vue';
import VButton from '@/components/VButton.vue';
import type{ ProjectRequestInterface } from '@/interfaces/project.interface';
import { onMounted, type PropType, ref, toRefs, watch } from 'vue';
import type { CommonResponseInterface } from '@/interfaces/common.interface';
import type { DeveloperOptionInterface } from '@/interfaces/developer.interface';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
    action: {
        type: Function as PropType<(data: ProjectRequestInterface) => Promise<void>>,
        required: true
    },
    projectModel: {
        type: Object as PropType<ProjectRequestInterface>,
        required: true
    }
})

const model = toRefs(props).projectModel;

const emit = defineEmits(['update:modelValue']);

watch(() => model, (newValue) => {
    emit('update:modelValue', newValue);
}, {deep: true }
)

const handleSubmit = async () => await props.action(model.value)

const developerOptions = ref([] as DeveloperOptionInterface[])

const getDevelopers = async () => {
    const response = await fetch('http://localhost:8080/api/developer/viewall')

    const data: CommonResponseInterface<DeveloperOptionInterface[]> = await response.json()
    developerOptions.value = data.data
}

onMounted(getDevelopers)
</script>

<template>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-2 py-2">
        <VInput v-model="model.nama" id="name" name="name" label="Nama Proyek" />
        <VTextArea v-model="model.deskripsi" id="description" name="description" label="Deskripsi" />
        <div class="flex w-full justify-between gap-2">
            <VInput id="start_date" name="startDate" type="date" v-model="model.tanggalMulai" label="Tanggal Mulai" />
            <VInput id="end_date" name="endDate" type="date" v-model="model.tanggalSelesai" label="Tanggal Selesai" />
        </div>
        <div class="flex w-full justify-between gap-2">
            <VSelect id="status" name="status" label="Status" v-model="model.status">
                <Option value="">Pilih Status...</Option>
                <Option value="STARTED">Started</Option>
                <Option value="ONGOING">Ongoing</Option>
                <Option value="FINISHED">Finished</Option>
            </VSelect>
            <VSelect id="developer" name="developer" label="Developer" v-model="model.developer">
                <option value="">Pilih Developer...</option>
                <option v-for="developer in developerOptions" :key="developer.id" :value="developer.id">{{ developer.nama }}</option>
            </VSelect>
        </div>
        <div class="flex gap-2 py-2">
            <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Kembali</VButton>
            <VButton type="submit" class="bg-green-600 hover:bg-green-800 text-white">Simpan</VButton>
        </div>
    </form>
</template>

<style scoped>

</style>