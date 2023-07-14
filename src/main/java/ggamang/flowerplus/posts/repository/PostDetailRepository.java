package ggamang.flowerplus.posts.repository;

import ggamang.flowerplus.posts.entity.PostDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDetailRepository extends JpaRepository<PostDetailEntity, Long> {

    PostDetailEntity findByPostId(Long postId);
}
