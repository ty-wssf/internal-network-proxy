
    alter table port_mapping add column NOP_TENANT_ID VARCHAR2(32) DEFAULT '0' NOT NULL;

alter table port_mapping drop constraint PK_port_mapping;
alter table port_mapping add constraint PK_port_mapping primary key (NOP_TENANT_ID, ID);

alter table port_mapping drop constraint UK_PORT_MAPPING_SERVER_PORT;
alter table port_mapping add constraint UK_PORT_MAPPING_SERVER_PORT
                     unique (NOP_TENANT_ID,SERVER_PORT);

                
