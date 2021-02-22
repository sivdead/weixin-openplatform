package com.github.sivdead.wx.open.dto;

import com.github.sivdead.wx.open.dao.eo.Media;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String author;

    private String digest;

    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    private Integer showCoverPic = 0;

    private String contentSourceUrl;

    /**
     * 是否打开评论，0不打开，1打开
     */
    private Integer needOpenComment = 0;

    /**
     * 是否粉丝才可评论，0所有人可评论，1粉丝才可评论
     */
    private Integer onlyFansCanComment = 0;

    @NotNull
    private Long thumbMediaId;

    private Media thumbMedia;

}
