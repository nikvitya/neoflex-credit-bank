package ru.neoflex.deal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.deal.model.jsonb.PassportData;

import java.util.Objects;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "passport_id")
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    private PassportData passportData;

    public Passport(PassportData passportData) {
        this.passportData = passportData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passport passport)) return false;
        return getId().equals(passport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
