-- Tips:
-- 1. 在使用这个insert.sql时,需要把@now_time换成时间字符串 '2017-6-12 18:18:18'


-- -------------------------------------------------------------------------- 角色,权限部分  ----------------------------------------------------------------------------

--
-- 角色表插入
--
SET @now_time = '2018-4-1 18:18:18';
-- INSERT INTO role(name, description, gmt_create, gmt_modified) VALUES ('all_user','所有用户',@now_time,@now_time);
-- INSERT INTO role(name, description, gmt_create, gmt_modified) VALUES ('normal_user','普通用户',@now_time,@now_time);
-- INSERT INTO role(name, description, gmt_create, gmt_modified) VALUES ('root','最强管理员',@now_time,@now_time);
--
--
-- --
-- -- 权限插入
-- --
-- -- user
-- INSERT INTO permission(name,description,gmt_create,gmt_modified) VALUES('/user|POST','用户注册',@now_time,@now_time);
-- INSERT INTO permission(name,description,gmt_create,gmt_modified) VALUES('/user|GET','用户登录',@now_time,@now_time);
-- INSERT INTO permission(name,description,gmt_create,gmt_modified) VALUES('/account/password|POST','用户密码修改',@now_time,@now_time);
-- INSERT INTO permission(name,description,gmt_create,gmt_modified) VALUES('/user/info|GET','用户信息查询',@now_time,@now_time);
-- INSERT INTO permission(name,description,gmt_create,gmt_modified) VALUES('/user/info|POST','个人用户信息更新',@now_time,@now_time);
-- INSERT INTO permission(name,description,gmt_create,gmt_modified) VALUES('/user/info/avatar|POST','个人用户头像更新',@now_time,@now_time);


--
-- 角色权限关系插入
--
-- 所有用户,主要是存放那些没有不需要登录的权限.
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,1,@now_time,@now_time);
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,2,@now_time,@now_time);
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,4,@now_time,@now_time);
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,5,@now_time,@now_time);
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,7,@now_time,@now_time);


-- 普通用户
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,3,@now_time,@now_time);
-- INSERT INTO r_role_permission(role_id, permission_id, gmt_create,gmt_modified) VALUES(1,6,@now_time,@now_time);


-- 插入默认分类
INSERT INTO groups VALUES('默认分类','default', 0, 1,@now_time,@now_time);





--
--  省份信息插入
--
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (1,'江苏',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (2,'北京',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (3,'上海',@now_time,@now_time);

INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (4,'山东',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (5,'山西',@now_time,@now_time);

INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (6,'河南',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (7,'河北',@now_time,@now_time);

INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (8,'广西',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (9,'广东',@now_time,@now_time);

INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (10,'内蒙古',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (11,'云南',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (12,'新疆',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (13,'西藏',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (14,'陕西',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (15,'浙江',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (16,'香港',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (17,'澳门',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (18,'湖南',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (19,'台湾',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (20,'重庆',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (21,'福建',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (22,'天津',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (23,'四川',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (24,'安徽',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (25,'海南',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (26,'湖北',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (27,'辽宁',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (28,'黑龙江',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (29,'吉林',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (30,'贵州',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (31,'甘肃',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (32,'青海',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (33,'宁夏',@now_time,@now_time);
INSERT INTO province_info(id,name,gmt_create,gmt_modified) VALUES (34,'江西',@now_time,@now_time);

