package com.github.sivdead.wx.open.dao.eo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 素材
 */
@Entity
@Table(name = "wx_tag",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_app_tag", columnNames = {"tag_id", "app_id"})
        },
        indexes = {
                @Index(name = "idx_app_id", columnList = "app_id")
        })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxTag {

    /**
     * 标签
     */
    @ManyToOne(targetEntity = Tag.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Tag tag;

    @Column(name = "tag_id", insertable = false, updatable = false)
    private Long tagId;

    @Column(name = "app_id", insertable = false, updatable = false)
    private String appId;

    @ManyToOne(targetEntity = ThirdPartyAccount.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", referencedColumnName = "app_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ThirdPartyAccount thirdPartyAccount;

    @Id
    private Long wxTagId;

}
