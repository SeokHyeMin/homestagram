package com.home.inmy.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long post_num;

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    private List<MultipartFile> imageFiles;

    @NotBlank
    private String content;

    private String tag;

    private String author;

    @NotBlank
    private String category;

    private LocalDateTime writeTime;

}
