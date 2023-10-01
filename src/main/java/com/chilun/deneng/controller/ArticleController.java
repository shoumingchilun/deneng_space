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
        Article article = null;
        try {
            article = service.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse("数据库错误", ResultCode.FAILURE);
        }
        if (article == null) {
            return new BaseResponse("记录不存在", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(article), ResultCode.SUCCESS);
    }

    @GetMapping("/all")
    public BaseResponse queryArticleAll() {
        List<Article> list = null;
        try {
            list = service.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse("数据库错误", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @GetMapping("/{name}")
    public BaseResponse queryArticleByName(@PathVariable String name) {
        List<Article> article = null;
        try {
            QueryWrapper<Article> qw = new QueryWrapper<>();
            qw.eq("name", name);
            article = service.getBaseMapper().selectList(qw);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse("数据库错误", ResultCode.FAILURE);
        }
        return article.size() == 0 ?
                new BaseResponse(null, ResultCode.SUCCESS) :
                new BaseResponse(JSON.toJSONString(article.get(0)), ResultCode.SUCCESS);//name唯一，所以只有一个
    }

    @PostMapping
    public BaseResponse addArticle(@RequestBody Article article) {
        try {
            boolean save = service.save(article);
            if (save) return new BaseResponse("添加成功", ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResponse( "数据库错误", ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateArticle(@RequestBody Article article) {
        try {
            boolean update = service.updateById(article);
            if (update) return new BaseResponse("修改成功", ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResponse("数据库错误", ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteArticle(@RequestParam int id) {
        try {
            boolean remove = service.removeById(id);
            if (remove) return new BaseResponse("删除成功", ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResponse("数据库错误", ResultCode.FAILURE);
    }
}
