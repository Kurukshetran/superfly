drop procedure if exists ui_change_role_groups;
delimiter $$
create procedure ui_change_role_groups(i_role_id int(10),
                                       i_grop_list_link text,
                                       i_grop_list_unlink text
)
 main_sql:
  begin
    if i_grop_list_link is not null then
      call int_link_role_groups(i_role_id, i_grop_list_link);
    end if;

    if i_grop_list_unlink is not null then
      call int_unlink_role_groups(i_role_id, i_grop_list_unlink);
    end if;

    commit;

    select 'OK' status, null error_message;
  end
$$
delimiter ;
call save_routine_information('ui_change_role_groups',
                              concat_ws(',',
                                        'status varchar',
                                        'error_message varchar'
                              )
     );