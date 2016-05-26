package kr.co.miracom.alarm.vo;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by probyoo on 2016-05-23.
 */
public class AbstractVO implements Serializable {

    @Override
    public String toString() {
        return (new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE) {
            @Override
            protected boolean accept(Field f) {
                return super.accept(f);
            }
        }).toString();
    }
}
