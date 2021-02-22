package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.service.FileService;
import com.github.sivdead.wx.open.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/common")
public class CommonRest {


    @Autowired
    private FileService fileService;

    @GetMapping("/file/policy")
    public R<Map<String, String>> getFilePolicy() {
        return R.ok(fileService.getPolicyFile());
    }
}
