package com.feige.api.handler;

import com.feige.api.msg.Msg;
import com.feige.fim.utils.AssertUtil;
import com.feige.fim.utils.Pair;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.Inject;
import com.feige.api.annotation.MsgComp;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.fim.utils.ClassGenerator;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.fim.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author feige<br />
 * @ClassName: AbstractMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:50<br/>
 */
public abstract class AbstractMsgHandler<T> implements MsgHandler<T> {
    
    public static class ClassGenerateParam {
        private byte serializerType;
        
        private Class<?> msgInterface;
        
        private Object[] args;

        public ClassGenerateParam(byte serializerType, Class<?> msgInterface, Object... args) {
            this.serializerType = serializerType;
            this.msgInterface = msgInterface;
            this.args = args;
        }

        public byte getSerializerType() {
            return serializerType;
        }

        public void setSerializerType(byte serializerType) {
            this.serializerType = serializerType;
        }

        public Class<?> getMsgInterface() {
            return msgInterface;
        }

        public void setMsgInterface(Class<?> msgInterface) {
            this.msgInterface = msgInterface;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }
    }
    
    @Inject
    protected SerializedClassManager serializedClassManager;
    
    @InitMethod
    public void initialize(){
        List<ClassGenerateParam> classGenerateParams = getClassGenerateParams();
        if (classGenerateParams == null || classGenerateParams.isEmpty()){
            return;
        }
        for (ClassGenerateParam cgp : classGenerateParams) {
            byte serializerType = cgp.getSerializerType();
            Class<Msg> msgInterface = (Class<Msg>) cgp.getMsgInterface();
            AssertUtil.notNull(msgInterface, "msg interface");
            serializedClassManager.getClass(serializerType, msgInterface, cgp::getArgs);
        }
    }
    
    public abstract List<ClassGenerateParam> getClassGenerateParams();

    
   
    
    
  
}
