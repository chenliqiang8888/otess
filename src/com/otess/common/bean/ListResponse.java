package com.otess.common.bean;

import java.util.List;

/**
 * @author j
 * @date 2015/11/24
 * @package com.otess.common.bean
 */
public class ListResponse extends BaseResponse {
    private List<?> list;

    public ListResponse() {
        super();
    }

    public ListResponse(String message) {
        super(message);
    }

    public ListResponse (List<?> list) {
        this.list = list;
    }

    public ListResponse(Integer code) {
        super(code);
    }

    public ListResponse(Integer code, String message) {
        super(code, message);
    }

    public ListResponse setList(List<?> list) {
        this.list = list;
        return this;
    }

    public List<?> getList() {
        return list;
    }
}
