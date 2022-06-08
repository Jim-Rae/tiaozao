# tiaozao
tiaozaoH5后台


## 数据库

![image](https://user-images.githubusercontent.com/25401068/172663719-92a17089-3bfa-4bc3-8119-921c9f6d0415.png)


```
create database tiaozao DEFAULT CHARACTER SET utf8;

use tiaozao;
CREATE TABLE userinfo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usrname VARCHAR(20),
    pass VARCHAR(20),
    nickname VARCHAR(20),
    sex VARCHAR(5),
    birthday VARCHAR(20),
    school VARCHAR(30),
    address VARCHAR(200),
    image VARCHAR(100) default 'http://120.79.234.43/TiaoZao/Image?imageUrl=default.png',
    introduce VARCHAR(50),
    ownedItem VARCHAR(200),
    collectedItem VARCHAR(200),
    shoppingCart VARCHAR(200),
    purchasedItem VARCHAR(200),
    feedBack VARCHAR(200)
)  DEFAULT CHARSET=UTF8;

create table iteminfo(
	id INT PRIMARY KEY AUTO_INCREMENT,
	itemname VARCHAR(20),
	introduce VARCHAR(100),
	image VARCHAR(500),
	price VARCHAR(10),
	collectedTime int default 0,
    message VARCHAR(300),
    ownerName VARCHAR(10),
    sold VARCHAR(10) default 'false',
    category VARCHAR(20),
    uploadTime VARCHAR(20)
)  DEFAULT CHARSET=UTF8;

SET SQL_SAFE_UPDATES=0;
```
