package com.github.sivdead.wx.open.dao.eo;

import com.github.sivdead.wx.open.dto.mapper.MediaMapper;
import lombok.*;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * 消息
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "article")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
public class Article extends AbstractPersistable<Long> {

    private String title;

    private String content;

    private String author;

    private String digest;

    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    private Integer showCoverPic;

    private String contentSourceUrl;

    /**
     * 是否打开评论，0不打开，1打开
     */
    private Integer needOpenComment;

    /**
     * 是否粉丝才可评论，0所有人可评论，1粉丝才可评论
     */
    private Integer onlyFansCanComment;

    @JoinColumn(name = "thumb_media_id",referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = Media.class,fetch = FetchType.LAZY, optional = false)
    private Media thumbMedia;

}
