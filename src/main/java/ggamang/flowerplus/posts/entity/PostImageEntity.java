package ggamang.flowerplus.posts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String imageUrl;
}
