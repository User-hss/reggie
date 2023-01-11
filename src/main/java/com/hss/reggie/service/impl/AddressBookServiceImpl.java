package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.common.BaseContext;
import com.hss.reggie.pojo.AddressBook;
import com.hss.reggie.service.AddressBookService;
import com.hss.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-01-08 20:37:00
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

    @Override
    public void updateDefault(AddressBook addressBook) {
        //修改原默认地址
        LambdaUpdateWrapper<AddressBook> updateWrap = new LambdaUpdateWrapper<>();
        updateWrap.eq(AddressBook::getUserId, BaseContext.getId());
        updateWrap.eq(AddressBook::getIsDefault,1);
        updateWrap.set(AddressBook::getIsDefault,0);
        this.update(updateWrap);
        //设置新默认地址
        addressBook.setIsDefault(1);
        this.updateById(addressBook);
    }
}




