package network.proxy.dao.entity._gen;

import io.nop.orm.model.IEntityModel;
import io.nop.orm.support.DynamicOrmEntity;
import io.nop.orm.support.OrmEntitySet; //NOPMD - suppressed UnusedImports - Auto Gen Code
import io.nop.orm.IOrmEntitySet; //NOPMD - suppressed UnusedImports - Auto Gen Code
import io.nop.api.core.convert.ConvertHelper;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

import network.proxy.dao.entity.PortMapping;

// tell cpd to start ignoring code - CPD-OFF
/**
 *  端口映射表: port_mapping
 */
@SuppressWarnings({"PMD.UselessOverridingMethod","PMD.UnusedLocalVariable","java:S3008","java:S1602","java:S1128","java:S1161",
        "PMD.UnnecessaryFullyQualifiedName","PMD.EmptyControlStatement","java:S116","java:S115","java:S101","java:S3776"})
public class _PortMapping extends DynamicOrmEntity{
    
    /* Id: ID INTEGER */
    public static final String PROP_NAME_id = "id";
    public static final int PROP_ID_id = 1;
    
    /* 服务端端口: SERVER_PORT VARCHAR */
    public static final String PROP_NAME_serverPort = "serverPort";
    public static final int PROP_ID_serverPort = 2;
    
    /* 协议: PROTOCOL VARCHAR */
    public static final String PROP_NAME_protocol = "protocol";
    public static final int PROP_ID_protocol = 3;
    
    /* 客户端IP: CLIENT_IP VARCHAR */
    public static final String PROP_NAME_clientIp = "clientIp";
    public static final int PROP_ID_clientIp = 4;
    
    /* 客户端端口: CLIENT_PORT INTEGER */
    public static final String PROP_NAME_clientPort = "clientPort";
    public static final int PROP_ID_clientPort = 5;
    
    /* 是否启动: ENABLED BOOLEAN */
    public static final String PROP_NAME_enabled = "enabled";
    public static final int PROP_ID_enabled = 9;
    
    /* 创建时间: ADD_TIME DATETIME */
    public static final String PROP_NAME_addTime = "addTime";
    public static final int PROP_ID_addTime = 10;
    
    /* 更新时间: UPDATE_TIME DATETIME */
    public static final String PROP_NAME_updateTime = "updateTime";
    public static final int PROP_ID_updateTime = 11;
    
    /* 逻辑删除: DELETED BOOLEAN */
    public static final String PROP_NAME_deleted = "deleted";
    public static final int PROP_ID_deleted = 12;
    

    private static int _PROP_ID_BOUND = 13;

    

    protected static final List<String> PK_PROP_NAMES = Arrays.asList(PROP_NAME_id);
    protected static final int[] PK_PROP_IDS = new int[]{PROP_ID_id};

    private static final String[] PROP_ID_TO_NAME = new String[13];
    private static final Map<String,Integer> PROP_NAME_TO_ID = new HashMap<>();
    static{
      
          PROP_ID_TO_NAME[PROP_ID_id] = PROP_NAME_id;
          PROP_NAME_TO_ID.put(PROP_NAME_id, PROP_ID_id);
      
          PROP_ID_TO_NAME[PROP_ID_serverPort] = PROP_NAME_serverPort;
          PROP_NAME_TO_ID.put(PROP_NAME_serverPort, PROP_ID_serverPort);
      
          PROP_ID_TO_NAME[PROP_ID_protocol] = PROP_NAME_protocol;
          PROP_NAME_TO_ID.put(PROP_NAME_protocol, PROP_ID_protocol);
      
          PROP_ID_TO_NAME[PROP_ID_clientIp] = PROP_NAME_clientIp;
          PROP_NAME_TO_ID.put(PROP_NAME_clientIp, PROP_ID_clientIp);
      
          PROP_ID_TO_NAME[PROP_ID_clientPort] = PROP_NAME_clientPort;
          PROP_NAME_TO_ID.put(PROP_NAME_clientPort, PROP_ID_clientPort);
      
          PROP_ID_TO_NAME[PROP_ID_enabled] = PROP_NAME_enabled;
          PROP_NAME_TO_ID.put(PROP_NAME_enabled, PROP_ID_enabled);
      
          PROP_ID_TO_NAME[PROP_ID_addTime] = PROP_NAME_addTime;
          PROP_NAME_TO_ID.put(PROP_NAME_addTime, PROP_ID_addTime);
      
          PROP_ID_TO_NAME[PROP_ID_updateTime] = PROP_NAME_updateTime;
          PROP_NAME_TO_ID.put(PROP_NAME_updateTime, PROP_ID_updateTime);
      
          PROP_ID_TO_NAME[PROP_ID_deleted] = PROP_NAME_deleted;
          PROP_NAME_TO_ID.put(PROP_NAME_deleted, PROP_ID_deleted);
      
    }

    
    /* Id: ID */
    private java.lang.Integer _id;
    
    /* 服务端端口: SERVER_PORT */
    private java.lang.String _serverPort;
    
    /* 协议: PROTOCOL */
    private java.lang.String _protocol;
    
    /* 客户端IP: CLIENT_IP */
    private java.lang.String _clientIp;
    
    /* 客户端端口: CLIENT_PORT */
    private java.lang.Integer _clientPort;
    
    /* 是否启动: ENABLED */
    private java.lang.Boolean _enabled;
    
    /* 创建时间: ADD_TIME */
    private java.time.LocalDateTime _addTime;
    
    /* 更新时间: UPDATE_TIME */
    private java.time.LocalDateTime _updateTime;
    
    /* 逻辑删除: DELETED */
    private java.lang.Boolean _deleted;
    

    public _PortMapping(){
        // for debug
    }

    protected PortMapping newInstance(){
        PortMapping entity = new PortMapping();
        entity.orm_attach(orm_enhancer());
        entity.orm_entityModel(orm_entityModel());
        return entity;
    }

    @Override
    public PortMapping cloneInstance() {
        PortMapping entity = newInstance();
        orm_forEachInitedProp((value, propId) -> {
            entity.orm_propValue(propId,value);
        });
        return entity;
    }

    @Override
    public String orm_entityName() {
      // 如果存在实体模型对象，则以模型对象上的设置为准
      IEntityModel entityModel = orm_entityModel();
      if(entityModel != null)
          return entityModel.getName();
      return "network.proxy.dao.entity.PortMapping";
    }

    @Override
    public int orm_propIdBound(){
      IEntityModel entityModel = orm_entityModel();
      if(entityModel != null)
          return entityModel.getPropIdBound();
      return _PROP_ID_BOUND;
    }

    @Override
    public Object orm_id() {
    
        return buildSimpleId(PROP_ID_id);
     
    }

    @Override
    public boolean orm_isPrimary(int propId) {
        
            return propId == PROP_ID_id;
          
    }

    @Override
    public String orm_propName(int propId) {
        if(propId >= PROP_ID_TO_NAME.length)
            return super.orm_propName(propId);
        String propName = PROP_ID_TO_NAME[propId];
        if(propName == null)
           return super.orm_propName(propId);
        return propName;
    }

    @Override
    public int orm_propId(String propName) {
        Integer propId = PROP_NAME_TO_ID.get(propName);
        if(propId == null)
            return super.orm_propId(propName);
        return propId;
    }

    @Override
    public Object orm_propValue(int propId) {
        switch(propId){
        
            case PROP_ID_id:
               return getId();
        
            case PROP_ID_serverPort:
               return getServerPort();
        
            case PROP_ID_protocol:
               return getProtocol();
        
            case PROP_ID_clientIp:
               return getClientIp();
        
            case PROP_ID_clientPort:
               return getClientPort();
        
            case PROP_ID_enabled:
               return getEnabled();
        
            case PROP_ID_addTime:
               return getAddTime();
        
            case PROP_ID_updateTime:
               return getUpdateTime();
        
            case PROP_ID_deleted:
               return getDeleted();
        
           default:
              return super.orm_propValue(propId);
        }
    }

    

    @Override
    public void orm_propValue(int propId, Object value){
        switch(propId){
        
            case PROP_ID_id:{
               java.lang.Integer typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toInteger(value,
                       err-> newTypeConversionError(PROP_NAME_id));
               }
               setId(typedValue);
               break;
            }
        
            case PROP_ID_serverPort:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_serverPort));
               }
               setServerPort(typedValue);
               break;
            }
        
            case PROP_ID_protocol:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_protocol));
               }
               setProtocol(typedValue);
               break;
            }
        
            case PROP_ID_clientIp:{
               java.lang.String typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toString(value,
                       err-> newTypeConversionError(PROP_NAME_clientIp));
               }
               setClientIp(typedValue);
               break;
            }
        
            case PROP_ID_clientPort:{
               java.lang.Integer typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toInteger(value,
                       err-> newTypeConversionError(PROP_NAME_clientPort));
               }
               setClientPort(typedValue);
               break;
            }
        
            case PROP_ID_enabled:{
               java.lang.Boolean typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toBoolean(value,
                       err-> newTypeConversionError(PROP_NAME_enabled));
               }
               setEnabled(typedValue);
               break;
            }
        
            case PROP_ID_addTime:{
               java.time.LocalDateTime typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toLocalDateTime(value,
                       err-> newTypeConversionError(PROP_NAME_addTime));
               }
               setAddTime(typedValue);
               break;
            }
        
            case PROP_ID_updateTime:{
               java.time.LocalDateTime typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toLocalDateTime(value,
                       err-> newTypeConversionError(PROP_NAME_updateTime));
               }
               setUpdateTime(typedValue);
               break;
            }
        
            case PROP_ID_deleted:{
               java.lang.Boolean typedValue = null;
               if(value != null){
                   typedValue = ConvertHelper.toBoolean(value,
                       err-> newTypeConversionError(PROP_NAME_deleted));
               }
               setDeleted(typedValue);
               break;
            }
        
           default:
              super.orm_propValue(propId,value);
        }
    }

    @Override
    public void orm_internalSet(int propId, Object value) {
        switch(propId){
        
            case PROP_ID_id:{
               onInitProp(propId);
               this._id = (java.lang.Integer)value;
               orm_id(); // 如果是设置主键字段，则触发watcher
               break;
            }
        
            case PROP_ID_serverPort:{
               onInitProp(propId);
               this._serverPort = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_protocol:{
               onInitProp(propId);
               this._protocol = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_clientIp:{
               onInitProp(propId);
               this._clientIp = (java.lang.String)value;
               
               break;
            }
        
            case PROP_ID_clientPort:{
               onInitProp(propId);
               this._clientPort = (java.lang.Integer)value;
               
               break;
            }
        
            case PROP_ID_enabled:{
               onInitProp(propId);
               this._enabled = (java.lang.Boolean)value;
               
               break;
            }
        
            case PROP_ID_addTime:{
               onInitProp(propId);
               this._addTime = (java.time.LocalDateTime)value;
               
               break;
            }
        
            case PROP_ID_updateTime:{
               onInitProp(propId);
               this._updateTime = (java.time.LocalDateTime)value;
               
               break;
            }
        
            case PROP_ID_deleted:{
               onInitProp(propId);
               this._deleted = (java.lang.Boolean)value;
               
               break;
            }
        
           default:
              super.orm_internalSet(propId,value);
        }
    }

    
    /**
     * Id: ID
     */
    public java.lang.Integer getId(){
         onPropGet(PROP_ID_id);
         return _id;
    }

    /**
     * Id: ID
     */
    public void setId(java.lang.Integer value){
        if(onPropSet(PROP_ID_id,value)){
            this._id = value;
            internalClearRefs(PROP_ID_id);
            orm_id();
        }
    }
    
    /**
     * 服务端端口: SERVER_PORT
     */
    public java.lang.String getServerPort(){
         onPropGet(PROP_ID_serverPort);
         return _serverPort;
    }

    /**
     * 服务端端口: SERVER_PORT
     */
    public void setServerPort(java.lang.String value){
        if(onPropSet(PROP_ID_serverPort,value)){
            this._serverPort = value;
            internalClearRefs(PROP_ID_serverPort);
            
        }
    }
    
    /**
     * 协议: PROTOCOL
     */
    public java.lang.String getProtocol(){
         onPropGet(PROP_ID_protocol);
         return _protocol;
    }

    /**
     * 协议: PROTOCOL
     */
    public void setProtocol(java.lang.String value){
        if(onPropSet(PROP_ID_protocol,value)){
            this._protocol = value;
            internalClearRefs(PROP_ID_protocol);
            
        }
    }
    
    /**
     * 客户端IP: CLIENT_IP
     */
    public java.lang.String getClientIp(){
         onPropGet(PROP_ID_clientIp);
         return _clientIp;
    }

    /**
     * 客户端IP: CLIENT_IP
     */
    public void setClientIp(java.lang.String value){
        if(onPropSet(PROP_ID_clientIp,value)){
            this._clientIp = value;
            internalClearRefs(PROP_ID_clientIp);
            
        }
    }
    
    /**
     * 客户端端口: CLIENT_PORT
     */
    public java.lang.Integer getClientPort(){
         onPropGet(PROP_ID_clientPort);
         return _clientPort;
    }

    /**
     * 客户端端口: CLIENT_PORT
     */
    public void setClientPort(java.lang.Integer value){
        if(onPropSet(PROP_ID_clientPort,value)){
            this._clientPort = value;
            internalClearRefs(PROP_ID_clientPort);
            
        }
    }
    
    /**
     * 是否启动: ENABLED
     */
    public java.lang.Boolean getEnabled(){
         onPropGet(PROP_ID_enabled);
         return _enabled;
    }

    /**
     * 是否启动: ENABLED
     */
    public void setEnabled(java.lang.Boolean value){
        if(onPropSet(PROP_ID_enabled,value)){
            this._enabled = value;
            internalClearRefs(PROP_ID_enabled);
            
        }
    }
    
    /**
     * 创建时间: ADD_TIME
     */
    public java.time.LocalDateTime getAddTime(){
         onPropGet(PROP_ID_addTime);
         return _addTime;
    }

    /**
     * 创建时间: ADD_TIME
     */
    public void setAddTime(java.time.LocalDateTime value){
        if(onPropSet(PROP_ID_addTime,value)){
            this._addTime = value;
            internalClearRefs(PROP_ID_addTime);
            
        }
    }
    
    /**
     * 更新时间: UPDATE_TIME
     */
    public java.time.LocalDateTime getUpdateTime(){
         onPropGet(PROP_ID_updateTime);
         return _updateTime;
    }

    /**
     * 更新时间: UPDATE_TIME
     */
    public void setUpdateTime(java.time.LocalDateTime value){
        if(onPropSet(PROP_ID_updateTime,value)){
            this._updateTime = value;
            internalClearRefs(PROP_ID_updateTime);
            
        }
    }
    
    /**
     * 逻辑删除: DELETED
     */
    public java.lang.Boolean getDeleted(){
         onPropGet(PROP_ID_deleted);
         return _deleted;
    }

    /**
     * 逻辑删除: DELETED
     */
    public void setDeleted(java.lang.Boolean value){
        if(onPropSet(PROP_ID_deleted,value)){
            this._deleted = value;
            internalClearRefs(PROP_ID_deleted);
            
        }
    }
    
}
// resume CPD analysis - CPD-ON
