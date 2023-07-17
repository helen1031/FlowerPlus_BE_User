package ggamang.flowerplus.subscribers.controller;

import ggamang.flowerplus.subscribers.dto.SubscriberDTO;
import ggamang.flowerplus.subscribers.entity.SubscribeId;
import ggamang.flowerplus.subscribers.entity.SubscriberEntity;
import ggamang.flowerplus.subscribers.service.SubscriberService;
import ggamang.flowerplus.users.entity.UserEntity;
import ggamang.flowerplus.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private UserService userService;

    // 구독 등록
    @PostMapping
    public ResponseEntity<?> createSubscriber(@AuthenticationPrincipal Long userId,
                                              @RequestBody SubscriberDTO subscriberDTO) {
        try {
            UserEntity userEntity = userService.findUserById(subscriberDTO.getUserId()); // fetch user from db
            UserEntity subscriberEntityUser = userService.findUserById(subscriberDTO.getSubscriberId());

            if (userEntity == null) {
                return ResponseEntity.badRequest().body("User Not Found");
            }

            if (subscriberEntityUser == null) {
                return ResponseEntity.badRequest().body("Subscriber Not Found");
            }

            SubscriberEntity subscriberEntity = SubscriberDTO.toEntity(subscriberDTO);
            subscriberEntity.setUser(userEntity);
            subscriberService.createSubscriber(subscriberEntity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 구독 삭제
    @DeleteMapping("/{subscriberId}")
    public ResponseEntity<?> deleteSubscriber(@AuthenticationPrincipal Long userId,
                                              @PathVariable Long subscriberId) {
        try {
            subscriberService.deleteSubscriber(new SubscribeId(userId, subscriberId));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 특정 아이디의 구독 여부 확인
    @GetMapping("/{subscriberId}")
    public ResponseEntity<?> checkSubscriptionStatus(@AuthenticationPrincipal Long userId,
                                                     @PathVariable Long subscriberId) {
        Optional<SubscriberEntity> subscriberEntityOptional
                = subscriberService.findSubscriber(new SubscribeId(userId, subscriberId));

        if (subscriberEntityOptional.isPresent()) {
            // If the entity exists, this means that the user is subscribed to the subscriber
            return ResponseEntity.ok(true);
        } else {
            // If the entity does not exist, this means that the user is not subscribed to the subscriber
            return ResponseEntity.ok(false);
        }
    }

    // 내 구독자 목록 조회하기
    @GetMapping("/my-subscribers")
    public ResponseEntity<List<SubscriberDTO>> getMySubscribers(@AuthenticationPrincipal Long userId) {
        List<SubscriberEntity> mySubscriberEntities = subscriberService.getSubscribers(userId);
        List<SubscriberDTO> mySubscribers = mySubscriberEntities.stream()
                .map(SubscriberDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mySubscribers);
    }

    // (관리자용) 유저별 구독자 목록 조회하기
    @GetMapping("/user-subscribers")
    public ResponseEntity<List<SubscriberDTO>> getUserSubscribers(@AuthenticationPrincipal Long serId,
                                                                  @RequestParam("userId") Long searchUserId) {
        List<SubscriberEntity> userSubscriberEntities = subscriberService.getSubscribers(searchUserId);
        List<SubscriberDTO> userSubscribers = userSubscriberEntities.stream()
                .map(SubscriberDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userSubscribers);
    }
}
