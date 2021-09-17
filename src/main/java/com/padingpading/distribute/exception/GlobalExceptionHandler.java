//package com.padingpading.distribute.exception;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//    /**
//     * 全局异常捕捉处理
//     * @param ex .
//     * @return .
//     */
//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public Result errorHandler(Exception ex) {
//        log.error("全局异常信息:",ex);
//        return Result.error(CodeEnum.SYSTEM_EXCEPTION);
//    }
//
//    /**
//     * 会员异常捕捉处理
//     * @param ex .
//     * @return .
//     */
//    @ResponseBody
//    @ExceptionHandler(value = MemberException.class)
//    public Result memberExceptionHandler(MemberException ex) {
//        log.error("会员异常信息:",ex);
//        Result error = Result.error(CodeEnum.SYSTEM_EXCEPTION);
//        error.setCode(ex.getCode());
//        error.setMessage(ex.getMsg());
//        return error;
//    }
//
//
//    /**
//     * 会员异常捕捉处理
//     * @param ex .
//     * @return .
//     */
//    @ResponseBody
//    @ExceptionHandler(value = MemberRuntimeException.class)
//    public Result memberRuntimeExceptionHandler(MemberRuntimeException ex) {
//        return Result.error(CodeEnum.SYSTEM_EXCEPTION);
//    }
//
//    /**
//     * 参数异常捕捉处理
//     * @param ex .
//     * @return .
//     */
//    @ResponseBody
//    @ExceptionHandler(value = IllegalArgumentException.class)
//    public Result illegalArgumentExceptionHandler(IllegalArgumentException ex) {
//        log.error("参数异常信息:",ex);
//        Result error = Result.error(CodeEnum.ILLEGAL_ARGUMENT);
//        error.setMessage(ex.getMessage());
//        return error;
//    }
//
//}
