package com.hgt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgt.domain.DataResult;
import com.hgt.domain.SimpleStringBean;
import com.hgt.entity.LogBeatsNormal;
import com.hgt.mapper.LogBeatsNormalMapper;
import com.hgt.service.LogBeatsNormalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * README:
 * Created by root on 11/17/16.
 * =============================================================================
 * CHANGELOG:
 */
@Api(value = "正常心跳信息 val", description = "关于正常心跳信息操作的接口")
@RestController
public class LogBeatsNormalController {

    private final String BASE_URL = "logb/beats/normal";

    @Autowired
    LogBeatsNormalService logBeatsNormalService;

    @Autowired
    LogBeatsNormalMapper logBeatsNormalMapper;

    @ApiOperation(value = "根据行id获取单条信息", notes = "已完成")
    @RequestMapping(value = BASE_URL + "/rid/{strId}", method = RequestMethod.GET)
    public DataResult<LogBeatsNormal> findById(@PathVariable String strId) {
        return logBeatsNormalService.findById(strId);
    }

    @ApiOperation(value = "获取所有记录（不分页）", notes = "已完成")
    @RequestMapping(value = BASE_URL + "/all", method = RequestMethod.GET)
    public DataResult<List<LogBeatsNormal>> findAll() {
        return logBeatsNormalService.findAll();
    }

    @ApiOperation(value = "获取所有记录（分页）", notes = "已完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第pageNum页", required = true,
                    dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页的条数", required = true,
                    dataType = "int", paramType = "path")
    })
    @RequestMapping(value = BASE_URL + "/all/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public DataResult<PageInfo> findAllByPages(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {

        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<LogBeatsNormal> list = logBeatsNormalMapper.selectAll();
        PageInfo pageInfo = new PageInfo(list);
        return new DataResult<>(pageInfo);
    }

    @ApiOperation(value = "根据行id删除单条信息", notes = "已完成")
    @RequestMapping(value = BASE_URL + "/delete/id/{strId}", method = RequestMethod.GET)
    public DataResult<SimpleStringBean> deleteById(@PathVariable String strId) {
        return logBeatsNormalService.deleteById(strId);
    }


    @ApiOperation(value = "插入新记录", notes = "已完成")
    @RequestMapping(value = BASE_URL + "/add", method = RequestMethod.POST)
    public DataResult<SimpleStringBean> add(@RequestBody LogBeatsNormal logBeatsNormal) {
        return logBeatsNormalService.add(logBeatsNormal);
    }


    @ApiOperation(value = "更新记录", notes = "已完成")
    @RequestMapping(value = BASE_URL + "/update", method = RequestMethod.POST)
    public DataResult<SimpleStringBean> update(@RequestBody LogBeatsNormal logBeatsNormal) {
        return logBeatsNormalService.update(logBeatsNormal);
    }

}
