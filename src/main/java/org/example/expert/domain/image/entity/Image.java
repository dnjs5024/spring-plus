package org.example.expert.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetId; // Store.id, Menu.id, Review.id 중 하나

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String imgKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType type; // STORE, MENU,

    private Image(Long targetId, String imgUrl,ImageType type, String imgKey) {
        this.targetId = targetId;
        this.imgUrl = imgUrl;
        this.type = type;
        this.imgKey = imgKey;
    }

    public static Image of(Long targetId, String imgUrl,ImageType type,String pKey){
        return new Image(targetId, imgUrl, type, pKey);
    }

}
