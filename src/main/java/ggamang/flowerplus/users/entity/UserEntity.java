package ggamang.flowerplus.users.entity;

import ggamang.flowerplus.subscribers.entity.SubscriberEntity;
import ggamang.flowerplus.users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String userId;
    private String email;
    private String password;
    private String username;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    private String authProvider;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SubscriberEntity> subscriptions = new HashSet<>();
}
