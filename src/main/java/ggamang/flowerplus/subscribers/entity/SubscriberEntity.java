package ggamang.flowerplus.subscribers.entity;

import ggamang.flowerplus.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SUBSCRIBERS")
public class SubscriberEntity {

    @EmbeddedId
    private SubscribeId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private UserEntity user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}
