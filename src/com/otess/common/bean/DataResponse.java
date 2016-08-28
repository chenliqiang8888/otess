package com.otess.common.bean;

import java.util.List;

/**
 * @author j
 * @date 2015/11/24
 * @package com.otess.common.bean
 */
public class DataResponse extends BaseResponse {
    private Object data;

    public DataResponse() {
        super();
    }

    public DataResponse(String message) {
        super(message);
    }

    public DataResponse (Object data) {
        this.data = data;
    }

    public DataResponse(Integer code) {
        super(code);
    }

    public DataResponse(Integer code, String message) {
        super(code, message);
    }

    public DataResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public Object getData() {
        return data;
    }
}
