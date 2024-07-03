/**
 * @description: Request result set
 */
export enum ResultEnum {
  SUCCESS = 0,
  ERROR = 1,
  TIMEOUT = 401,
  TYPE = 'success',
}

/**
 * @description: request method
 */
export enum RequestEnum {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
}

/**
 * @description:  contentTyp
 */
export enum ContentTypeEnum {
  // json
  JSON = 'application/json;charset=UTF-8',
  // form-data qs
  FORM_URLENCODED = 'application/x-www-form-urlencoded;charset=UTF-8',
  // form-data  upload
  FORM_DATA = 'multipart/form-data;charset=UTF-8',
}

/**
 * 请求header
 * @description:  contentTyp
 */
export enum ConfigEnum {
  // TOKEN
  TOKEN = 'x-access-token',
  // TIMESTAMP
  TIMESTAMP = 'x-timestamp',
  // Sign
  Sign = 'x-sign',
  // 租户id
  TENANT_ID = 'nop-tenant',
  // 版本
  VERSION = 'x-version',
  // 低代码应用ID
  X_LOW_APP_ID = 'nop-app-id',
}
