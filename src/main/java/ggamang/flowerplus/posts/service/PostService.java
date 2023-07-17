package ggamang.flowerplus.posts.service;

import ggamang.flowerplus.posts.PostRange;
import ggamang.flowerplus.posts.dto.PostImageDTO;
import ggamang.flowerplus.posts.entity.PostDetailEntity;
import ggamang.flowerplus.posts.entity.PostEntity;
import ggamang.flowerplus.posts.entity.PostImageEntity;
import ggamang.flowerplus.posts.repository.PostRepository;
import ggamang.flowerplus.subscribers.entity.SubscribeId;
import ggamang.flowerplus.subscribers.repository.SubscriberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    // 게시물 등록
    public PostEntity createPost(final PostEntity postEntity) {
        postRepository.save(postEntity);
        log.info("Post Entity Id : {} is saved", postEntity.getPostId());

        return postRepository.findByPostId(postEntity.getPostId());
    }

    // 게시물 삭제
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
        log.info("Post Entity Id : {} is deleted", postId);
    }

    public PostEntity updatePost(final PostEntity updatedPostEntity) {
        // Fetch the post to be updated
        Long postId = updatedPostEntity.getPostId();
        PostEntity previousPostEntity = postRepository.findByPostId(postId);
        if (previousPostEntity == null) {
            throw new IllegalArgumentException("Post with id: " + postId + " does not exist");
        }

        previousPostEntity.setPostRange(updatedPostEntity.getPostRange());
        previousPostEntity.setForExchange(updatedPostEntity.isForExchange());
        previousPostEntity.setForSale(updatedPostEntity.isForSale());
        previousPostEntity.setFlowerName(updatedPostEntity.getFlowerName());
        previousPostEntity.setContent(updatedPostEntity.getContent());
        previousPostEntity.setFlowerType(updatedPostEntity.getFlowerType());
        previousPostEntity.setUpdateDate(new Date());

        postRepository.save(previousPostEntity);

        log.info("Post Entity Id : {} is updated", postId);

        return postRepository.findByPostId(postId);
    }


    // 게시물 조회_0. 특정 게시물
    public PostEntity getPostById(final Long postId) {
        return postRepository.findByPostId(postId);
    }

    // 게시물 조회_1. 자기 게시물
    public List<PostEntity> getPostsByUserId(final Long userId){
        return postRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }

    // 게시물 조회_2. 구독자 게시물
    public List<PostEntity> getSubscriberPosts(final List<Long> subscriberIds) {
        List<PostRange> privateRange = Collections.singletonList(PostRange.PRIVATE);
        return postRepository.findByUserIdInAndPostRangeNotInOrderByCreatedDateDesc(subscriberIds,privateRange);
    }

    // 게시물 조회_3. 전체 게시물 조회
    public List<PostEntity> getPublicPosts() {
        List<PostRange> publicRange = Collections.singletonList(PostRange.PUBLIC);
        return postRepository.findAllByPostRangeInOrderByCreatedDateDesc(publicRange);
    }

    // 게시물 조회_4. 특정 유저 아이디의 게시물 조회(구독자라면 구독자용 게시물도 추가)
    public List<PostEntity> getOthersPostsByUserId(final Long loggedInUserId, final Long otherUserId) {
        List<PostRange> postRanges = new ArrayList<>();
        postRanges.add(PostRange.PUBLIC);

        boolean isSubscribed = subscriberRepository.existsById(new SubscribeId(loggedInUserId, otherUserId));

        if (isSubscribed) {
            postRanges.add(PostRange.SUBSCRIBERS);
        }

        return postRepository.findAllByUserIdAndPostRangeInOrderByCreatedDateDesc(otherUserId, postRanges);
    }


    // 게시물 조회_5. (관리자용) 전체 게시물 조회
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

}
