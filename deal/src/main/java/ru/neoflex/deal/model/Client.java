package ru.neoflex.deal.model;

import jakarta.persistence.*;
import lombok.*;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_id")
    private UUID id;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_id")
    private Employment employment;

    @Column(name = "account_number")
    private String account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return getId().equals(client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
