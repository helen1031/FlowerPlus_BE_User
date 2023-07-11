package ggamang.flowerplus.subscribers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeId implements Serializable {

    private String userId;

    private String subscriberId;
}
