package com.gexingw.shop.component;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.util.StringUtils;

//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.cursor.Cursor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.apache.ibatis.transaction.Transaction;

import java.lang.reflect.Method;
import java.util.Properties;

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        ),
        @Signature(
                method = "query",
                type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
        )
})
public class MybatisInterceptor implements Interceptor {

    private final static String METHOD_UPDATE = "update";

    private final static String METHOD_QUERY = "query";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        String sql = mappedStatement.getBoundSql(parameter).getSql();
        Method method = invocation.getMethod();

        if (checkHasEmptyList(sql, method)) {
            return getDefaultReturnValue(method);
        }

        return invocation.proceed();
    }

    private Object getDefaultReturnValue(Method method) {
        if (method.getReturnType().equals(Integer.TYPE)) {
            return 0;
        }

        return null;
    }

    private Boolean checkHasEmptyList(String sql, Method method) {
        String methodName = method.getName();
        if ("update".equals(methodName)) {
            // 无条件插入的情况
            if (StringUtils.startsWithIgnoreCase(sql, "insert") && StringUtils.endsWithIgnoreCase(sql, "values")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

