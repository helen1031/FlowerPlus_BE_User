package ggamang.flowerplus.posts.service;

import ggamang.flowerplus.posts.entity.PostDetailEntity;
import ggamang.flowerplus.posts.repository.PostDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostDetailService {

    @Autowired
    private PostDetailRepository postDetailRepository;

    public PostDetailEntity createPostDetail(PostDetailEntity postDetailEntity){
        PostDetailEntity savedPostDetailEntity = postDetailRepository.save(postDetailEntity);
        log.info("Post Detail Entity Id : {} is saved", postDetailEntity.getPostId());

        return savedPostDetailEntity;
    }

    public PostDetailEntity updatePostDetail(final PostDetailEntity postDetailEntity){
        // Fetch the postDetail to be updated
        Long postId = postDetailEntity.getPostId();
        PostDetailEntity previousPostDetailEntity = postDetailRepository.findByPostId(postId);
        if (previousPostDetailEntity == null) {
            throw new IllegalArgumentException("PostDetail with id: " + postId + " does not exist");
        }

        // Set the updated details for the postDetail
        previousPostDetailEntity.setHeight(postDetailEntity.getHeight());
        previousPostDetailEntity.setFeature(postDetailEntity.getFeature());
        previousPostDetailEntity.setQuantity(postDetailEntity.getQuantity());

        PostDetailEntity updatedPostDetailEntity = postDetailRepository.save(previousPostDetailEntity);

        log.info("Post Detail Entity Id : {} is updated", postId);

        return updatedPostDetailEntity;
    }


}
