package com.snake.mapper;

import com.snake.pojo.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author admin
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-07-16 17:21:05
* @Entity com.snake.pojo.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




