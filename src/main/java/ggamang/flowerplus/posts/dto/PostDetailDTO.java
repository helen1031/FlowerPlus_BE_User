package ggamang.flowerplus.posts.dto;

import ggamang.flowerplus.posts.entity.PostDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDTO {
    private String postId;
    private int height;
    private String feature;
    private int quantity;

    public PostDetailDTO(PostDetailEntity entity) {
        this.postId = entity.getPost().getPostId();
        this.height = entity.getHeight();
        this.feature = entity.getFeature();
        this.quantity = entity.getQuantity();
    }


    public static PostDetailDTO fromEntity(PostDetailEntity postDetailEntity) {
        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setPostId(postDetailEntity.getPostId());
        postDetailDTO.setHeight(postDetailEntity.getHeight());
        postDetailDTO.setFeature(postDetailEntity.getFeature());
        postDetailDTO.setQuantity(postDetailEntity.getQuantity());
        return postDetailDTO;
    }

    public static PostDetailEntity toEntity(PostDetailDTO dto) {
        return PostDetailEntity.builder()
                .height(dto.getHeight())
                .feature(dto.getFeature())
                .quantity(dto.getQuantity())
                .build();
    }
}

