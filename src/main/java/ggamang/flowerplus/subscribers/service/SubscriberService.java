package ggamang.flowerplus.subscribers.service;

import ggamang.flowerplus.subscribers.entity.SubscribeId;
import ggamang.flowerplus.subscribers.entity.SubscriberEntity;
import ggamang.flowerplus.subscribers.repository.SubscriberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    public List<SubscriberEntity> getSubscribers(final Long userId) {
        return subscriberRepository.findAllByIdUserId(userId);
    }

    public List<Long> getSubscribersIds(final Long userId){
        List<SubscriberEntity> subscribers = subscriberRepository.findAllByIdUserId(userId);
        List<Long> subscriberIds = subscribers.stream()
                .map(subscriber -> subscriber.getId().getSubscriberId())
                .collect(Collectors.toList());
        return subscriberIds;
    }

    public void createSubscriber(final SubscriberEntity subscriberEntity) {
        subscriberRepository.save(subscriberEntity);
        log.info("{} subscribed {}.", subscriberEntity.getId().getUserId(), subscriberEntity.getId().getSubscriberId());
    }

    public void deleteSubscriber(final SubscribeId subscribeId) {
        subscriberRepository.deleteById(subscribeId);
        log.info("{} un-subscribed {}.", subscribeId.getUserId(), subscribeId.getSubscriberId());
    }

    public Optional<SubscriberEntity> findSubscriber(SubscribeId subscribeId) {
        return subscriberRepository.findById(subscribeId);
    }



}
