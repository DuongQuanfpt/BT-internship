package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChangedPasswordToken_tbl")
@Data
public class ChangedPasswordToken {
    @Id
    private String token;

    @Column(name = "expire_date", nullable = false)
    private LocalDate expireDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User owner;
}
