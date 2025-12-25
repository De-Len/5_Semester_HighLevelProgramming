alter table permissions
    add constraint fk_permission_user
    foreign key (user_id) references users(id);

alter table permissions
    add constraint fk_permission_resource
    foreign key (resource_id) references resources(id);

create index idx_permissions_user on permissions(user_id);
create index idx_permissions_resource on permissions(resource_id);