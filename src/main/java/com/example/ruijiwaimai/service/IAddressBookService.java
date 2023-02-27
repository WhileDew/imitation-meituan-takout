package com.example.ruijiwaimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruijiwaimai.entity.AddressBook;
import com.example.ruijiwaimai.mapper.AddressBookMapper;
import com.example.ruijiwaimai.utils.Result;

public interface IAddressBookService extends IService<AddressBook> {
    Result defaultAddressBook();

    Result saveAddressBook(AddressBook addressBook);

    Result updateAddressBook(AddressBook addressBook);
}
