package aze.coders.basic_authentication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private String username;
    private Boolean valid;
    private Date issueDate;
    private Date expiryDate;

    public RefreshToken(String token, String username, Boolean valid, Date issueDate, Date expiryDate) {
        this.token = token;
        this.username = username;
        this.valid = valid;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }
}
