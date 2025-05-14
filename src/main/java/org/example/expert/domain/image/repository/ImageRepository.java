package org.example.expert.domain.image.repository;


import java.util.List;
import org.example.expert.domain.image.entity.Image;
import org.example.expert.domain.image.entity.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<org.example.expert.domain.image.entity.Image, Long> {

    default org.example.expert.domain.image.entity.Image findByIdOrElseThrow(Long reviewImageId) {
        return findById(reviewImageId).orElseThrow(
            () -> new RuntimeException("")
        );
    }

    public List<Image> findByTargetIdAndType(Long targetId, ImageType type);

    default List<Image> findByTargetIdAndTypeElseThrow(Long targetId, ImageType type) {
        List<Image> list = findByTargetIdAndType(targetId, type);
        if (list.isEmpty()) throw new RuntimeException("");
        return list;
    }
}