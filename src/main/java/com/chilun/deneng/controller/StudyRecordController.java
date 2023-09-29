package com.chilun.deneng.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chilun.deneng.Response.BaseResponse;
import com.chilun.deneng.Response.ResultCode;
import com.chilun.deneng.pojo.Article;
import com.chilun.deneng.pojo.StudyRecord;
import com.chilun.deneng.service.IArticleService;
import com.chilun.deneng.service.IStudyRecordService;
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
@RequestMapping("/studyRecord")
public class StudyRecordController {
    //增删改查
    @Autowired
    IStudyRecordService service;

    @GetMapping
    public BaseResponse queryStudyRecordById(@RequestParam int id) {
        StudyRecord studyRecord = service.getById(id);
        if (studyRecord == null) {
            return new BaseResponse("记录不存在", ResultCode.FAILURE);
        }
        return new BaseResponse(JSON.toJSONString(studyRecord), ResultCode.SUCCESS);
    }

    @GetMapping("/all")
    public BaseResponse queryStudyRecordAll() {
        List<StudyRecord> list = service.list();
        return new BaseResponse(JSON.toJSONString(list), ResultCode.SUCCESS);
    }

    @GetMapping("/{userId}")
    public BaseResponse queryStudyRecordByName(@PathVariable String userId) {
        QueryWrapper<StudyRecord> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        List<StudyRecord> StudyRecord = service.getBaseMapper().selectList(qw);
        return new BaseResponse(JSON.toJSONString(StudyRecord), ResultCode.SUCCESS);
    }

    @PostMapping
    public BaseResponse addStudyRecord(@RequestBody StudyRecord studyRecord) {
        QueryWrapper<StudyRecord> qw = new QueryWrapper<>();
        qw.eq("user_id", studyRecord.getUserId());
        qw.eq("learn_type", studyRecord.getLearnType());
        if (service.getBaseMapper().selectList(qw).size() != 0) {//说明已存在
            return new BaseResponse(null, ResultCode.FAILURE);
        }
        boolean save = service.save(studyRecord);
        if (save) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @PutMapping
    public BaseResponse updateStudyRecord(@RequestBody StudyRecord studyRecord) {
        boolean update = service.updateById(studyRecord);
        if (update) return new BaseResponse(null, ResultCode.SUCCESS);
        return new BaseResponse(null, ResultCode.FAILURE);
    }

    @DeleteMapping
    public BaseResponse deleteStudyRecord(@RequestParam int id) {
        boolean remove = service.removeById(id);
        if (remove) return new BaseResponse(null, ResultCode.SUCCESS);
        else return new BaseResponse(null, ResultCode.FAILURE);
    }
}
