import type { PekerjaInterface } from '@/interfaces/pekerja.interface'
import type { DeveloperInterface } from '@/interfaces/developer.interface'

export interface ProjectInterface {
    id: string,
    nama: string,
    deskripsi: string,
    status: string,
    tanggalMulai: string,
    tanggalSelesai: string,
    createdAt: Date,
    updatedAt: Date,
    developer: DeveloperInterface,
    listPekerja: PekerjaInterface[]
}

export interface ProjectRequestInterface {
    nama: string,
    deskripsi: string,
    status: string,
    tanggalMulai: string,
    tanggalSelesai: string,
    developer: string,
}