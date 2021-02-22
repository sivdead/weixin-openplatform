package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

/**
 * 群发消息
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends AbstractPersistable<Long> {

    /**
     * 消息类型 {@link WxConsts.MassMsgType}
     */
    private String messageType;

    private String content;

    @ManyToOne(targetEntity = Media.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id",referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Media media;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 目标类型 0 所有人 1 标签 2 openid
     */
    private Integer targetType;

    /**
     * 推荐语
     */
    private String recommend;

    /**
     * 图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。
     */
    private Integer sendIgnoreReprint = 0;

}
