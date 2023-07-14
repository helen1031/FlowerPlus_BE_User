package ggamang.flowerplus.posts.entity;

import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "post") // post 필드를 equals()와 hashCode()에서 제외
@Table(name="POSTS_DETAILS")
public class PostDetailEntity {
    @Id
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private PostEntity post;

    private String height;

    private String feature;

    private int quantity;
}
