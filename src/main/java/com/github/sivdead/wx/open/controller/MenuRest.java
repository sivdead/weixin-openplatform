package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.dto.MenuTemplateDto;
import com.github.sivdead.wx.open.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu")
@Slf4j
public class MenuRest {


    @PostMapping("/template")
    public R<MenuTemplateDto> createMenuTemplate(@RequestBody@Validated MenuTemplateDto menuTemplateDto){

        return R.ok(menuTemplateDto);
    }

}
