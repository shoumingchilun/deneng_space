package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.Article;
import com.chilun.deneng.pojo.User;
import com.chilun.deneng.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    //增删改查
    @Autowired
    IArticleService service;

    @GetMapping
    public BaseResponse queryArticleById(@RequestParam int id) {
        Article article = service.getById(id);
        if (article == null) {
            return new BaseResponse("记录不存在", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(article), ResultCode.SUCCESS);
    }

    @GetMapping("/all")
    public BaseResponse queryArticleAll() {
        List<Article> list = service.list();
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @GetMapping("/{name}")
    public BaseResponse queryArticleByName(@PathVariable String name) {
        QueryWrapper<Article> qw = new QueryWrapper<>();
        qw.eq("name", name);
        List<Article> article = service.getBaseMapper().selectList(qw);
        return article.size() == 0 ?
                new BaseResponse(null, ResultCode.SUCCESS) :
                new BaseResponse(JSON.toJSONString(article.get(0)), ResultCode.SUCCESS);
    }

    @PostMapping
    public BaseResponse addArticle(@RequestBody Article article) {
        boolean save = service.save(article);
        if (save) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateArticle(@RequestBody Article article) {

        boolean update = service.updateById(article);
        if (update) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteArticle(@RequestParam int id) {
        boolean remove = service.removeById(id);
        if (remove) return new BaseResponse(null, ResultCode.SUCCESS);
        else return new BaseResponse(null, ResultCode.FAILURE);
    }
}
