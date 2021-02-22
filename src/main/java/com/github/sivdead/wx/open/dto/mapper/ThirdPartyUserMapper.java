package com.github.sivdead.wx.open.dto.mapper;

import com.github.sivdead.wx.open.dao.eo.ThirdPartyUser;
import com.github.sivdead.wx.open.dto.ThirdPartyUserDto;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface ThirdPartyUserMapper {

    ThirdPartyUserMapper INSTANCE = Mappers.getMapper(ThirdPartyUserMapper.class);

    @Mappings({
            @Mapping(target = "openid", source = "openId"),
            @Mapping(target = "unionid", source = "unionId")
    })
    ThirdPartyUser fromWxMpUser(WxMpUser wxMpUser);

    ThirdPartyUserDto toDto(ThirdPartyUser thirdPartyUser);

    default LocalDateTime map(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}
