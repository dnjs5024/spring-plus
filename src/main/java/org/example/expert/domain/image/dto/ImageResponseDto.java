package org.example.expert.domain.image.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageResponseDto {

    private final String imgUrl;

    private final String imgKey;

    public static ImageResponseDto toDto(org.example.expert.domain.image.entity.Image image) {
        return new ImageResponseDto(
            image.getImgUrl(),
            image.getImgKey()
        );
    }

}
