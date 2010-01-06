drop table if exists mysql_routines_return_arguments;

create table mysql_routines_return_arguments (
  routine_name varchar(128) not null,
  argument_name varchar(128) not null,
  argument_type varchar(128) not null,
	ordinal_number int(10),
  unique key unq_mysql_routines_arguments_name_type (routine_name, argument_name)
) engine=innodb;

\. save_routine_information.sql

\. run_install_command.sql

\. uninject.sql

\. get/get_user_actions.sql

\. get/get_procedures_resultset.prc

\. create/create_collections.prc

\. int/int_users_list.prc

\. int/int_actions_list.prc

\. int/int_link_group_actions.prc

\. int/int_groups_list.prc

\. int/int_unlink_group_actions.prc

\. int/int_link_role_groups.prc

\. int/int_unlink_role_groups.prc

\. int/int_link_role_actions.prc

\. int/int_unlink_role_actions.prc

\. int/int_roles_list.prc

\. int/int_link_user_roles.prc

\. int/int_unlink_user_roles.prc

\. int/int_group_actions_list.prc

\. int/int_group_actions_list_count.prc

\. int/int_role_groups_list.prc

\. int/int_role_groups_list_count.prc

\. int/int_role_actions_list.prc

\. int/int_role_actions_list_count.prc

\. int/int_user_roles_list.prc

\. int/int_user_roles_list_count.prc

\. ui_copy/ui_copy_action_properties.prc

\. ui_filter/ui_filter_subsystems.prc

\. ui_filter/ui_filter_dyn_actions.prc

\. ui_filter/ui_filter_dyn_roles.prc

\. ui_filter/ui_filter_dyn_groups.prc

\. ui_create/ui_create_group.prc

\. ui_create/ui_clone_group.prc

\. ui_create/ui_create_subsystem.prc

\. ui_create/ui_create_role.prc

\. ui_create/ui_create_user.prc

\. ui_create/ui_clone_user.prc

\. ui_delete/ui_delete_group.prc

\. ui_delete/ui_delete_role.prc

\. ui_delete/ui_delete_subsystem.prc

\. ui_delete/ui_delete_user.prc

\. save/save_actions.prc

\. ui_get/ui_get_groups_list.prc

\. ui_get/ui_get_mapped_group_actions_list.prc

\. ui_get/ui_get_mapped_group_actions_list_count.prc

\. ui_get/ui_get_unmapped_group_actions_list.prc

\. ui_get/ui_get_unmapped_group_actions_list_count.prc

\. ui_get/ui_get_all_group_actions_list.prc

\. ui_get/ui_get_all_group_actions_list_count.prc

\. ui_get/ui_get_mapped_role_groups_list.prc

\. ui_get/ui_get_mapped_role_groups_list_count.prc

\. ui_get/ui_get_unmapped_role_groups_list.prc

\. ui_get/ui_get_unmapped_role_groups_list_count.prc

\. ui_get/ui_get_all_role_groups_list.prc

\. ui_get/ui_get_all_role_groups_list_count.prc

\. ui_get/ui_get_mapped_role_actions_list.prc

\. ui_get/ui_get_mapped_role_actions_list_count.prc

\. ui_get/ui_get_unmapped_role_actions_list.prc

\. ui_get/ui_get_unmapped_role_actions_list_count.prc

\. ui_get/ui_get_all_role_actions_list.prc

\. ui_get/ui_get_all_role_actions_list_count.prc

\. ui_get/ui_get_mapped_user_roles_list.prc

\. ui_get/ui_get_mapped_user_roles_list_count.prc

\. ui_get/ui_get_unmapped_user_roles_list.prc

\. ui_get/ui_get_unmapped_user_roles_list_count.prc

\. ui_get/ui_get_all_user_roles_list.prc

\. ui_get/ui_get_all_user_roles_list_count.prc

\. ui_get/ui_get_users_list.prc

\. ui_get/ui_get_users_list_count.prc

\. ui_get/ui_get_actions_list.prc

\. ui_get/ui_get_actions_list_count.prc

\. ui_get/ui_get_subsystems_list.prc

\. ui_get/ui_get_groups_list_count.prc

\. ui_get/ui_get_roles_list.prc

\. ui_get/ui_get_roles_list_count.prc

\. ui_get/ui_get_user.prc

\. ui_get/ui_get_action.prc

\. ui_get/ui_get_subsystem.prc

\. ui_get/ui_get_role.prc

\. ui_get/ui_get_group.prc

\. ui_update/ui_lock_user.prc

\. ui_update/ui_unlock_user.prc

\. ui_update/ui_update_user.prc

\. ui_update/ui_change_actions_log_level.prc

\. ui_update/ui_change_group_actions.prc

\. ui_update/ui_edit_group_properties.prc

\. ui_update/ui_change_role_groups.prc

\. ui_update/ui_change_role_actions.prc

\. ui_update/ui_edit_role_properties.prc

\. ui_update/ui_edit_subsystem_properties.prc

\. ui_update/ui_change_user_roles.prc
