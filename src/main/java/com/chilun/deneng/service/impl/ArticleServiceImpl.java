package com.chilun.deneng.service.impl;

import com.chilun.deneng.pojo.Article;
import com.chilun.deneng.dao.ArticleMapper;
import com.chilun.deneng.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chilun
 * @since 2023-09-28
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
