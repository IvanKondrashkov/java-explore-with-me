package ru.practicum.dto;

import lombok.*;
import java.time.LocalDateTime;
import ru.practicum.validation.IpAddress;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class EndpointHit {
    private Long id;
    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    @IpAddress
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
