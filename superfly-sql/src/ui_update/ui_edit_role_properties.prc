drop procedure if exists ui_edit_role_properties;
delimiter $$
create procedure ui_edit_role_properties(i_role_id int(10),
                                         i_role_name varchar(32),
                                         i_principal_name varchar(32),
                                         i_ssys_id int(10) -- java compatibility
)
 main_sql:
  begin
    update roles
       set role_name        = coalesce(i_role_name, role_name),
           principal_name   = coalesce(i_principal_name, principal_name)
     where role_id = i_role_id;

    commit;

    select 'OK' status, null error_message;
  end
$$
delimiter ;
call save_routine_information('ui_edit_role_properties',
                              concat_ws(',',
                                        'status varchar',
                                        'error_message varchar'
                              )
     );
