package ggamang.flowerplus.posts.entity;

import ggamang.flowerplus.posts.FlowerType;
import ggamang.flowerplus.posts.PostRange;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "postDetail") // postDetail 필드를 equals()와 hashCode()에서 제외
@Table(name="POSTS")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PostRange postRange;

    private boolean forExchange;

    private boolean forSale;

    private String flowerName;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private FlowerType flowerType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private PostDetailEntity postDetail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImageEntity> images;
}
