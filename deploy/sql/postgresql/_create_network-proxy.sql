
CREATE TABLE proxy_clients(
  ID INT4 NOT NULL ,
  NAME VARCHAR(255) NOT NULL ,
  ENABLED BOOLEAN NOT NULL  default 'false' ,
  ADD_TIME TIMESTAMP  ,
  UPDATE_TIME TIMESTAMP  ,
  DELETED BOOLEAN  ,
  constraint UK_PROXY_CLIENTS_NAME unique (NAME),
  constraint PK_proxy_clients primary key (ID)
);

CREATE TABLE proxy_port_mapping(
  ID INT4 NOT NULL ,
  CLIENT_ID INT4 NOT NULL ,
  SERVER_PORT INT4 NOT NULL ,
  PROTOCOL VARCHAR(255) NOT NULL ,
  CLIENT_IP VARCHAR(255) NOT NULL ,
  CLIENT_PORT INT4 NOT NULL ,
  ENABLED BOOLEAN NOT NULL  default 'false' ,
  ADD_TIME TIMESTAMP  ,
  UPDATE_TIME TIMESTAMP  ,
  DELETED BOOLEAN  ,
  constraint UK_PORT_MAPPING_SERVER_PORT unique (SERVER_PORT),
  constraint PK_proxy_port_mapping primary key (ID)
);


      COMMENT ON TABLE proxy_clients IS '客户端';
                
      COMMENT ON COLUMN proxy_clients.ID IS 'Id';
                    
      COMMENT ON COLUMN proxy_clients.NAME IS '名称';
                    
      COMMENT ON COLUMN proxy_clients.ENABLED IS '是否启用';
                    
      COMMENT ON COLUMN proxy_clients.ADD_TIME IS '创建时间';
                    
      COMMENT ON COLUMN proxy_clients.UPDATE_TIME IS '更新时间';
                    
      COMMENT ON COLUMN proxy_clients.DELETED IS '逻辑删除';
                    
      COMMENT ON TABLE proxy_port_mapping IS '端口映射';
                
      COMMENT ON COLUMN proxy_port_mapping.ID IS 'Id';
                    
      COMMENT ON COLUMN proxy_port_mapping.CLIENT_ID IS '客户端';
                    
      COMMENT ON COLUMN proxy_port_mapping.SERVER_PORT IS '服务端端口';
                    
      COMMENT ON COLUMN proxy_port_mapping.PROTOCOL IS '协议';
                    
      COMMENT ON COLUMN proxy_port_mapping.CLIENT_IP IS '客户端IP';
                    
      COMMENT ON COLUMN proxy_port_mapping.CLIENT_PORT IS '客户端端口';
                    
      COMMENT ON COLUMN proxy_port_mapping.ENABLED IS '是否启动';
                    
      COMMENT ON COLUMN proxy_port_mapping.ADD_TIME IS '创建时间';
                    
      COMMENT ON COLUMN proxy_port_mapping.UPDATE_TIME IS '更新时间';
                    
      COMMENT ON COLUMN proxy_port_mapping.DELETED IS '逻辑删除';
                    
