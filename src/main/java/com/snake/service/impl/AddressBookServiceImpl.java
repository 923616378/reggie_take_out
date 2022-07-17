package com.snake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.pojo.AddressBook;
import com.snake.service.AddressBookService;
import com.snake.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-07-16 17:21:05
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




