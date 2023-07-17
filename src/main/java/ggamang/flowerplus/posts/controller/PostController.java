package ggamang.flowerplus.posts.controller;

import ggamang.flowerplus.common.dto.ResponseDTO;
import ggamang.flowerplus.files.FileService;
import ggamang.flowerplus.posts.dto.PostDTO;
import ggamang.flowerplus.posts.dto.PostDetailDTO;
import ggamang.flowerplus.posts.dto.PostImageDTO;
import ggamang.flowerplus.posts.entity.PostDetailEntity;
import ggamang.flowerplus.posts.entity.PostEntity;
import ggamang.flowerplus.posts.entity.PostImageEntity;
import ggamang.flowerplus.posts.service.PostDetailService;
import ggamang.flowerplus.posts.service.PostImageService;
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
    private PostDetailService postDetailService;
    @Autowired
    private PostImageService postImageService;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private FileService fileService;


    // 게시물 등록
    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal Long userId,
                                        @RequestBody PostDTO newPostDTO){
        try {

            // 1. postEntity 를 저장한다
            PostEntity postEntity = PostDTO.toEntity(newPostDTO);
            postEntity.setUserId(userId);
            postEntity.setCreatedDate(new Date());
            PostEntity savedPost = postService.createPost(postEntity);

            // 2. postDetailEntity를 저장한다
            PostDetailEntity postDetailEntity = PostDetailDTO.toEntity(newPostDTO.getPostDetail());
            postDetailEntity.setPostId(savedPost.getPostId());
            postDetailEntity.setPost(savedPost);
            PostDetailEntity savedPostDetailEntity = postDetailService.createPostDetail(postDetailEntity);
            savedPost.setPostDetail(savedPostDetailEntity);

            // 3. postImageEntity 각각 저장한다.
            List<PostImageEntity> postImageEntities = new ArrayList<>();
            for (PostImageDTO imageDTO : newPostDTO.getImages()) {
                //imageDTO.setPostId(newPostDTO.getPostId());

                // base64 string -> byte[] 변환
                byte[] byteImage = fileService.convertBase64ToImage(imageDTO.getImage());

                // 임시 이름으로 우선 저장하여 이미지 아이디 채번
                String initialImageName = "temp";
                String fileName = savedPost.getPostId() + "_" + initialImageName+".png";
                String imageUrl = fileService.uploadFile(byteImage, fileName);
                imageDTO.setImageUrl(imageUrl);
                PostImageEntity postImageEntity =  imageDTO.toEntity(imageDTO, savedPost);
                PostImageEntity savedImageEntity = postImageService.createPostImage(postImageEntity);

                // rename하여 정식 이름으로 업데이트
                String newImageName = savedPost.getPostId() + "_" + savedImageEntity.getImageId()+".png";
                String newImageUrl = fileService.renameFile(fileName, newImageName);

                savedImageEntity.setPost(savedPost);
                savedImageEntity.setImageUrl(newImageUrl);
                savedImageEntity.setCreatedDate(new Date());

                PostImageEntity finalImageEntity = postImageService.updatePostImage(savedImageEntity);
                postImageEntities.add(finalImageEntity);
            }
            savedPost.setImages(postImageEntities);
            log.info("savedPost: "+savedPost);
            PostDTO savedPostDTO = PostDTO.fromEntity(savedPost);
            log.info("savedPostDTO: "+savedPostDTO);
            return ResponseEntity.ok().body(savedPostDTO);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
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
            // Convert DTOs to entities
            PostEntity postEntity = PostDTO.toEntity(updatedPostDTO);
            PostDetailEntity postDetailEntity = PostDetailDTO.toEntity(updatedPostDTO.getPostDetail());

            // Update postEntity
            PostEntity updatedPost = postService.updatePost(postEntity);

            // Update postDetailEntity
            postDetailService.updatePostDetail(postDetailEntity);

            // Update postImageEntity
            for (PostImageDTO imageDTO : updatedPostDTO.getImages()) {
                // Convert base64 string -> byte[]
                byte[] byteImage = fileService.convertBase64ToImage(imageDTO.getImage());
                // Upload image and set URL
                String imageUrl = fileService.uploadFile(byteImage, postId + "_" + imageDTO.getImageId());
                imageDTO.setImageUrl(imageUrl);
                // Convert DTO to entity and update
                PostImageEntity postImageEntity = PostImageDTO.toEntity(imageDTO, updatedPost);
                postImageService.updatePostImage(postImageEntity);
            }

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
    public ResponseEntity<List<PostDTO>> getPublicPosts() {
        List<PostEntity> publicPosts = postService.getPublicPosts();
        List<PostDTO> publicPostsDTO = publicPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(publicPostsDTO);
    }

    // 게시물 조회_4. 공개 범위 전체인 특정 ID의 게시물 조회
    @GetMapping("/user/{otherUserId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@AuthenticationPrincipal Long userId,
                                                          @PathVariable Long otherUserId) {
        List<PostEntity> publicPosts = postService.getOthersPostsByUserId(userId, otherUserId);
        List<PostDTO> otherIdPostDTO = publicPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(otherIdPostDTO);
    }

    // 게시물 조회_5. (관리자용) 전체 게시물 조회
    @GetMapping("/all-posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(@AuthenticationPrincipal Long userId) {
        List<PostEntity> allPosts = postService.getAllPosts();
        List<PostDTO> allPostsDTO = allPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allPostsDTO);
    }
}
