drop procedure if exists ui_get_mapped_group_actions_list_count;
delimiter $$
create procedure ui_get_mapped_group_actions_list_count(i_grop_id int(10))
 main_sql:
  begin
    call int_group_actions_list_count(i_grop_id, "M");
  end
$$
delimiter ;
call save_routine_information('ui_get_mapped_group_actions_list_count',
                              concat_ws(',', 'records_count int')
     );