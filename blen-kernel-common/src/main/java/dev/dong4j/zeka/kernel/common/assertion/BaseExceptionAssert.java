// package dev.dong4j.zeka.kernel.common.assertion;
//
// import dev.dong4j.zeka.kernel.common.api.IResultCode;
// import dev.dong4j.zeka.kernel.common.exception.BaseException;
//
// /**
//  * <p>Description: 全局错误异常断言 </p>
//  *
//  * @author dong4j
//  * @version 1.2.3
//  * @email "mailto:dong4j@gmail.com"
//  * @date 2019.12.24 12:20
//  * @since 1.0.0
//  */
// public interface BaseExceptionAssert extends IResultCode, IAssert {
//     /** serialVersionUID */
//     long serialVersionUID = 3077918845714343375L;
//
//     /**
//      * New exceptions base exception.
//      *
//      * @param args the args
//      * @return the base exception
//      * @since 1.0.0
//      */
//     @Override
//     default BaseException newException(Object... args) {
//         return new BaseException(this, args, this.getMessage());
//     }
//
//     /**
//      * New exceptions base exception.
//      *
//      * @param t    the t
//      * @param args the args
//      * @return the base exception
//      * @since 1.0.0
//      */
//     @Override
//     default BaseException newException(Throwable t, Object... args) {
//         return new BaseException(this, args, this.getMessage(), t);
//     }
//
// }
