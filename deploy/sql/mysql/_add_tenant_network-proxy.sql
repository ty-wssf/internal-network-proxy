
    alter table proxy_clients add column NOP_TENANT_ID VARCHAR(32) DEFAULT '0' NOT NULL;

alter table proxy_port_mapping add column NOP_TENANT_ID VARCHAR(32) DEFAULT '0' NOT NULL;

alter table proxy_clients drop primary key;
alter table proxy_clients add primary key (NOP_TENANT_ID, ID);

alter table proxy_port_mapping drop primary key;
alter table proxy_port_mapping add primary key (NOP_TENANT_ID, ID);

alter table proxy_clients drop constraint UK_PROXY_CLIENTS_NAME;
alter table proxy_clients add constraint UK_PROXY_CLIENTS_NAME
                     unique (NOP_TENANT_ID,NAME);

                alter table proxy_port_mapping drop constraint UK_PORT_MAPPING_SERVER_PORT;
alter table proxy_port_mapping add constraint UK_PORT_MAPPING_SERVER_PORT
                     unique (NOP_TENANT_ID,SERVER_PORT);

                
