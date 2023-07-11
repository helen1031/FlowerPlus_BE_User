package ggamang.flowerplus.posts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POSTS_DETAILS")
public class PostDetailEntity {
    @Id
    private String postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private int height;

    private String feature;

    private int quantity;
}
