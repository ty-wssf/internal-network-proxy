
    alter table port_mapping add column NOP_TENANT_ID VARCHAR2(32) DEFAULT '0' NOT NULL;

alter table port_mapping drop constraint PK_port_mapping;
alter table port_mapping add constraint PK_port_mapping primary key (NOP_TENANT_ID, ID);


