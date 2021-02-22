package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * 素材
 */
@Entity
@Table(name = "wx_media",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_tag", columnNames = {"media_id", "app_id"})
        },
        indexes = {
                @Index(name = "idx_app_id", columnList = "app_id")
        })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxMedia {


    /**
     * 微信图片素材的url
     */
    private String wxUrl;

    /**
     * 微信的素材id
     */
    @Id
    private String wxMediaId;


    @ManyToOne(targetEntity = Media.class, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), name = "media_id", referencedColumnName = "id")
    private Media media;

    @ManyToOne(targetEntity = ThirdPartyAccount.class, optional = false)
    @JoinColumn(name = "app_id", referencedColumnName = "app_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ThirdPartyAccount thirdPartyAccount;

}
