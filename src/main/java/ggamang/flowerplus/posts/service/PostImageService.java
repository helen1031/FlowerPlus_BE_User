package ggamang.flowerplus.posts.service;

import ggamang.flowerplus.posts.entity.PostImageEntity;
import ggamang.flowerplus.posts.repository.PostImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostImageService {

    @Autowired
    private PostImageRepository postImageRepository;
    public PostImageEntity createPostImage(PostImageEntity postImageEntity) {
        log.info("postImageEntity: "+ postImageEntity);
        PostImageEntity savedEntity = postImageRepository.save(postImageEntity);
        log.info("Post Image Entity Id : {} - {} is saved", savedEntity.getPost().getPostId(), savedEntity.getImageId());
        return savedEntity;
    }

    public PostImageEntity updatePostImage(PostImageEntity postImageEntity) {
        if (postImageRepository.existsById(postImageEntity.getImageId())) {
            PostImageEntity updatedEntity = postImageRepository.save(postImageEntity);
            log.info("Post Image Entity Id : {} - {} is updated", updatedEntity.getPost().getPostId(), updatedEntity.getImageId());
            return updatedEntity;
        } else {
            throw new IllegalArgumentException("PostImageEntity with id: " + postImageEntity.getImageId() + " does not exist");
        }
    }

}
