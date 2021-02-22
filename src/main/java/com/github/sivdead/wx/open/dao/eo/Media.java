package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

/**
 * 素材
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "media")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Media extends AbstractPersistable<Long> {

    /**
     * 素材类型 {@link WxConsts.MaterialType}
     */
    private String type;

    /**
     * 文件名
     */
    private String name;

    /**
     * oss上的访问url
     */
    private String ossUrl;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频介绍
     */
    private String description;

    @ManyToOne(targetEntity = MediaGroup.class)
    @JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MediaGroup group;

    @ManyToMany(targetEntity = Article.class)
    @JoinTable(name = "r_media_article",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            joinColumns = {
                    @JoinColumn(name = "article_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "media_id", referencedColumnName = "id")
            },
            indexes = {
                    @Index(name = "idx_article_id",columnList = "article_id"),
                    @Index(name = "idx_media_id",columnList = "media_id"),
            }
    )
    private List<Article> articles;

}
