package ggamang.flowerplus.posts.controller;

import ggamang.flowerplus.common.dto.ResponseDTO;
import ggamang.flowerplus.files.FileService;
import ggamang.flowerplus.posts.dto.PostDTO;
import ggamang.flowerplus.posts.dto.PostDetailDTO;
import ggamang.flowerplus.posts.dto.PostImageDTO;
import ggamang.flowerplus.posts.entity.PostDetailEntity;
import ggamang.flowerplus.posts.entity.PostEntity;
import ggamang.flowerplus.posts.service.PostService;
import ggamang.flowerplus.subscribers.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private FileService fileService;


    // 게시물 등록
    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal Long userId,
                                        @RequestBody PostDTO newPostDTO){
        try {
            PostEntity postEntity = PostDTO.toEntity(newPostDTO);
            PostDetailEntity postDetailEntity = PostDetailDTO.toEntity(newPostDTO.getPostDetail());

            // userId 설정
            postEntity.setUserId(userId);

            List<PostImageDTO> uploadedImages = new ArrayList<>();

            // 이미지 업로드
            for (PostImageDTO imageDTO : newPostDTO.getImages()) {
                // base64 string -> byte[] 변환
                byte[] byteImage = fileService.convertBase64ToImage(imageDTO.getImage());
                String imageUrl = fileService.uploadFile(byteImage, imageDTO.getPostId() +
                        "_" + imageDTO.getImageId());
                imageDTO.setImageUrl(imageUrl);
                uploadedImages.add(imageDTO);
            }

            // createdTime 설정
            postEntity.setCreatedDate(new Date());

            PostEntity savedPost = postService.createPost(postEntity, postDetailEntity, uploadedImages);
            PostDTO savedPostDTO = PostDTO.fromEntity(savedPost);
            return ResponseEntity.ok().body(savedPostDTO);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 게시물 삭제
    @DeleteMapping("/{postID}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal Long userId,
                                        @PathVariable Long postId){
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 게시물 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal Long userId,
                                        @PathVariable Long postId,
                                        @RequestBody PostDTO updatedPostDTO){
        try {
            PostEntity postEntity = PostDTO.toEntity(updatedPostDTO);
            PostDetailEntity postDetailEntity = PostDetailDTO.toEntity(updatedPostDTO.getPostDetail());

            List<PostImageDTO> uploadedImages = new ArrayList<>();

            for (PostImageDTO imageDTO : updatedPostDTO.getImages()) {
                // base64 string -> byte[] 변환
                byte[] byteImage = fileService.convertBase64ToImage(imageDTO.getImage());
                String imageUrl = fileService.uploadFile(byteImage, imageDTO.getPostId() + "_" + imageDTO.getImageId());
                imageDTO.setImageUrl(imageUrl);
                uploadedImages.add(imageDTO);
            }

            PostEntity updatedPost = postService.updatePost(postId, postEntity, postDetailEntity, uploadedImages);
            PostDTO savedPostDTO = PostDTO.fromEntity(updatedPost);
            return ResponseEntity.ok().body(savedPostDTO);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 게시물 조회_0.특정 게시물
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@AuthenticationPrincipal String userId,
                                         @PathVariable Long postId) {
        try {
            PostEntity post = postService.getPostById(postId);
            PostDTO postDTO = PostDTO.fromEntity(post);
            return ResponseEntity.ok(postDTO);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<PostEntity> response = ResponseDTO.<PostEntity>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 게시물 조회_1. 자기 게시물
    @GetMapping("/my-posts")
    public ResponseEntity<List<PostDTO>> getMyPosts(@AuthenticationPrincipal Long userId) {
        List<PostEntity> myPosts = postService.getPostsByUserId(userId);
        List<PostDTO> myPostsDTO = myPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(myPostsDTO);

    }

    // 게시물 조회_2. 구독자 게시물
    @GetMapping("/subscriber-posts")
    public ResponseEntity<List<PostDTO>> getSubscriberPosts(@AuthenticationPrincipal Long userId) {
        List<Long> subscriberIds = subscriberService.getSubscribersIds(userId);

        List<PostEntity> subscriberPosts = postService.getSubscriberPosts(subscriberIds);
        List<PostDTO> subscriberPostsDTO = subscriberPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(subscriberPostsDTO);
    }

    // 게시물 조회_3. 공개 범위 전체인 전체 게시물 조회
    @GetMapping("/public-posts")
    public ResponseEntity<List<PostDTO>> getPublicPosts(@AuthenticationPrincipal Long userId) {
        List<PostEntity> publicPosts = postService.getPublicPosts();
        List<PostDTO> publicPostsDTO = publicPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(publicPostsDTO);
    }

    // 게시물 조회_4. (관리자용) 전체 게시물 조회
    @GetMapping("/all-posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(@AuthenticationPrincipal Long userId) {
        List<PostEntity> allPosts = postService.getAllPosts();
        List<PostDTO> allPostsDTO = allPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allPostsDTO);
    }
}
