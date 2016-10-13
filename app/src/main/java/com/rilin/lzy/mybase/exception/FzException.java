package com.rilin.lzy.mybase.exception;

/**    
 * @author cate
 * 2014-12-6 上午11:36:00   
 */

public class FzException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FzException() {
        super();
    }

    public FzException(String msg) {
        super(msg);
    }

    public FzException(Throwable ex) {
        super(ex);
    }

    public FzException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
