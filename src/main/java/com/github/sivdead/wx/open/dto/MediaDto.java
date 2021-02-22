package com.github.sivdead.wx.open.dto;

import com.github.sivdead.wx.open.constant.ValidateGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDto {

    /**
     * 素材类型 {@link me.chanjar.weixin.common.api.WxConsts.MaterialType}
     */
    private String type;

    /**
     * 文件名
     */
    private String name;

    /**
     * oss上的访问url
     */
    @URL
    private String ossUrl;

    /**
     * 视频标题
     */
    @NotBlank(groups = {ValidateGroup.A.class})
    private String title;

    /**
     * 视频介绍
     */
    @NotBlank(groups = {ValidateGroup.A.class})
    private String introduction;

    private Long id;

    @NotNull
    private Long groupId;

    private MediaGroupDto group;

}
