import { defineStore } from "pinia"
import type { ProjectInterface } from "@/interfaces/project.interface"
import type { ProjectRequestInterface } from '@/interfaces/project.interface'
import type { CommonResponseInterface } from "@/interfaces/common.interface";
import { th } from "date-fns/locale";
import { useToast } from "vue-toastification";
import router from "@/router";

export const useProjectStore = defineStore('project', {
  state: () => ({
    projects: [] as ProjectInterface[],
    loading: false,
    error: null as null | string,
  }),
  actions: {
    async getProjects() {
      this.loading = true
      this.error = null

      try {
        const response = await fetch('http://localhost:8080/api/proyek/viewall')

        const data: CommonResponseInterface<ProjectInterface[]> = await response.json()

        this.projects = data.data
      } catch (err) {
        this.error = 'Gagal mengambil proyek ${err}'
      } finally {
        this.loading = false
      }
    },
    async addProject(body: ProjectRequestInterface) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`http://localhost:8080/api/proyek/add`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body),
        })
        const data: CommonResponseInterface<ProjectInterface> = await response.json()

        console.log('BODY: ', JSON.stringify(body))
        console.log('RESPONSE: ', response)
        console.log('DATA: ', data)

        this.projects.push(data.data)

        useToast().success('Sukses menambahkan proyek')
        await router.push('/proyek')
      } catch (err) {
        this.error = 'Gagal menambah proyek ${(err as Error).message}'

        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

    async updateProject(id: string, body: ProjectRequestInterface) {
      this.loading = true
      this.error = null

      console.log('ID: ', id)
      console.log('BODY: ', body)

      try {
        const response = await fetch(`http://localhost:8080/api/proyek/${id}/update`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ id, ...body }),
        })
        console.log('body: ', JSON.stringify({ id, ...body }))
        console.log('RESPONSE: ', response)
        const data: CommonResponseInterface<ProjectInterface> = await response.json()
        console.log('DATA: ', data)
        this.projects.push(data.data)

        useToast().success('Sukses mengubah proyek')
        await router.push('/proyek')
      } catch (err) {
        this.error = `Gagal mengubah proyek ${(err as Error).message}`

        useToast().error(this.error)
      } finally {
        this.loading = false
      }
    },

    async getProjectDetail(id: string) {
        this.loading = true
        this.error = null

        try {
            const response = await fetch(`http://localhost:8080/api/proyek/${id}`)
            const data: CommonResponseInterface<ProjectInterface> = await response.json()
            return data.data
        } catch (err) {
            this.error = `Gagal mengambil proyek ${(err as Error).message}`
        } finally {
            this.loading = false
        }
    },

    async deleteProject(id: string) {
        this.loading = true
        this.error = null
        
        try {
            const response = await fetch(`http://localhost:8080/api/proyek/${id}/delete`, 
                    {
                    method: 'DELETE',
                    headers: {'Content-Type': 'application/json'},
                },
            )

            if (response.ok) {
                this.projects = this.projects.filter(
                    (project) => project.id != id
                )
            }

            useToast().success("Sukses menghapus proyek")
            window.location.reload()
        } catch (err) {
            this.error = 'Gagal menghapus proyek ${(err as Error).message}'

            useToast().error(this.error)
        } finally {
            this.loading = false
        }
    }
  },
})