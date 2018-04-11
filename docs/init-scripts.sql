drop table if exists red_packages;

/*==============================================================*/
/* Table: red_packages                                          */
/*==============================================================*/
create table red_packages
(
   pid                  int not null auto_increment comment '红包ID',
   provide_id           int not null comment '提供方ID',
   amount               decimal(16.2) not null comment '红包金额',
   send_date            timestamp not null comment '创建时间',
   sub_total           int not null comment '小红包个数',
   unit_amount           decimal(12) not null comment '小红包金额',
   stock        int not null comment '小红包剩余个数',
   version              int default 0 not null,
   remark               varchar(256) null comment '备注',
   primary key (pid)
);

alter table red_packages comment '红包表';

drop table if exists user_red_packages;

/*==============================================================*/
/* Table: user_red_packages                                     */
/*==============================================================*/
create table user_red_packages
(
   upid                 int not null auto_increment comment '主键',
   pid                  int comment '红包ID',
   customer_id          int not null comment '消费者ID',
   amount               decimal(16,2) not null comment '红包金额',
   grab_date            timestamp not null comment '创建时间',
   remark               varchar(256) comment '备注',
   primary key (upid)
);

alter table user_red_packages comment '用户红包表';

alter table user_red_packages add constraint FK_pid foreign key (pid)
      references red_packages (pid) on delete restrict on update restrict;



insert into red_packages(provide_id, amount, send_date, sub_total, unit_amount, stock, remark)
   values(1, 200000.00, now(), 20000, 10.00, 20000, "20万元红包，拆分成2万份");


   SELECT * FROM red_packages WHERE pid = 1;
UNION ALL 
SELECT SUM(amount), COUNT(*) FROM user_red_packages WHERE pid = 1;