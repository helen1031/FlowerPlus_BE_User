package ggamang.flowerplus.posts.repository;

import ggamang.flowerplus.posts.PostRange;
import ggamang.flowerplus.posts.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    PostEntity findByPostId(Long postId);

    List<PostEntity> findByUserIdOrderByCreatedDateDesc(Long userId);

    List<PostEntity> findByUserIdInAndPostRangeNotInOrderByCreatedDateDesc(List<Long> subscriberIds, Collection<PostRange> postRanges);

    List<PostEntity> findAllByPostRangeInOrderByCreatedDateDesc(Collection<PostRange> postRanges);

    List<PostEntity> findAllByUserIdAndPostRangeInOrderByCreatedDateDesc(Long userId, Collection<PostRange> postRanges);

}
