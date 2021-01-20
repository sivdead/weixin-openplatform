package com.github.binarywang.demo.wx.open.dao.repository;

import com.github.binarywang.demo.wx.open.dao.eo.ThirdPartyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyAccountRepository extends JpaRepository<ThirdPartyAccount,Long> {

    ThirdPartyAccount findByAppId(String appId);

}
