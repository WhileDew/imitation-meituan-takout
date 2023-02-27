package com.example.ruijiwaimai.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.ruijiwaimai.entity.AddressBook;
import com.example.ruijiwaimai.service.IAddressBookService;
import com.example.ruijiwaimai.utils.OnlineHolder;
import com.example.ruijiwaimai.utils.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private IAddressBookService addressBookService;

    @PostMapping
    public Result saveAddressBook(@RequestBody AddressBook addressBook){
        return addressBookService.saveAddressBook(addressBook);
    }

    // @PostMapping("update")
    // public Result updateAddressBook(@RequestBody AddressBook addressBook){
    //     return addressBookService.updateAddressBook(addressBook);
    // }

    @GetMapping("/list")
    public Result getAddressBookList(){
        // 获取用户id
        Long userId = OnlineHolder.getId();
        return Result.success(addressBookService.query().eq("user_id", userId).list());
    }


    @GetMapping("/default")
    public Result defaultAddressBook(){
        return addressBookService.defaultAddressBook();
    }

}
