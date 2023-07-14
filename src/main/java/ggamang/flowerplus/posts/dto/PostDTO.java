package ggamang.flowerplus.posts.dto;

import ggamang.flowerplus.posts.FlowerType;
import ggamang.flowerplus.posts.PostRange;
import ggamang.flowerplus.posts.entity.PostDetailEntity;
import ggamang.flowerplus.posts.entity.PostEntity;
import ggamang.flowerplus.posts.entity.PostImageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private Long userId;
    private PostRange postRange;

    private boolean forExchange;

    private boolean forSale;
    private String flowerName;
    private String content;
    private FlowerType flowerType;
    private Date createdDate;
    private Date updateDate;
    private PostDetailDTO postDetail;
    private List<PostImageDTO> images;

    public PostDTO(final PostEntity entity) {
        this.postId = entity.getPostId();
        this.userId = entity.getUserId();
        this.postRange = entity.getPostRange();
        this.forExchange = entity.isForExchange();
        this.forSale = entity.isForSale();
        this.flowerName = entity.getFlowerName();
        this.content = entity.getContent();
        this.flowerType = entity.getFlowerType();
        this.createdDate = entity.getCreatedDate();
        this.updateDate = entity.getUpdateDate();

        if (entity.getPostDetail() != null) {
            this.postDetail = new PostDetailDTO(entity.getPostDetail());
        }

        if (entity.getImages() != null) {
            this.images = entity.getImages().stream()
                    .map(PostImageDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public static PostEntity toEntity(final PostDTO dto) {
        PostEntity postEntity = PostEntity.builder()
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .postRange(dto.getPostRange())
                .forExchange(dto.isForExchange())
                .forSale(dto.isForSale())
                .flowerName(dto.flowerName)
                .content(dto.getContent())
                .flowerType(dto.getFlowerType())
                .createdDate(dto.getCreatedDate())
                .updateDate(dto.getUpdateDate())
        .build();

        if (dto.getPostDetail() != null) {
            PostDetailEntity postDetailEntity = PostDetailDTO.toEntity(dto.getPostDetail());
            postEntity.setPostDetail(postDetailEntity);
            postDetailEntity.setPost(postEntity);
        }  else {
            postEntity.setPostDetail(new PostDetailEntity());
        }

        if (dto.getImages() != null) {
            List<PostImageEntity> imageEntities = dto.getImages().stream()
                    .map(imageDTO -> PostImageDTO.toEntity(imageDTO, postEntity))
                    .collect(Collectors.toList());
            postEntity.setImages(imageEntities);
        } else {
            postEntity.setImages(new ArrayList<>());
        }


        return postEntity;
    }

    public static PostDTO fromEntity(final PostEntity postEntity) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(postEntity.getPostId());
        postDTO.setUserId(postEntity.getUserId());
        postDTO.setPostRange(postEntity.getPostRange());
        postDTO.setForExchange(postEntity.isForExchange());
        postDTO.setForSale(postEntity.isForSale());
        postDTO.setFlowerName(postEntity.getFlowerName());
        postDTO.setContent(postEntity.getContent());
        postDTO.setFlowerType(postEntity.getFlowerType());
        postDTO.setCreatedDate(postEntity.getCreatedDate());
        postDTO.setUpdateDate(postEntity.getUpdateDate());

        // Set postDetail only if it is not null
        if (postEntity.getPostDetail() != null) {
            try {
                postDTO.setPostDetail(new PostDetailDTO(postEntity.getPostDetail()));
            } catch (Exception e) {
                System.err.println("Failed to convert PostDetailEntity to PostDetailDTO: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            postDTO.setPostDetail(null);
        }

        // Set images if the list is not null or empty
        if (postEntity.getImages() != null && !postEntity.getImages().isEmpty()) {
            try {
                postDTO.setImages(postEntity.getImages().stream().map(imageEntity -> {
                    try {
                        return new PostImageDTO(imageEntity);
                    } catch (Exception e) {
                        System.err.println("Failed to convert PostImageEntity to PostImageDTO: " + e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList()));
            } catch (Exception e) {
                System.err.println("Failed to convert PostImageEntity list to PostImageDTO list: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            postDTO.setImages(null);
        }


        return postDTO;
    }
}
