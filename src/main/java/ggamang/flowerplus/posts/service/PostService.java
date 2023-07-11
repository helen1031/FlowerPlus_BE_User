package ggamang.flowerplus.posts.service;

import ggamang.flowerplus.posts.PostRange;
import ggamang.flowerplus.posts.dto.PostImageDTO;
import ggamang.flowerplus.posts.entity.PostDetailEntity;
import ggamang.flowerplus.posts.entity.PostEntity;
import ggamang.flowerplus.posts.entity.PostImageEntity;
import ggamang.flowerplus.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시물 등록
    public PostEntity createPost(final PostEntity postEntity,
                                 final PostDetailEntity postDetailEntity,
                                 final List<PostImageDTO> postImages) {
        postEntity.setPostDetail(postDetailEntity);
        postDetailEntity.setPost(postEntity);

        if (postImages != null) {
            List<PostImageEntity> postImageEntities = postImages.stream()
                    .map(PostImageDTO::toEntity)
                    .collect(Collectors.toList());
            postImageEntities.forEach(postImageEntity -> postImageEntity.setPost(postEntity));
            postEntity.setImages(postImageEntities);
        }

        postRepository.save(postEntity);
        log.info("Post Entity Id : {} is saved", postEntity.getPostId());

        return postRepository.findByPostId(postEntity.getPostId());
    }

    // 게시물 삭제
    public void deletePost(String postId) {
        postRepository.deleteById(postId);
        log.info("Post Entity Id : {} is deleted", postId);
    }

    // 게시물 수정
    public PostEntity updatePost(String postId,
                                 final PostEntity updatedPostEntity,
                                 final PostDetailEntity updatedPostDetailEntity,
                                 final List<PostImageDTO> updatedPostImages) {
        PostEntity previousPostEntity = postRepository.findByPostId(postId);
        previousPostEntity.setPostRange(updatedPostEntity.getPostRange());
        previousPostEntity.setForExchange(updatedPostEntity.isForExchange());
        previousPostEntity.setForSale(updatedPostEntity.isForSale());
        previousPostEntity.setFlowerName(updatedPostEntity.getFlowerName());
        previousPostEntity.setContent(updatedPostEntity.getContent());
        previousPostEntity.setFlowerType(updatedPostEntity.getFlowerType());
        previousPostEntity.setUpdateDate(new Date());

        PostDetailEntity postDetailEntity = previousPostEntity.getPostDetail();
        if (postDetailEntity != null) {
            postDetailEntity.setHeight(updatedPostDetailEntity.getHeight());
            postDetailEntity.setFeature(updatedPostDetailEntity.getFeature());
            postDetailEntity.setQuantity(updatedPostDetailEntity.getQuantity());
        } else {
            postDetailEntity = updatedPostDetailEntity;
            postDetailEntity.setPost(previousPostEntity);
        }
        previousPostEntity.setPostDetail(postDetailEntity);

        if (updatedPostImages != null) {
            List<PostImageEntity> updatedPostImageEntities = updatedPostImages.stream()
                    .map(PostImageDTO::toEntity)
                    .collect(Collectors.toList());
            updatedPostImageEntities.forEach(postImageEntity -> postImageEntity.setPost(previousPostEntity));
            previousPostEntity.setImages(updatedPostImageEntities);
        }

        postRepository.save(previousPostEntity);

        log.info("Post Entity Id : {} is updated", postId);

        return postRepository.findByPostId(postId);
    }

    // 게시물 조회_0. 특정 게시물
    public PostEntity getPostById(final String postId) {
        return postRepository.findByPostId(postId);
    }

    // 게시물 조회_1. 자기 게시물
    public List<PostEntity> getPostsByUserId(final String userId){
        return postRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }

    // 게시물 조회_2. 구독자 게시물
    public List<PostEntity> getSubscriberPosts(final List<String> subscriberIds) {
        return postRepository.findByUserIdInOrderByCreatedDateDesc(subscriberIds);
    }

    // 게시물 조회_3. 공개 범위 전체인 전체 게시물 조회
    public List<PostEntity> getPublicPosts() {
        List<PostRange> publicRange = Collections.singletonList(PostRange.PUBLIC);
        return postRepository.findAllByPostRangeInOrderByCreatedDateDesc(publicRange);
    }

    // 게시물 조회_4. (관리자용) 전체 게시물 조회
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

}
