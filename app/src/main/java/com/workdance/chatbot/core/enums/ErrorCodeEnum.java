package com.workdance.chatbot.core.enums;


import com.workdance.chatbot.core.error.ErrorLevels;
import com.workdance.chatbot.core.error.ErrorTypes;
import com.workdance.chatbot.core.util.StringUtil;

public enum ErrorCodeEnum {

    // ============================= 公共类错误码[000-299]==================================

    /**
     * 状态异常
     */
    UNKNOWN_ERROR("999", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "未知异常"),

    USER_NOT_LOGIN("001", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "用户未登录"),

    MER_ARGUMENT_ILLEGAL("002", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "参数错误"),

    SYSTEM_ERROR("003", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "系统异常"),

    THIRD_SYSTEM_ERROR("004", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "三方系统异常"),

    NETWORK_ERROR("005", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "网络异常");


    /**
     * 常量代表固定标识
     */
    protected final static String AE = "AE";

    /**
     * 常量代表版本号
     */
    protected final static String VERSION = "0";

    /**
     * 错误信息code
     */
    private final String errDtlCode;

    /**
     * 错误级别
     */
    private final String level;

    /**
     * 错误类型
     */
    private final String type;

    /**
     * 描述说明
     */
    private final String desc;

    /**
     * 私有的构造函数
     *
     * @param errDtlCode 错误场景
     * @param levels     错误级别
     * @param type       错误类型
     * @param desc       错误描述
     */
    private ErrorCodeEnum(String errDtlCode, String levels, String type, String desc) {

        this.errDtlCode = errDtlCode;
        this.level = levels;
        this.type = type;
        this.desc = desc;
    }

    /**
     * 私有的构造函数
     *
     * @param errDtlCode 错误场景
     * @param desc       错误描述
     */
    private ErrorCodeEnum(String errDtlCode, String desc) {

        this.errDtlCode = errDtlCode;
        this.level = ErrorLevels.ERROR;
        this.type = ErrorTypes.BIZ;
        this.desc = desc;
    }

//    public ErrorCode toErrorCode() {
//        return new ErrorCode(ErrorCodeEnum.getVersion(), getLevel(), getType(),
//                MrchmngConstants.MERCHANT_ERROR_SCENE, getErrDtlCode(), ErrorCodeEnum.getAE());
//    }

    // ~~ 使用方法

    /**
     * Getter method for property <tt>mer</tt>.
     *
     * @return property value of MER
     */
    public static String getAE() {
        return AE;
    }

    /**
     * Getter method for property <tt>version</tt>.
     *
     * @return property value of VERSION
     */
    public static String getVersion() {
        return ErrorCodeEnum.VERSION;
    }

    /**
     * Getter method for property <tt>level</tt>.
     *
     * @return property value of level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     *
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Getter method for property <tt>errDtlCode</tt>.
     *
     * @return property value of errDtlCode
     */
    public String getErrDtlCode() {
        return errDtlCode;
    }

    /**
     * 根据编码查询枚举。
     *
     * @param code 编码。
     * @return 枚举。
     */
    public static ErrorCodeEnum getByErrDtlCode(String code) {
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
            if (StringUtil.equals(code, value.getErrDtlCode())) {
                return value;
            }
        }
        return null;
    }

    public static ErrorCodeEnum getByDesc(String desc) {
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
            if (StringUtil.equals(desc, value.getDesc())) {
                return value;
            }
        }
        return null;
    }

}
