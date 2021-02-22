package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * 群发消息发送者
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message_sender")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSender extends AbstractPersistable<Long> {

    @JoinColumn(referencedColumnName = "app_id", name = "app_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = ThirdPartyAccount.class, optional = false)
    private ThirdPartyAccount thirdPartyAccount;

    @JoinColumn(referencedColumnName = "id", name = "message_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = Message.class, optional = false)
    private Message message;

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
