package ggamang.flowerplus.posts.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POSTS_IMAGES")
public class PostImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private PostEntity post;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String imageUrl;
}
