drop procedure if exists ui_change_role_actions;
delimiter $$
create procedure ui_change_role_actions(i_role_id int(10),
                                        i_actn_list_link text,
                                        i_actn_list_unlink text
)
 main_sql:
  begin
    if i_actn_list_link is not null and i_actn_list_link <> '' then
      call int_link_role_actions(i_role_id, i_actn_list_link);
    end if;

    if i_actn_list_unlink is not null and i_actn_list_unlink <> '' then
      call int_unlink_role_actions(i_role_id, i_actn_list_unlink);
    end if;

    call ui_check_expired_sessions(null, i_role_id, null, null);

    select 'OK' status, null error_message;
  end
$$
delimiter ;
call save_routine_information('ui_change_role_actions',
                              concat_ws(',',
                                        'status varchar',
                                        'error_message varchar'
                              )
     );