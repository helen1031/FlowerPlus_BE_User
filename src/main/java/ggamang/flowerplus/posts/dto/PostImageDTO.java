package ggamang.flowerplus.posts.dto;

import ggamang.flowerplus.posts.entity.PostImageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImageDTO {
    private Long imageId;
    private Long postId;
    private String imageUrl;
    private Date createdDate;

    private String image; // base64 담을 용도

    public PostImageDTO(PostImageEntity entity) {
        this.imageId = entity.getImageId();
        this.postId = entity.getPost().getPostId();
        this.createdDate = entity.getCreatedDate();
        this.imageUrl = entity.getImageUrl();
    }


    public static PostImageDTO fromEntity(PostImageEntity entity) {
        return PostImageDTO.builder()
                .imageId(entity.getImageId())
                .postId(entity.getPost().getPostId())
                .createdDate(entity.getCreatedDate())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    public static PostImageEntity toEntity(PostImageDTO dto) {
        PostImageEntity postImageEntity = new PostImageEntity();
        postImageEntity.setImageId(dto.getImageId());
        postImageEntity.setCreatedDate(dto.getCreatedDate());
        postImageEntity.setImageUrl(dto.getImageUrl());
        return postImageEntity;
    }
}
