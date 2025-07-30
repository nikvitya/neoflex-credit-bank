package ru.neoflex.deal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.deal.model.jsonb.EmploymentData;

import java.util.Objects;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employment")
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employment_id")
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentData employmentData;

    public Employment(EmploymentData employmentData) {
        this.employmentData = employmentData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employment that)) return false;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
