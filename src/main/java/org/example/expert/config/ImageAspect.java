package org.example.expert.config;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.constants.BusinessRuleConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Aspect
@Slf4j
@Component
public class ImageAspect {

    /**
     * 이미지 크기, 개수 ,타입 체크 AOP @ImageValid 붙은 컨트롤러에만 적용
     * 맞는 타입체크하고 검사함
     */
    @Around("@annotation(org.example.expert.annotation.ImageValid)")
    public Object validParma(ProceedingJoinPoint point) throws Throwable {
        for (Object arg : point.getArgs()) {
            // 리스트 x , 리스트 비어있음, 타입이 다르면 for문 탈출
            if (!(arg instanceof List<?> list) || list.isEmpty() || !(list.get(0) instanceof MultipartFile)) {
                continue;
            }
            validateFile((List<MultipartFile>) list); // 이미지 검사
        }
        return point.proceed();
    }

    /**
     * 실제 이미지 valid 메소드
     * @param fileList
     */
    private void validateFile(List<MultipartFile> fileList) {

        // 이미지 개수 검사
        if (fileList.size() > BusinessRuleConstants.MAX_IMAGE_CNT) {
            throw new RuntimeException();
        }

        fileList.stream()
            .map(o -> (MultipartFile) o)
            .forEach(file -> {
                // 이미지 크기 검사
                if (file.getSize() > BusinessRuleConstants.MAX_IMAGE_SIZE) {
                    throw new RuntimeException();
                }
                // 이미지 타입 검사
                String contentType = file.getContentType();
                if (contentType == null ||
                    !(contentType.equals("image/png") || contentType.equals("image/jpeg"))) {
                    throw new RuntimeException();
                }
            });
    }
}
