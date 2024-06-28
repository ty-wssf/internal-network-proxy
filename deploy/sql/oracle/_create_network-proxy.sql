
CREATE TABLE port_mapping(
  ID INTEGER NOT NULL ,
  SERVER_PORT INTEGER NOT NULL ,
  PROTOCOL VARCHAR2(255) NOT NULL ,
  CLIENT_IP VARCHAR2(255) NOT NULL ,
  CLIENT_PORT INTEGER NOT NULL ,
  ENABLED CHAR(1) NOT NULL  default 'false' ,
  ADD_TIME DATE  ,
  UPDATE_TIME DATE  ,
  DELETED CHAR(1)  ,
  constraint UK_PORT_MAPPING_SERVER_PORT unique (SERVER_PORT),
  constraint PK_port_mapping primary key (ID)
);


      COMMENT ON TABLE port_mapping IS '端口映射表';
                
      COMMENT ON COLUMN port_mapping.ID IS 'Id';
                    
      COMMENT ON COLUMN port_mapping.SERVER_PORT IS '服务端端口';
                    
      COMMENT ON COLUMN port_mapping.PROTOCOL IS '协议';
                    
      COMMENT ON COLUMN port_mapping.CLIENT_IP IS '客户端IP';
                    
      COMMENT ON COLUMN port_mapping.CLIENT_PORT IS '客户端端口';
                    
      COMMENT ON COLUMN port_mapping.ENABLED IS '是否启动';
                    
      COMMENT ON COLUMN port_mapping.ADD_TIME IS '创建时间';
                    
      COMMENT ON COLUMN port_mapping.UPDATE_TIME IS '更新时间';
                    
      COMMENT ON COLUMN port_mapping.DELETED IS '逻辑删除';
                    
