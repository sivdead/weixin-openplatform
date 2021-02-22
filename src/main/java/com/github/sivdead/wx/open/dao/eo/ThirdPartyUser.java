package com.github.sivdead.wx.open.dao.eo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 第三方用户
 */
@Entity
@Table(name = "third_party_user",
        indexes = {
                @Index(name = "idx_openid", columnList = "openid", unique = true),
                @Index(name = "idx_unionid", columnList = "unionid")
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyUser {

    @JoinColumn(referencedColumnName = "app_id", name = "app_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = ThirdPartyAccount.class, optional = false)
    private ThirdPartyAccount thirdPartyAccount;

    @Column(name = "app_id", insertable = false, updatable = false)
    private String appId;

    @Id
    private String openid;

    private String unionid;

    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    private Boolean subscribe;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Integer sex;

    /**
     * 城市
     */
    private String city;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 用户的语言，简体中文为zh_CN
     */
    private String language;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，
     * 0代表640*640正方形头像），用户没有头像时该项为空。
     * 若用户更换头像，原有头像URL将失效。
     */
    private String headImgUrl;

    private LocalDateTime subscribeTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户关注的渠道来源，
     * ADD_SCENE_SEARCH 公众号搜索，
     * ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
     * ADD_SCENE_PROFILE_CARD 名片分享，
     * ADD_SCENE_QR_CODE 扫描二维码，
     * ADD_SCENE_PROFILE_LINK 图文页内名称点击，
     * ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
     * ADD_SCENE_PAID 支付后关注，
     * ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，
     * ADD_SCENE_OTHERS 其他
     */
    private String subscribeScene;

    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(name = "r_user_tag",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            joinColumns = {
                    @JoinColumn(name = "user_openid", referencedColumnName = "openid")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            },
            indexes = {
                @Index(name = "idx_user_openid",columnList = "user_openid"),
                @Index(name = "idx_tag_id",columnList = "tag_id"),
            }
    )
    private List<Tag> tags = new ArrayList<>();
}
