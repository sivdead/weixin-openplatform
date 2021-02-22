package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.dao.eo.Tag;
import com.github.sivdead.wx.open.dto.TagDto;
import com.github.sivdead.wx.open.service.TagService;
import com.github.sivdead.wx.open.web.R;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/tag")
public class TagRest {


    private final TagService tagService;

    public TagRest(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public R<TagDto> updateTag(@RequestBody @Validated TagDto tagDto) {

        return R.ok(tagService.addTag(tagDto));
    }

    @PutMapping("/{id}")
    public R<TagDto> updateTag(@RequestBody @Validated TagDto tagDto,
                               @PathVariable("id") Long id) throws WxErrorException {
        return R.ok(tagService.updateTag(tagDto, id));
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteTag(@PathVariable("id") Long id) throws WxErrorException {

        tagService.deleteTag(id);
        return R.ok();
    }

    @GetMapping
    public R<Page<TagDto>> queryTag(Pageable pageable,
                                    @QuerydslPredicate(root = Tag.class) Predicate predicate) {
        return R.ok(tagService.queryTag(pageable, predicate));
    }
}
