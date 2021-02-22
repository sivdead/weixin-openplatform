package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * 群发消息接收者
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message_target")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTarget extends AbstractPersistable<Long> {

    @JoinColumns(
            value = {
                    @JoinColumn(referencedColumnName = "app_id", name = "app_id", nullable = false),
                    @JoinColumn(referencedColumnName = "message_id", name = "message_id", nullable = false)
            },
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @ManyToOne(targetEntity = MessageSender.class, optional = false)
    private MessageSender messageSender;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = ThirdPartyUser.class)
    private ThirdPartyUser thirdPartyUser;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),name = "tag_id",referencedColumnName = "id")
    @ManyToOne(targetEntity = Tag.class)
    private Tag tag;

    /**
     * 目标类型 0 所有人 1 标签 2 openid
     */
    private Integer targetType;


    /**
     * 发送状态 0 待发送 1 已发送 2 发送失败
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 错误码
     */
    private String errorCode;

}
