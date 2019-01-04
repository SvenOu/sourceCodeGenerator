package com.sven.common.lib.bean;

import javax.servlet.http.HttpServletRequest;

public class InteractionContro {
    public static final String KEY_CONTRO = "contro";
    private String toUrl;
    private Object data;
    private boolean finish;

    public InteractionContro() {}

    public InteractionContro(String form) {
        this.toUrl = form;
        this.finish = false;
    }

    public InteractionContro(String form, Object data, boolean finish) {
        this.toUrl = form;
        this.data = data;
        this.finish = finish;
    }

    public static String getKeyContro() {
        return KEY_CONTRO;
    }

    public static InteractionContro finishInteraction(HttpServletRequest request, Object data) {
        InteractionContro contro = (InteractionContro)
                request.getAttribute(InteractionContro.KEY_CONTRO);
        if(contro == null){
            throw new RuntimeException("InteractionContro must not null");
        }
        contro.setFinish(true);
        contro.setData(data);
        request.setAttribute(InteractionContro.KEY_CONTRO, contro);
        contro.setData(data);
        return contro;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    @Override
    public String toString() {
        return "InteractionContro{" +
                "toUrl='" + toUrl + '\'' +
                ", data=" + data +
                ", finish=" + finish +
                '}';
    }
}
