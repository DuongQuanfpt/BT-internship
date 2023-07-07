package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private Date expireDate;

//    @Column(name = "user_id", nullable = false)
//    private int userId;
}
