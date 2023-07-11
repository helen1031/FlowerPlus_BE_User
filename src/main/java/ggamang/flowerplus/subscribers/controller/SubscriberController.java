package ggamang.flowerplus.subscribers.controller;

import ggamang.flowerplus.subscribers.dto.SubscriberDTO;
import ggamang.flowerplus.subscribers.entity.SubscribeId;
import ggamang.flowerplus.subscribers.entity.SubscriberEntity;
import ggamang.flowerplus.subscribers.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    String userId = "temp";

    // 구독 등록
    @PostMapping
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) {
        try {
            SubscriberEntity subscriberEntity = SubscriberDTO.toEntity(subscriberDTO);
            subscriberService.createSubscriber(subscriberEntity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 구독 삭제
    @DeleteMapping("/{subscriberId}")
    public ResponseEntity<?> deleteSubscriber(@PathVariable String subscriberId) {
        try {
            subscriberService.deleteSubscriber(new SubscribeId(userId, subscriberId));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 내 구독자 목록 조회하기
    @GetMapping("/my-subscribers")
    public ResponseEntity<List<SubscriberDTO>> getMySubscribers() {
        List<SubscriberEntity> mySubscriberEntities = subscriberService.getSubscribers(userId);
        List<SubscriberDTO> mySubscribers = mySubscriberEntities.stream()
                .map(SubscriberDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mySubscribers);
    }

    // (관리자용) 유저별 구독자 목록 조회하기
    @GetMapping("/user-subscribers")
    public ResponseEntity<List<SubscriberDTO>> getUserSubscribers(@RequestParam("userId") String userId) {
        List<SubscriberEntity> userSubscriberEntities = subscriberService.getSubscribers(userId);
        List<SubscriberDTO> userSubscribers = userSubscriberEntities.stream()
                .map(SubscriberDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userSubscribers);
    }
}
