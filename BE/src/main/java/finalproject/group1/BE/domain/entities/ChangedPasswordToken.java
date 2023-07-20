package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime expireDate;

    @OneToOne
    @JoinColumn(name="user_id", nullable=false)
    private User owner;
}
