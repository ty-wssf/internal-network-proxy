
    alter table port_mapping add column NOP_TENANT_ID VARCHAR(32) DEFAULT '0' NOT NULL;

alter table port_mapping drop primary key;
alter table port_mapping add primary key (NOP_TENANT_ID, ID);


