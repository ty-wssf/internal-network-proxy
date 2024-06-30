
CREATE TABLE proxy_clients(
  ID INTEGER NOT NULL    COMMENT 'Id',
  NAME VARCHAR(255) NOT NULL    COMMENT '名称',
  ENABLED BOOLEAN NOT NULL    COMMENT '是否启用',
  ADD_TIME DATETIME NULL    COMMENT '创建时间',
  UPDATE_TIME DATETIME NULL    COMMENT '更新时间',
  DELETED BOOLEAN NULL    COMMENT '逻辑删除',
  constraint UK_PROXY_CLIENTS_NAME unique (NAME),
  constraint PK_proxy_clients primary key (ID)
);

CREATE TABLE proxy_port_mapping(
  ID INTEGER NOT NULL    COMMENT 'Id',
  CLIENT_ID INTEGER NOT NULL    COMMENT '客户端',
  SERVER_PORT INTEGER NOT NULL    COMMENT '服务端端口',
  PROTOCOL VARCHAR(255) NOT NULL    COMMENT '协议',
  CLIENT_IP VARCHAR(255) NOT NULL    COMMENT '客户端IP',
  CLIENT_PORT INTEGER NOT NULL    COMMENT '客户端端口',
  ENABLED BOOLEAN NOT NULL    COMMENT '是否启动',
  ADD_TIME DATETIME NULL    COMMENT '创建时间',
  UPDATE_TIME DATETIME NULL    COMMENT '更新时间',
  DELETED BOOLEAN NULL    COMMENT '逻辑删除',
  constraint UK_PORT_MAPPING_SERVER_PORT unique (SERVER_PORT),
  constraint PK_proxy_port_mapping primary key (ID)
);


   ALTER TABLE proxy_clients COMMENT '客户端';
                
   ALTER TABLE proxy_port_mapping COMMENT '端口映射';
                
