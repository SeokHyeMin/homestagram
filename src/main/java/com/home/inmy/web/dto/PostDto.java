package com.home.inmy.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PostsDto {

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    private String image;

    private String content;

    private String tag;

    private String author;

}
