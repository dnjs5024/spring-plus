package org.example.expert.domain.image.service;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.example.expert.domain.image.dto.ImageResponseDto;
import org.example.expert.domain.image.entity.Image;
import org.example.expert.domain.image.entity.ImageType;
import org.example.expert.domain.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    /**
     * 외부 클라우드서버에 사진 저장 후 사진 url 리턴
     *
     * @param fileList
     * @return
     */
    @Override
    public List<Map<String, Object>> uploadFile(List<MultipartFile> fileList) throws IOException {
        List<Map<String, Object>> returnList = new ArrayList<>(); // 업로드한 사진 정보 담아서 리턴

        for (MultipartFile file : fileList) {
            Map<String, Object> urlMap = new HashMap<>(); // url, key 맵에 저장
            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)  // 업로드할 대상 버킷 이름
                .key(key)  // 버킷 내 저장할 경로 (위에서 만든 고유 파일명)
                .contentType(file.getContentType()) // 타입 image/png
                .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            urlMap.put("url", "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key);
            urlMap.put("key", key);
            returnList.add(urlMap);
        }
        return returnList;
    }

    /**
     * aws 에 사진 업로드 하고 이미지 파일 db에 저장
     *
     * @param files    이미지 파일
     * @param targetId 타겟 식별자 ex) 가게 , 리뷰 등
     * @param type     타겟 타입 ex) 가게 , 리뷰 등
     */
    @Override
    public void fileSave(List<MultipartFile> files, Long targetId, ImageType type) {
        List<String> uploadedKey = new ArrayList<>(); // 업로드 성공한 키 저장
        try {// db에 저장 실패 or aws 에 업로드 실패 시 에러
            uploadFile(files).forEach(data -> {
                String url = (String) data.get("url");
                String key = (String) data.get("key");
                imageRepository.save(Image.of(targetId, url, type, key));
                uploadedKey.add(key);
            });
        } catch (Exception e) {
            for (String key : uploadedKey) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder() // aws 에 이미 올라간 이미지 삭제
                    .bucket(bucketName)  // 연결 된 대상 버킷 이름
                    .key(key)  // 버킷 내 삭제할 객체 키
                    .build();
                s3Client.deleteObject(deleteObjectRequest);
            }
            throw new RuntimeException();
        }

    }

    /**
     * 타겟 식별자,타입으로 조회한 후 결과 url 를 String List 에 담아서 반환
     *
     * @param targetId 타겟 식별자 ex) 가게 , 리뷰 등
     * @param type     타겟 타입 ex) 가게 , 리뷰 등
     * @return List<String> 이미지 url
     */
    @Override
    public List<ImageResponseDto> find(Long targetId, ImageType type) {
        return imageRepository.findByTargetIdAndTypeElseThrow(targetId, type).stream()
            .map(ImageResponseDto::toDto).toList();
    }

    @Override
    public void update(Long targetId, ImageType imageType, List<MultipartFile> fileList) {
        delete(find(targetId, imageType).stream().map(ImageResponseDto::getImgKey).toList(),
            imageType, targetId);// 기존 사진 삭제
        fileSave(fileList, targetId, imageType);
    }

    @Override
    public void delete(List<String> keys, ImageType imageType, Long targetId) {
        for (String key : keys) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)  // 연결 된 대상 버킷 이름
                .key(key)  // 버킷 내 삭제할 객체 키
                .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
        imageRepository.deleteAll(
            imageRepository.findByTargetIdAndTypeElseThrow(targetId, imageType));
    }
}
