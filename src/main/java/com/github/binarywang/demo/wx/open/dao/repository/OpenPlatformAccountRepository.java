package com.github.binarywang.demo.wx.open.dao.repository;

import com.github.binarywang.demo.wx.open.dao.eo.OpenPlatformAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenPlatformAccountRepository extends JpaRepository<OpenPlatformAccount, Long> {

    OpenPlatformAccount findByPrincipalName(String principalName);

}
