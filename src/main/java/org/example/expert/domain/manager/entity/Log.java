package org.example.expert.domain.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Logs")
public class Log  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String requestUrl;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime currentTime;

    @Column(nullable = false)
    private String methodName;

    @Column(nullable = false)
    private boolean isSave;

    private Log(String requestUrl, Long userId, LocalDateTime currentTime, String methodName, boolean isSave ) {
        this.requestUrl = requestUrl;
        this.userId = userId;
        this.currentTime = currentTime;
        this.methodName = methodName;
        this.isSave = isSave;
    }

    public static Log of(String requestUrl, Long userId, LocalDateTime currentTime, String methodName, boolean isSave){
        return new Log(requestUrl, userId, currentTime , methodName, isSave);
    }

}
