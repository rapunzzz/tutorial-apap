package apap.tutorial.manpromanpro.model;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "developer")
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "alamat", columnDefinition = "TEXT", nullable = false)
    private String alamat;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Column(name = "tanggal_berdiri", columnDefinition = "DATE", nullable = false)
    private Date tanggalBerdiri;

    @Size(max = 30)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

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

    @OneToMany(mappedBy = "developer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Proyek> listProyek;
}

