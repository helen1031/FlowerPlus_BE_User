package ggamang.flowerplus.subscribers.repository;

import ggamang.flowerplus.subscribers.entity.SubscribeId;
import ggamang.flowerplus.subscribers.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<SubscriberEntity, SubscribeId> {
    List<SubscriberEntity> findAllByIdUserId(String userId);
}
