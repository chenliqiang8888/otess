package com.otess.common.bean;

import java.util.List;

/**
 * @author j
 * @date 2015/11/24
 * @package com.otess.common.bean
 */
public class PageListResponse extends BaseResponse {
	private Integer draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
    private List<?> data;

    public PageListResponse() {
        super();
    }

    public PageListResponse(String message) {
        super(message);
    }

    public PageListResponse (List<?> data) {
        this.data = data;
    }

    public PageListResponse(Integer code) {
        super(code);
    }

    public PageListResponse(Integer code, String message) {
        super(code, message);
    }

    public PageListResponse setList(List<?> data) {
        this.data = data;
        return this;
    }

    public List<?> getData() {
        return data;
    }

    public PageListResponse setDraw(Integer draw) {
        this.draw = draw;
        return this;
    }

    public PageListResponse setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
        return this;
    }

    public PageListResponse setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
        return this;
    }
    public Integer getDraw() {
        return draw;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }
    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }
}
