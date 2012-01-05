drop procedure if exists ui_get_smtp_server;
delimiter $$
create procedure ui_get_smtp_server(i_ssrv_id int(10))
 main_sql:
  begin
    select ssrv_id,
           server_name,
           host,
           port,
           username,
           password,
           from_address
      from smtp_servers
      where ssrv_id = i_ssrv_id;
  end
$$
delimiter ;
call save_routine_information('ui_get_smtp_server',
                              concat_ws(',',
                                        'ssrv_id int',
                                        'server_name varchar',
                                        'host varchar',
                                        'port int',
                                        'username varchar',
                                        'password varchar',
                                        'from_address varchar'
                              )
     );