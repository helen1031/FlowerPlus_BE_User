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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String postId;
    private String userId;
    private PostRange postRange;
    private boolean isForExchange;
    private boolean isForSale;
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
        this.isForExchange = entity.isForExchange();
        this.isForSale = entity.isForSale();
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
                .isForExchange(dto.isForExchange())
                .isForSale(dto.isForSale())
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
        }

        if (dto.getImages() != null) {
            List<PostImageEntity> imageEntities = dto.getImages().stream()
                    .map(PostImageDTO::toEntity)
                    .collect(Collectors.toList());
            postEntity.setImages(imageEntities);
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
        return postDTO;
    }
}
