package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.reggie.common.BaseContext;
import com.hss.reggie.common.R;
import com.hss.reggie.pojo.AddressBook;
import com.hss.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    /**
     * 根据用户id查询所有
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getId());
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public R<AddressBook> getItem(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook == null)return R.error("数据为空");
        return R.success(addressBook);
    }
    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if(addressBook == null)return R.error("数据为空");
        return R.success(addressBook);
    }
    /**
     * 保存新地址
     */
    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getId());
        //第一个地址设为默认地址
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,addressBook.getUserId());
        if(addressBookService.count(queryWrapper)==0){
            addressBook.setIsDefault(1);
        }
        addressBookService.save(addressBook);
        return R.success("保存地址成功");
    }
    /**
     * 修改默认地址
     */
    @PutMapping("/default")
    public R<String> updateDefault(@RequestBody AddressBook addressBook) {
        addressBookService.updateDefault(addressBook);
        return R.success("修改默认成功");
    }
    /**
     * 修改地址
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
    /**
     * 删除地址
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }
}
