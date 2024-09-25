package apap.tutorial.manpromanpro.model;

import java.util.List;
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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pekerja")
public class Pekerja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "usia", nullable = false)
    private int usia;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Size(max = 30)
    @Column(name = "pekerjaan", nullable = false)
    private String pekerjaan;

    @NotNull
    @Column(name = "biografi", columnDefinition = "TEXT", nullable = false)
    private String biografi;

    @ManyToMany
    private List<Proyek> listProyek;
}

