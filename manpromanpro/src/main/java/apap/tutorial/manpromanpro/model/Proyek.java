package apap.tutorial.manpromanpro.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proyek")
public class Proyek {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Size(max = 30)
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "deskripsi", columnDefinition = "TEXT", nullable = false)
    private String deskripsi;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Column(name = "tanggal_mulai", columnDefinition = "DATE", nullable = false)
    private Date tanggalMulai;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Column(name = "tanggal_selesai", columnDefinition = "DATE", nullable = false)
    private Date tanggalSelesai;

    @NotNull
    @Size(max = 30)
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_developer", referencedColumnName = "id")
    private Developer developer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    @NotNull
    @Column(name = "tanggal_dibentuk", nullable = false)
    private Date tanggalDibentuk;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @UpdateTimestamp
    @NotNull
    @Column(name = "tanggal_diubah", nullable = false)
    private Date tanggalDiubah;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal_dihapus", nullable = true)
    private Date tanggalDihapus;

    @ManyToMany
    @JoinTable(
        name = "pekerja_proyek",
        joinColumns = @JoinColumn(name = "id_proyek"),
        inverseJoinColumns = @JoinColumn(name = "id_pekerja")
    )
    private List<Pekerja> listPekerja;
}

