package com.example.ruijiwaimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruijiwaimai.entity.AddressBook;
import com.example.ruijiwaimai.mapper.AddressBookMapper;
import com.example.ruijiwaimai.service.IAddressBookService;
import com.example.ruijiwaimai.utils.OnlineHolder;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {
    @Override
    public Result defaultAddressBook() {
        // 获取当前用户id
        Long userId = OnlineHolder.getId();
        AddressBook addressBook = query().eq("user_id", userId).one();
        if (addressBook == null){
            return Result.error("还没有添加地址");
        }
        return Result.success(addressBook);
    }

    @Override
    public Result saveAddressBook(AddressBook addressBook) {
        // 获取当前用户id
        Long userId = OnlineHolder.getId();
        addressBook.setUserId(userId);
        // 新增
        save(addressBook);
        return Result.success("添加成功");
    }

    @Override
    public Result updateAddressBook(AddressBook addressBook) {
        // 判断id是否正确
        if (getById(addressBook.getId()) == null){
            return Result.error("没有这个地址信息");
        }
        // 获取当前用户id
        Long userId = OnlineHolder.getId();
        addressBook.setUserId(userId);
        updateById(addressBook);
        return Result.success("更新成功");
    }
}
