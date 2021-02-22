package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizerInfo;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

@Entity
@Table(
        name = "third_party_account",
        indexes = {
                @Index(columnList = "platform_account_id", name = "idx_platform_account_id"),
                @Index(columnList = "app_id", name = "idx_app_id"),
        })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyAccount{

    @Id
    @Column(name = "app_id")
    private String appId;

    private String appSecret;

    private String nickName;

    private String headImg;

    /**
     * 公众号类型 0 订阅号 1 由历史老帐号升级后的订阅号 2 服务号
     */
    private Integer serviceType;

    /**
     * 认证类型
     * -1 未认证
     * 0 微信认证
     * 1 新浪微博认证
     * 2 腾讯微博认证
     * 3 已资质认证通过但还未通过名称认证
     * 4 已资质认证通过、还未通过名称认证，但通过了新浪微博认证
     * 5 已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
     */
    private Integer verifyType;

    private String username;

    private String alias;

    private String qrcodeUrl;

    private String signature;

    private String principalName;

    @JoinColumn(name = "platform_account_id", referencedColumnName = "app_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
    @ManyToOne(targetEntity = OpenPlatformAccount.class, fetch = FetchType.LAZY)
    private OpenPlatformAccount openPlatformAccount;

    public static ThirdPartyAccount buildFromWxOpenAuthorizerInfo(@NotBlank String appId, @NotNull WxOpenAuthorizerInfo authorizerInfo) {
        return ThirdPartyAccount.builder()
                .nickName(authorizerInfo.getNickName())
                .alias(authorizerInfo.getAlias())
                .headImg(authorizerInfo.getHeadImg())
                .principalName(authorizerInfo.getPrincipalName())
                .serviceType(authorizerInfo.getServiceTypeInfo())
                .verifyType(authorizerInfo.getVerifyTypeInfo())
                .signature(authorizerInfo.getSignature())
                .qrcodeUrl(authorizerInfo.getQrcodeUrl())
                .username(authorizerInfo.getUserName())
                .appId(appId)
                .build();
    }
}
