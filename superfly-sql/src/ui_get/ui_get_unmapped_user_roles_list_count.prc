drop procedure if exists ui_get_unmapped_user_roles_list_count;
delimiter $$
create procedure ui_get_unmapped_user_roles_list_count(i_user_id int(10))
 main_sql:
  begin
    call int_user_roles_list_count(i_user_id, "U");
  end
$$
delimiter ;
call save_routine_information('ui_get_unmapped_user_roles_list_count',
                              concat_ws(',', 'records_count int')
     );