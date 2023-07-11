package ggamang.flowerplus.subscribers.dto;

import ggamang.flowerplus.subscribers.entity.SubscribeId;
import ggamang.flowerplus.subscribers.entity.SubscriberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberDTO {
    private Long subscriberId;
    private Long userId;
    private Date createdDate;

    public static SubscriberDTO fromEntity(SubscriberEntity subscriberEntity) {
        return new SubscriberDTO(
                subscriberEntity.getId().getUserId(),
                subscriberEntity.getId().getSubscriberId(),
                subscriberEntity.getCreatedDate()
        );
    }

    public static SubscriberEntity toEntity(SubscriberDTO subscriberDTO) {
        SubscribeId subscribeId = new SubscribeId(
                subscriberDTO.getUserId(),
                subscriberDTO.getSubscriberId()
        );

        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setId(subscribeId);
        subscriberEntity.setCreatedDate(subscriberDTO.getCreatedDate());

        return subscriberEntity;
    }
}
