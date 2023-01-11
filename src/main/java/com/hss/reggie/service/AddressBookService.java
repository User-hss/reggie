package com.hss.reggie.service;

import com.hss.reggie.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author master
* @description 针对表【address_book(地址管理)】的数据库操作Service
* @createDate 2023-01-08 20:37:00
*/
@Transactional
public interface AddressBookService extends IService<AddressBook> {
    /**
     * 修改默认地址
     */
    void updateDefault(AddressBook addressBook);
}
